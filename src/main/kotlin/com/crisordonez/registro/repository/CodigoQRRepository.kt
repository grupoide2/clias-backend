package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.CodigoQREntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface CodigoQRRepository : JpaRepository<CodigoQREntity, Long> {
    fun findByCodigo(codigo: String): Optional<CodigoQREntity>
}
