package com.crisordonez.registro.model.entities

import com.crisordonez.registro.model.enums.EstadoCivilEnum
import com.crisordonez.registro.model.enums.IdiomaEnum
import com.crisordonez.registro.model.enums.PaisEnum
import com.crisordonez.registro.model.enums.SexoEnum
import com.crisordonez.registro.model.enums.ZonaResidencialEnum
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "pacientes")
data class PacienteEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "public_id", nullable = false, unique = true)
    var publicId: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var nombre: String,

    @Column(nullable = false)
    var fechaNacimiento: Date,

    @Enumerated(EnumType.STRING)
    var pais: PaisEnum = PaisEnum.ECUADOR,

    @Enumerated(EnumType.STRING)
    @Column(name="zona_residencial", length = 10)
    var zonaResidencial: ZonaResidencialEnum? = null,

    @Enumerated(EnumType.STRING)
    var lenguaMaterna: IdiomaEnum = IdiomaEnum.ESPANOL,

    @Enumerated(EnumType.STRING)
    var estadoCivil: EstadoCivilEnum = EstadoCivilEnum.CASADO,

    @Enumerated(EnumType.STRING)
    var sexo: SexoEnum = SexoEnum.FEMENINO,

    var identificacion: String? = null,

    @OneToOne(fetch = FetchType.LAZY,optional = false, cascade = [CascadeType.MERGE])
    @JoinColumn(
        name = "cuenta_id",
        referencedColumnName = "id",
        nullable = false
    )
    var cuenta: CuentaUsuarioEntity,

    @OneToOne
    @JoinColumn(name = "info_socioeconomica_id")
    var informacionSocioeconomica: InformacionSocioeconomicaEntity? = null,

    @OneToOne
    @JoinColumn(name = "anamnesis_id")
    var anamnesis: AnamnesisEntity? = null,

    @OneToOne(mappedBy = "paciente", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var saludSexual: SaludSexualEntity? = null,

    @OneToMany(mappedBy = "paciente", fetch = FetchType.LAZY)
    var sesionChat: MutableList<SesionChatEntity> = mutableListOf(),

    @OneToMany(mappedBy = "paciente", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var dispositivos: MutableList<DispositivoRegistradoEntity> = mutableListOf()

) : AuditModel()

