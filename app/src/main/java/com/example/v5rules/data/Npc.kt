package com.example.v5rules.data

data class Npc(
    var nome: String,
    var secondName: String? = null,
    var cognome: String,
) {
    override fun toString(): String {
        return "${nome} ${secondName.orEmpty() + " "}${cognome}"
    }
}

enum class Gender {
    MALE,
    FEMALE
}

enum class RegenerationType {
    NAME,
    SECOND_NAME,
    FAMILY_NAME,
    ALL
}
