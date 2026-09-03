package com.crisordonez.registro.model.entities

import com.crisordonez.registro.model.enums.IngresoEnum
import com.crisordonez.registro.model.enums.InstruccionEnum
import com.crisordonez.registro.model.enums.OpcionesEnum
import com.crisordonez.registro.model.enums.OpcionesSiNoEnum
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "informacion_socioeconomica")
data class InformacionSocioeconomicaEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var publicId: UUID = UUID.randomUUID(),

    @Enumerated(EnumType.STRING)
    var instruccion: InstruccionEnum? = null,

    @Enumerated(EnumType.STRING)
    var ingresos: IngresoEnum? = null,

    @Enumerated(EnumType.STRING)
    var trabajoRemunerado: OpcionesEnum? = null,

    @Enumerated(EnumType.STRING)
    @Column(name="dependencia_universitaria", nullable = false, length = 2, columnDefinition = "varchar(2) default 'NO'")
    var dependenciaUniversitaria: OpcionesSiNoEnum = OpcionesSiNoEnum.NO,

    var ocupacion: String? = null,

    @Enumerated(EnumType.STRING)
    var recibeBono: OpcionesEnum? = null,

    @OneToOne
    @JoinColumn(name = "paciente_id")
    var paciente: PacienteEntity

) : AuditModel()
