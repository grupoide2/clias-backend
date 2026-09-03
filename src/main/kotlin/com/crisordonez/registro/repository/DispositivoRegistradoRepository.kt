// src/main/kotlin/com/crisordonez/registro/repository/DispositivoRegistradoRepository.kt
package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.DispositivoRegistradoEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface DispositivoRegistradoRepository :
    JpaRepository<DispositivoRegistradoEntity, Long> {
    fun findByDispositivo(dispositivo: String): Optional<DispositivoRegistradoEntity>

    fun findFirstByDispositivo(dispositivo: String): Optional<DispositivoRegistradoEntity>
}
