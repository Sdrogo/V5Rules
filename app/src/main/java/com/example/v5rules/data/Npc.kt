package com.example.v5rules.data


data class Npc(
    val nome: String = "",
    val secondName: String? = "",
    val cognome: String = "",
    var isFavorite: Boolean = false
) {
    override fun toString(): String {
        return "$nome $secondName $cognome"
    }
}

enum class Gender {
    MALE,
    FEMALE
}

data class FavoriteNpc(
    val id: Int = 0,
    val name: String = "",
    val secondName: String? = "",
    val familyName: String = "",
    val nationality: String = ""
) {
    constructor(
        name: String,
        secondName: String?,
        familyName: String,
        nationality: String
    ) : this(0, name, secondName, familyName, nationality)
}

/**
 * Rappresenta le culture degli NPC con le loro regole di denominazione.
 * La stringa name deve corrispondere al campo 'nome' nel file name_list.json.
 *
 * @param displayName Nome della nazionalità localizzato (da usare in UI/Logica).
 * @param nameOrder Indica se l'ordine è Cognome-Nome (EASTERN) o Nome-Cognome (WESTERN).
 * @param hasGenderFamilyNameRules Indica se il cognome ha desinenze di genere (es. Russo).
 */
enum class NpcNationality(
    val displayName: String,
    val nameOrder: NameOrder,
    val hasGenderFamilyNameRules: Boolean,
    val supportsSecondName: Boolean
) {
    // ----------------------------------------------------
    // GRUPPO A: WESTERN Order (Nome Cognome) & Regole di genere specializzate
    // ----------------------------------------------------
    ISLANDESE("islandese", NameOrder.WESTERN, true, false), // DISATTIVATO
    RUSSO("russo", NameOrder.WESTERN, true, false),      // DISATTIVATO
    LITUANO("lituano", NameOrder.WESTERN, true, true),   // (Future: Mantenuto per opzione)
    LETTONE("lettone", NameOrder.WESTERN, true, true),   // (Future: Mantenuto per opzione)

    // ----------------------------------------------------
    // GRUPPO B: EASTERN Order (Cognome Nome) & Nessuna Regola di genere sul Cognome
    // ----------------------------------------------------
    CINESE("cinese", NameOrder.EASTERN, false, false),   // DISATTIVATO
    GIAPPONESE("giapponese", NameOrder.EASTERN, false, false), // DISATTIVATO
    COREANO("coreano", NameOrder.EASTERN, false, false),   // DISATTIVATO


    // ----------------------------------------------------
    // GRUPPO C: WESTERN Order (Nome Cognome) & Nessuna Regola di genere sul Cognome (DEFAULT)
    // ----------------------------------------------------
    ITALIANO("italiano", NameOrder.WESTERN, false, true),
    FRANCESE("francese", NameOrder.WESTERN, false, true),
    TEDESCO("tedesco", NameOrder.WESTERN, false, true),
    ALBANESE("albanese", NameOrder.WESTERN, false, true),
    MAROCCHINO("marocchino", NameOrder.WESTERN, false, true),
    FILIPPINO("filippino", NameOrder.WESTERN, false, true),
    SPAGNOLO("spagnolo", NameOrder.WESTERN, false, true),
    PORTOGHESE("portoghese", NameOrder.WESTERN, false, true),
    GRECO("greco", NameOrder.WESTERN, false, true),
    TURCO("turco", NameOrder.WESTERN, false, true),
    EGIZIANO("egiziano", NameOrder.WESTERN, false, true),
    SUDAFRICANO("sudafricano", NameOrder.WESTERN, false, true),
    NIGERIANO("nigeriano", NameOrder.WESTERN, false, true),
    POLACCO("polacco", NameOrder.WESTERN, false, true),
    KENIOTA("keniota", NameOrder.WESTERN, false, true),
    ETIOPE("etiope", NameOrder.WESTERN, false, true),
    SAUDITA("saudita", NameOrder.WESTERN, false, true),
    INDIANO("indiano", NameOrder.WESTERN, false, true),
    BRASILIANO("brasiliano", NameOrder.WESTERN, false, true),
    ARGENTINO("argentino", NameOrder.WESTERN, false, true),
    MESSICANO("messicano", NameOrder.WESTERN, false, true),
    CANADESE("canadese", NameOrder.WESTERN, false, true),
    AMERICANO("americano", NameOrder.WESTERN, false, true),
    BRITANNICO("britannico", NameOrder.WESTERN, false, true),
    IRLANDESE("irlandese", NameOrder.WESTERN, false, true),
    SCOZZESE("scozzese", NameOrder.WESTERN, false, true),
    GALLESE("gallese", NameOrder.WESTERN, false, true),
    OLANDESE("olandese", NameOrder.WESTERN, false, true),
    SVEDESE("svedese", NameOrder.WESTERN, false, true),
    NORVEGESE("norvegese", NameOrder.WESTERN, false, true),
    DANESE("danese", NameOrder.WESTERN, false, true),
    FINLANDESE("finlandese", NameOrder.WESTERN, false, true),
    AUSTRALIANO("australiano", NameOrder.WESTERN, false, true),
    NEOZELANDESE("neozelandese", NameOrder.WESTERN, false, true),
    EBRAICO("ebraico", NameOrder.WESTERN, false, true),
    SERBO("serbo", NameOrder.WESTERN, false, true),
    BOSNIACO("bosniaco", NameOrder.WESTERN, false, true),
    SLOVENO("sloveno", NameOrder.WESTERN, false, true),
    MACEDONE("macedone", NameOrder.WESTERN, false, true),
    MONTENEGRINO("montenegrino", NameOrder.WESTERN, false, true),
    BULGARO("bulgaro", NameOrder.WESTERN, false, true),
    KOSSOVARO("kosovaro", NameOrder.WESTERN, false, true),
    TUNISINO("tunisino", NameOrder.WESTERN, false, true),
    ALGERINO("algerino", NameOrder.WESTERN, false, true),
    SENEGALESE("senegalese", NameOrder.WESTERN, false, true),
    CROATO("croato", NameOrder.WESTERN, false, true),
    RUMENO("rumeno", NameOrder.WESTERN, false, true),

    // Nuove Nazionalità Aggiunte precedentemente
    ARABO("arabo", NameOrder.WESTERN, false, true),
    SCANDINAVO("scandinavo", NameOrder.WESTERN, false, true);

}

enum class NameOrder {
    WESTERN, // Nome (Secondo Nome) Cognome
    EASTERN  // Cognome Nome (Secondo Nome)
}