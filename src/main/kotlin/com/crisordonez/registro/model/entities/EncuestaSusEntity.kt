package com.crisordonez.registro.model.entities

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "encuesta_sus")
data class EncuestaSusEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var item1: Int,
    var item2: Int,
    var item3: Int,
    var item4: Int,
    var item5: Int,
    var item6: Int,
    var item7: Int,
    var item8: Int,
    var item9: Int,
    var item10: Int,
    var item11: Int,
    var item12: Int,
    var item13: Int,
    var item14: Int,

    @OneToOne
    @JoinColumn(name = "cuenta_usuario_id", nullable = false)
    val cuentaUsuario: CuentaUsuarioEntity

) : AuditModel()
