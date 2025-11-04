package com.kingsnet.iptvpro.model

data class Channel(
    val name: String,
    val url: String,
    val group: String? = null,
    val tvgLogo: String? = null,
    val tvgId: String? = null
)
