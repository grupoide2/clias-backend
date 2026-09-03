package com.crisordonez.registro.model.enums

enum class EstablecimientoEnum {
    CENTRO_SALUD {
        override fun toString(): String {
            return "CENTROS DE SALUD Y HOSPITALES"
        }
    },
    CENTRO_PROTECCION {
        override fun toString(): String {
            return "CENTROS DE PROTECCIÓN CONTRA LA VIOLENCIA"
        }
    },
    ATENCION_PSICOLOGICA {
        override fun toString(): String {
            return "CENTROS DE ATENCIÓN PSICOLÓGICA"
        }
    };

    companion object {
        fun getEnum(value: String?): EstablecimientoEnum? {
            for (v in EstablecimientoEnum.entries) {
                if (v.toString() == value) {
                    return v
                }
            }
            return null
        }
    }
}