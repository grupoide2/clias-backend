package com.crisordonez.registro.configuration

import com.crisordonez.registro.model.entities.OcupacionEntity
import com.crisordonez.registro.model.entities.RangoTiempoExamenEntity
import com.crisordonez.registro.repository.OcupacionRepository
import com.crisordonez.registro.repository.RangoTiempoExamenRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

/**
 * Siembra los catálogos base **solo en el primer arranque** (cuando la tabla está
 * vacía). Si ya existe al menos una fila, el seeder no toca nada: el catálogo lo
 * gestiona el admin desde clias-admin (crear / editar / eliminar) y esas
 * eliminaciones deben persistir entre reinicios.
 */
@Component
class CatalogoSeeder(
    private val ocupacionRepository: OcupacionRepository,
    private val rangoRepository: RangoTiempoExamenRepository,
) : ApplicationRunner {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun run(args: ApplicationArguments?) {
        seedOcupaciones()
        seedRangosTiempoExamen()
    }

    private fun seedOcupaciones() {
        if (ocupacionRepository.count() > 0L) return
        // nombre, soloUniversidad, orden
        val base = listOf(
            Triple("CONTRATO DE SERVICIO", true, 10),
            Triple("TRABAJADORA", true, 20),
            Triple("EMPLEADA", true, 30),
            Triple("SERVICIOS PROFESIONALES", true, 40),
            Triple("DOCENTE", true, 50),
            Triple("ADMINISTRATIVA", true, 60),
        )
        base.forEach { (nombre, soloUni, orden) ->
            if (!ocupacionRepository.existsByNombreIgnoreCase(nombre)) {
                ocupacionRepository.save(
                    OcupacionEntity(nombre = nombre, soloUniversidad = soloUni, orden = orden)
                )
                log.info("Seed ocupacion: $nombre")
            }
        }
    }

    private fun seedRangosTiempoExamen() {
        if (rangoRepository.count() > 0L) return
        // Un catálogo por pregunta de salud sexual. El código lleva prefijo de la pregunta
        // para mantenerse único globalmente. Campos: pregunta, codigo, etiqueta, orden.
        data class Rango(val pregunta: String, val codigo: String, val etiqueta: String, val orden: Int)
        val base = listOf(
            // ¿Hace cuánto fue el primer día de tu última menstruación?
            Rango("MENSTRUACION", "MENST_3_A_6_MESES", "Entre 3 y 6 meses", 10),
            Rango("MENSTRUACION", "MENST_6_A_12_MESES", "Entre 6 y 12 meses", 20),
            Rango("MENSTRUACION", "MENST_MAS_12_MESES", "Más de 12 meses", 30),
            // ¿Hace cuánto fue el último examen de Papanicolaou (Pap)?
            Rango("PAPANICOLAOU", "PAP_MENOS_1_ANIO", "Menos de 1 año", 10),
            Rango("PAPANICOLAOU", "PAP_1_A_3_ANIOS", "De 1 a 3 años", 20),
            Rango("PAPANICOLAOU", "PAP_MAS_3_ANIOS", "Más de 3 años", 30),
            Rango("PAPANICOLAOU", "PAP_NUNCA", "Nunca", 40),
            // ¿Cuándo fue la última prueba de VPH?
            Rango("VPH", "VPH_MENOS_1_ANIO", "Menos de 1 año", 10),
            Rango("VPH", "VPH_1_A_3_ANIOS", "De 1 a 3 años", 20),
            Rango("VPH", "VPH_MAS_3_ANIOS", "Más de 3 años", 30),
            Rango("VPH", "VPH_NUNCA", "Nunca", 40),
        )
        base.forEach { r ->
            if (!rangoRepository.existsByCodigo(r.codigo)) {
                rangoRepository.save(
                    RangoTiempoExamenEntity(
                        pregunta = r.pregunta,
                        codigo = r.codigo,
                        etiqueta = r.etiqueta,
                        orden = r.orden,
                    )
                )
                log.info("Seed rango_tiempo_examen: ${r.pregunta}/${r.codigo}")
            }
        }
    }
}
