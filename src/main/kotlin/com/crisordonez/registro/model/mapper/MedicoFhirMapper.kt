package com.crisordonez.registro.model.mapper

import com.crisordonez.registro.model.entities.MedicoEntity
import org.hl7.fhir.r4.model.*
import org.hl7.fhir.utilities.xhtml.XhtmlNode

object MedicoFhirMapper {

    fun mapMedicoToFhir(m: MedicoEntity): Practitioner {
        val practitioner = Practitioner()

        practitioner.id = "Practitioner/${m.publicId}"

        practitioner.addIdentifier(
            Identifier()
                .setSystem("https://clias.ucuenca.edu.ec/fhir/ids/medico")
                .setValue(m.publicId.toString())
                .setType(
                    CodeableConcept().addCoding(
                        Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "MR", "Medical record number")
                    )
                )
        )
        m.nRegistro?.takeIf { it.isNotBlank() }?.let { nro ->
            practitioner.addIdentifier(
                Identifier()
                    .setSystem("https://clias.ucuenca.edu.ec/fhir/licenses/medico")
                    .setValue(nro)
                    .setType(
                        CodeableConcept().addCoding(
                            Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "PRN", "Professional license number")
                        )
                    )
            )
        }

        val parts = m.nombre.trim().split(Regex("\\s+"))
        val human = HumanName().apply {
            use = HumanName.NameUse.OFFICIAL
            if (parts.size > 1) {
                family = parts.last()
                given = parts.dropLast(1).map { StringType(it) }
            } else {
                given = listOf(StringType(m.nombre))
            }
        }
        practitioner.addName(human)

        try {
            val gender = when (m.sexo.name) {
                "MASCULINO" -> Enumerations.AdministrativeGender.MALE
                "FEMENINO"  -> Enumerations.AdministrativeGender.FEMALE
                else        -> Enumerations.AdministrativeGender.UNKNOWN
            }
            practitioner.gender = gender
        } catch (_: Exception) {
            practitioner.gender = Enumerations.AdministrativeGender.UNKNOWN
        }

        m.correo?.takeIf { it.isNotBlank() }?.let { email ->
            practitioner.addTelecom(
                ContactPoint()
                    .setSystem(ContactPoint.ContactPointSystem.EMAIL)
                    .setUse(ContactPoint.ContactPointUse.WORK)
                    .setValue(email)
            )
        }

        m.especializacion?.takeIf { it.isNotBlank() }?.let { esp ->
            val qual = Practitioner.PractitionerQualificationComponent()
            val concept = CodeableConcept()

            val snomed = when (esp.lowercase()) {
                "ginecología", "ginecologia" -> "394586005" to "Gynecology"
                "cardiología", "cardiologia" -> "394579002" to "Cardiology"
                "pediatría", "pediatria"     -> "394537008" to "Pediatrics"
                "dermatología", "dermatologia"-> "394582007" to "Dermatology"
                "neurología", "neurologia"   -> "394591006" to "Neurology"
                "genética", "genetica"       -> "1304107001" to "Genetics service"
                else -> null
            }

            if (snomed != null) {
                concept.addCoding(
                    Coding("http://snomed.info/sct", snomed.first, snomed.second)
                )
                concept.text = esp
            } else {
                concept.text = esp
            }
            qual.code = concept
            practitioner.addQualification(qual)
        }

        val narrative = Narrative().apply {
            status = Narrative.NarrativeStatus.GENERATED
            div = XhtmlNode(org.hl7.fhir.utilities.xhtml.NodeType.Element, "div")
                .apply { addText("Profesional ${m.nombre} (${m.especializacion ?: "Especialidad no especificada"}).") }
        }
        practitioner.text = narrative

        practitioner.active = true

        return practitioner
    }
}
