package com.crisordonez.registro.service

import com.crisordonez.registro.model.dtos.EstadoDispositivoDto
import com.crisordonez.registro.repository.EstadoDispositivoRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EstadoDispositivoService(
    private val estadoDispositivoRepository: EstadoDispositivoRepository
) {

    fun getEstadoDispositivos(
        fechaInicio: LocalDateTime?,
        fechaFin: LocalDateTime?,
        estado: String? = null,
        page: Int,
        size: Int
    ): Page<EstadoDispositivoDto> {
        val pageable = PageRequest.of(page, size)
        return getEstadoDispositivos(fechaInicio, fechaFin, estado, pageable)
    }

    fun getEstadoDispositivos(
        fechaInicio: LocalDateTime?,
        fechaFin: LocalDateTime?,
        estado: String? = null,
        pageable: Pageable
    ): Page<EstadoDispositivoDto> {
        val fromQR: List<EstadoDispositivoDto>
        val orphans: List<EstadoDispositivoDto>

        if (fechaInicio != null && fechaFin != null) {
            fromQR  = estadoDispositivoRepository.findEstadoDispositivosConFiltro(fechaInicio, fechaFin, Pageable.unpaged()).content
            orphans = estadoDispositivoRepository.findDispositivosRegistradosSinQRConFiltro(fechaInicio, fechaFin)
        } else {
            fromQR  = estadoDispositivoRepository.findEstadoDispositivosSinFiltro(Pageable.unpaged()).content
            orphans = estadoDispositivoRepository.findDispositivosRegistradosSinQR()
        }

        val merged = (fromQR + orphans).sortedBy { it.codigo }

        val filtered = if (!estado.isNullOrBlank() && !estado.equals("todos", ignoreCase = true)) {
            merged.filter { it.estado.equals(estado, ignoreCase = true) }
        } else {
            merged
        }

        val start = if (pageable.isUnpaged) 0 else pageable.offset.toInt().coerceAtMost(filtered.size)
        val end   = if (pageable.isUnpaged) filtered.size else (start + pageable.pageSize).coerceAtMost(filtered.size)

        return PageImpl(filtered.subList(start, end), pageable, filtered.size.toLong())
    }
}
