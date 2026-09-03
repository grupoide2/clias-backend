package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.InformacionSocioeconomicaEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.UUID

interface InformacionSocioeconomicaRepository: CrudRepository<InformacionSocioeconomicaEntity, Long> {
    @Query("""
    SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END
    FROM InformacionSocioeconomicaEntity i
    WHERE i.paciente.cuenta.publicId = :cuentaUsuarioPublicId
""")
    fun existsByCuentaUsuarioPublicId(@Param("cuentaUsuarioPublicId") cuentaUsuarioPublicId: UUID): Boolean

}