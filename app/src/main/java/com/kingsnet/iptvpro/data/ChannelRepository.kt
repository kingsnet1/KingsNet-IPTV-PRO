package com.kingsnet.iptvpro.data

import android.content.Context
import com.kingsnet.iptvpro.data.db.AppDatabase
import com.kingsnet.iptvpro.data.db.ChannelEntity
import kotlinx.coroutines.flow.Flow

class ChannelRepository(context: Context) {
    private val db = AppDatabase.get(context)
    private val dao = db.channelDao()

    fun getAll(): Flow<List<ChannelEntity>> = dao.getAll()
    fun getFavorites(): Flow<List<ChannelEntity>> = dao.getFavorites()

    suspend fun insertAll(list: List<ChannelEntity>) = dao.insertAll(list)
    suspend fun insert(entity: ChannelEntity) = dao.insert(entity)
    suspend fun update(entity: ChannelEntity) = dao.update(entity)
    suspend fun clear() = dao.clear()
}
