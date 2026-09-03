package com.crisordonez.registro.model.entities

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Column
import jakarta.persistence.GenerationType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
@Table(name = "recursos")
data class RecursoEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false, unique = true)
    val codigo: String,

    @Column(nullable = false)
    val tipo: String,

    @Column(nullable = false)
    var contador_vistas: Long = 0
)
