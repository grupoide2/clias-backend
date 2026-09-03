package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.RecursoEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RecursoRepository : JpaRepository<RecursoEntity, Long> {
    fun findByCodigo(codigo: String): RecursoEntity?
}
