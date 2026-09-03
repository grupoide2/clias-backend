package com.crisordonez.registro.model.entities

import com.crisordonez.registro.model.enums.EstablecimientoEnum
import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.UUID


@Entity
@Table(name = "ubicaciones")
data class UbicacionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "public_id", nullable = false, unique = true)
    var publicId: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var nombre: String,

    @Column()
    var telefono: String,

    @Column(nullable = false)
    var direccion: String,

    @Column(name = "horario_atencion")
    var horario: String,

    @Column(name = "sitio_web")
    var sitioWeb: String,

    @Column(nullable = false)
    var latitud: Double,

    @Column(nullable = false)
    var longitud: Double,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var establecimiento: EstablecimientoEnum = EstablecimientoEnum.CENTRO_SALUD,

    @Column(name = "creado_en", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "actualizado_en", nullable = false)
    var updatedAt: OffsetDateTime = OffsetDateTime.now()
)