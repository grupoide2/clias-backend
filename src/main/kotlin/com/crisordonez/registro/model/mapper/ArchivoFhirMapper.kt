package com.crisordonez.registro.model.mapper

import com.crisordonez.registro.model.entities.ArchivoEntity
import org.hl7.fhir.r4.model.Attachment
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.DocumentReference
import org.hl7.fhir.r4.model.Enumerations
import org.hl7.fhir.r4.model.Identifier
import org.hl7.fhir.r4.model.Narrative
import org.hl7.fhir.utilities.xhtml.NodeType
import org.hl7.fhir.utilities.xhtml.XhtmlNode
import java.util.UUID

object ArchivoFhirMapper {

    fun toFhirBundle(a: ArchivoEntity): Bundle {
        val bundle = Bundle().apply {
            type = Bundle.BundleType.COLLECTION
            id = "Bundle/${UUID.randomUUID()}"
        }

        val docRef = buildDocumentReference(a)

        bundle.addEntry(Bundle.BundleEntryComponent().apply {
            fullUrl = "urn:uuid:${docRef.idElement.idPart}"
            resource = docRef
        })

        return bundle
    }

    private fun buildDocumentReference(a: ArchivoEntity): DocumentReference {
        val doc = DocumentReference()
        doc.id = "DocumentReference/${UUID.randomUUID()}"

        doc.status = Enumerations.DocumentReferenceStatus.CURRENT

        doc.docStatus = DocumentReference.ReferredDocumentStatus.FINAL

        doc.addIdentifier(
            Identifier()
                .setSystem("https://clias.ucuenca.edu.ec/identifiers/archivo")
                .setValue(a.publicId.toString())
        )

        doc.type = CodeableConcept().apply { text = "Archivo clínico adjunto" }

        val att = Attachment().apply {
            contentType = a.tipo ?: "application/octet-stream"
            title = a.nombre ?: "archivo"
            data = a.contenido
            size = if (a.tamano > Int.MAX_VALUE) Int.MAX_VALUE else a.tamano.toInt()
        }
        doc.content = listOf(DocumentReference.DocumentReferenceContentComponent(att))

        doc.text = Narrative().apply {
            status = Narrative.NarrativeStatus.GENERATED
            val nombre = a.nombre ?: "archivo"
            val tipo = a.tipo ?: "desconocido"
            val tam = a.tamano
            val resumen = "Archivo: $nombre ($tipo), tamaño $tam bytes."
            div = XhtmlNode(NodeType.Element, "div").apply { addText(resumen) }
        }

        return doc
    }
}
