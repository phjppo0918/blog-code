package com.example.sample.security.jwt

data class TokenClaims(
    val id: String,
    val role: Collection<String>,
) {
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun from(attributes: Map<String, Any>): TokenClaims =
            TokenClaims(
                id = attributes["id"] as String,
                role = attributes["role"] as ArrayList<String>,
            )
    }
}
