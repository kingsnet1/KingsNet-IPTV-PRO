package com.kingsnet.iptvpro.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "channels")
data class ChannelEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val url: String,
    val groupTitle: String?,
    val tvgLogo: String?,
    val tvgId: String?,
    val favorite: Boolean = false
)
