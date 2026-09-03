package com.crisordonez.registro.model.mapper

import com.crisordonez.registro.model.entities.ExamenVphEntity
import org.hl7.fhir.r4.model.*
import org.hl7.fhir.utilities.xhtml.XhtmlNode
import java.time.ZoneId
import java.util.Date
import java.util.UUID

object ExamenVphFhirMapper {

    fun toFhirBundle(ex: ExamenVphEntity): Bundle {
        val bundle = Bundle().apply {
            type = Bundle.BundleType.COLLECTION
            id = "Bundle/${UUID.randomUUID()}"
        }

        val patientRef = buildPatientReference(ex)

        val riskObs = ex.diagnostico?.takeIf { it.isNotBlank() }?.let {
            buildRiskObservation(ex, patientRef, it)
        }

        val genotypeObs = ex.genotipos
            ?.filterNotNull()
            ?.map { it.trim() }
            ?.filter { it.isNotBlank() }
            ?.map { buildGenotypeObservation(ex, patientRef, it) }
            ?: emptyList()

        val report = buildDiagnosticReport(ex, patientRef, riskObs, genotypeObs)

        riskObs?.let { o ->
            bundle.addEntry(Bundle.BundleEntryComponent().apply {
                fullUrl = "urn:uuid:${o.idElement.idPart}"
                resource = o
            })
        }
        genotypeObs.forEach { o ->
            bundle.addEntry(Bundle.BundleEntryComponent().apply {
                fullUrl = "urn:uuid:${o.idElement.idPart}"
                resource = o
            })
        }
        bundle.addEntry(Bundle.BundleEntryComponent().apply {
            fullUrl = "urn:uuid:${report.idElement.idPart}"
            resource = report
        })

        return bundle
    }

    private fun buildPatientReference(ex: ExamenVphEntity): Reference {
        val pacienteId: String? = try {
            ex.saludSexual?.paciente?.publicId?.toString()
                ?: ex.sesionChat?.paciente?.publicId?.toString()
        } catch (_: Exception) {
            null
        }

        return if (!pacienteId.isNullOrBlank()) {
            Reference("Patient/$pacienteId")
        } else {
            Reference().apply { display = "Paciente no resuelto" }
        }
    }

    private fun buildRiskObservation(
        ex: ExamenVphEntity,
        patientRef: Reference,
        diagnostico: String
    ): Observation {
        val obs = Observation()
        obs.id = "Observation/${UUID.randomUUID()}"
        obs.status = if (ex.fechaResultado != null)
            Observation.ObservationStatus.FINAL
        else
            Observation.ObservationStatus.PRELIMINARY

        obs.category = listOf(
            CodeableConcept().addCoding(
                Coding("http://terminology.hl7.org/CodeSystem/observation-category", "laboratory", "Laboratory")
            )
        )
        obs.code = CodeableConcept().apply { text = "HPV Risk Category" }
        obs.value = CodeableConcept().apply { text = diagnostico.trim().uppercase() }

        ex.fechaExamen?.let {
            obs.effective = DateTimeType(Date.from(it.atZone(ZoneId.systemDefault()).toInstant()))

        }
        ex.fechaResultado?.let {
            obs.issued = it
        }

        if (!patientRef.reference.isNullOrBlank()) obs.subject = patientRef

        ex.dispositivo?.takeIf { it.isNotBlank() }?.let { code ->
            obs.addIdentifier(
                Identifier()
                    .setSystem("https://clias.ucuenca.edu.ec/identifiers/dispositivo")
                    .setValue(code)
            )
        }
        return obs
    }

    private fun buildGenotypeObservation(
        ex: ExamenVphEntity,
        patientRef: Reference,
        genotype: String
    ): Observation {
        val obs = Observation()
        obs.id = "Observation/${UUID.randomUUID()}"
        obs.status = if (ex.fechaResultado != null)
            Observation.ObservationStatus.FINAL
        else
            Observation.ObservationStatus.PRELIMINARY

        obs.category = listOf(
            CodeableConcept().addCoding(
                Coding("http://terminology.hl7.org/CodeSystem/observation-category", "laboratory", "Laboratory")
            )
        )
        obs.code = CodeableConcept().apply { text = "HPV Genotype Detected" }
        obs.value = CodeableConcept().apply { text = genotype }

        ex.fechaExamen?.let {
            obs.effective = DateTimeType(Date.from(it.atZone(ZoneId.systemDefault()).toInstant()))
        }
        ex.fechaResultado?.let {
            obs.issued = it
        }

        if (!patientRef.reference.isNullOrBlank()) obs.subject = patientRef

        ex.dispositivo?.takeIf { it.isNotBlank() }?.let { code ->
            obs.addIdentifier(
                Identifier()
                    .setSystem("https://clias.ucuenca.edu.ec/identifiers/dispositivo")
                    .setValue(code)
            )
        }
        return obs
    }

    private fun buildDiagnosticReport(
        ex: ExamenVphEntity,
        patientRef: Reference,
        riskObs: Observation?,
        genotypeObs: List<Observation>
    ): DiagnosticReport {
        val dr = DiagnosticReport()
        dr.id = "DiagnosticReport/${UUID.randomUUID()}"
        dr.status = if (ex.fechaResultado != null)
            DiagnosticReport.DiagnosticReportStatus.FINAL
        else
            DiagnosticReport.DiagnosticReportStatus.PRELIMINARY

        dr.category = listOf(
            CodeableConcept().addCoding(
                Coding("http://terminology.hl7.org/CodeSystem/v2-0074", "LAB", "Laboratory")
            )
        )
        dr.code = CodeableConcept().apply { text = "HPV Panel Report" }

        if (!patientRef.reference.isNullOrBlank()) dr.subject = patientRef

        ex.fechaExamen?.let {
            dr.effective = DateTimeType(Date.from(it.atZone(ZoneId.systemDefault()).toInstant()))
        }
        ex.fechaResultado?.let {
            dr.issued = it
        }

        ex.dispositivo?.takeIf { it.isNotBlank() }?.let { code ->
            dr.addIdentifier(
                Identifier()
                    .setSystem("https://clias.ucuenca.edu.ec/identifiers/dispositivo")
                    .setValue(code)
            )
        }

        riskObs?.let { dr.addResult(Reference("urn:uuid:${it.idElement.idPart}")) }
        genotypeObs.forEach { ob -> dr.addResult(Reference("urn:uuid:${ob.idElement.idPart}")) }


        dr.text = Narrative().apply {
            status = Narrative.NarrativeStatus.GENERATED
            val genos = ex.genotipos?.filterNotNull()?.map { it.trim() }?.filter { it.isNotBlank() } ?: emptyList()
            val resumen = "Informe VPH. Riesgo: ${ex.diagnostico ?: "No especificado"}. Genotipos: ${if (genos.isEmpty()) "N/D" else genos.joinToString("~")}."
            div = XhtmlNode(org.hl7.fhir.utilities.xhtml.NodeType.Element, "div").apply { addText(resumen) }
        }

        return dr
    }
}
