package com.crisordonez.registro.model.enums

enum class EstadoCivilEnum {
    SOLTERO {
        override fun toString(): String {
            return "SOLTERO/A"
        }
    },
    CASADO {
        override fun toString(): String {
            return "CASADO/A"
        }
    },
    VIUDO {
        override fun toString(): String {
            return "VIUDO/A"
        }
    },
    DIVORCIADO {
        override fun toString(): String {
            return "DIVORCIADO/A"
        }
    },
    UNION_LIBRE {
        override fun toString(): String {
            return "UNION LIBRE"
        }
    };

    companion object {
        fun getEnum(value: String?): EstadoCivilEnum? {
            for (v in EstadoCivilEnum.entries) {
                if (v.toString() == value) {
                    return v
                }
            }
            return null
        }
    }
}