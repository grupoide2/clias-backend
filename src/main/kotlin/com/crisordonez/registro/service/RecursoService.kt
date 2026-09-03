package com.crisordonez.registro.service

import com.crisordonez.registro.model.entities.RecursoEntity
import com.crisordonez.registro.repository.RecursoRepository
import org.springframework.stereotype.Service

@Service
class RecursoService(private val recursoRepository: RecursoRepository) {

    fun aumentarContadorVistas(codigo: String) {
        // Busca el recurso por su código
        val recurso = recursoRepository.findByCodigo(codigo)

        if (recurso != null) {
            recurso.contador_vistas++
            recursoRepository.save(recurso)
            println("Incrementando contador para el recurso: $recurso")
        }
    }
}
