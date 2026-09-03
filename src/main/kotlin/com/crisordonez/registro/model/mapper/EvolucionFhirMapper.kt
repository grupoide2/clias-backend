package com.crisordonez.registro.model.mapper

import com.crisordonez.registro.model.entities.EvolucionEntity
import org.hl7.fhir.r4.model.*
import org.hl7.fhir.utilities.xhtml.XhtmlNode
import java.math.BigDecimal
import java.time.ZoneId
import java.util.Date
import java.util.UUID

object EvolucionFhirMapper {

    fun toFhirBundle(e: EvolucionEntity): Bundle {
        val bundle = Bundle().apply {
            type = Bundle.BundleType.COLLECTION
            id = "Bundle/${UUID.randomUUID()}"
        }

        val observations = buildObservations(e)
        observations.forEach { obs ->
            bundle.addEntry(Bundle.BundleEntryComponent().apply {
                fullUrl = "urn:uuid:${obs.idElement.idPart}"
                resource = obs
            })
        }

        return bundle
    }

    private fun buildObservations(e: EvolucionEntity): List<Observation> {
        val list = mutableListOf<Observation>()

        val fecha: Date? = e.fecha?.let {
            Date.from(it.atZone(ZoneId.systemDefault()).toInstant())
        }

        e.temperatura?.let {
            list += buildQuantityObservation(
                display = "Temperatura corporal",
                unit = "°C",
                value = it,
                fecha = fecha
            )
        }

        e.pulso?.let {
            list += buildQuantityObservation(
                display = "Frecuencia cardíaca",
                unit = "lpm",
                value = it,
                fecha = fecha
            )
        }

        e.talla?.let {
            list += buildQuantityObservation(
                display = "Talla",
                unit = "cm",
                value = it,
                fecha = fecha
            )
        }

        e.peso?.let {
            list += buildQuantityObservation(
                display = "Peso",
                unit = "kg",
                value = it,
                fecha = fecha
            )
        }

        return list
    }

    private fun buildQuantityObservation(
        display: String,
        unit: String,
        value: Number,
        fecha: Date?
    ): Observation {
        val obs = Observation()
        obs.id = "Observation/${UUID.randomUUID()}"
        obs.status = Observation.ObservationStatus.FINAL
        obs.category = listOf(
            CodeableConcept().addCoding(
                Coding(
                    "http://terminology.hl7.org/CodeSystem/observation-category",
                    "vital-signs",
                    "Vital Signs"
                )
            )
        )
        obs.code = CodeableConcept().apply { text = display }

        val qty = Quantity().apply {
            this.value = BigDecimal.valueOf(value.toDouble())
            this.unit = unit
        }
        obs.setValue(qty)

        fecha?.let { obs.effective = DateTimeType(it) }

        obs.text = Narrative().apply {
            status = Narrative.NarrativeStatus.GENERATED
            val resumen = "$display: ${value} $unit"
            div = XhtmlNode(org.hl7.fhir.utilities.xhtml.NodeType.Element, "div").apply { addText(resumen) }
        }

        return obs
    }
}
