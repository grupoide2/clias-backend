package com.crisordonez.registro.repository

import com.crisordonez.registro.model.entities.EncuestaSusEntity
import org.springframework.data.jpa.repository.JpaRepository
import com.crisordonez.registro.model.entities.CuentaUsuarioEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID


interface EncuestaSusRepository : JpaRepository<EncuestaSusEntity, Long> {
    fun findByCuentaUsuario(cuentaUsuario: CuentaUsuarioEntity): EncuestaSusEntity?
    @Query("""
    SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END
    FROM EncuestaSusEntity e
    WHERE e.cuentaUsuario.publicId = :cuentaUsuarioPublicId
""")
    fun existsByCuentaUsuarioPublicId(@Param("cuentaUsuarioPublicId") cuentaUsuarioPublicId: UUID): Boolean


}
