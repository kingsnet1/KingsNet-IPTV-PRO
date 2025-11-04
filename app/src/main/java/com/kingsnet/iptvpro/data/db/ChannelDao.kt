package com.kingsnet.iptvpro.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelDao {
    @Query("SELECT * FROM channels ORDER BY name")
    fun getAll(): Flow<List<ChannelEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<ChannelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ChannelEntity): Long

    @Update
    suspend fun update(channel: ChannelEntity)

    @Query("DELETE FROM channels")
    suspend fun clear()

    @Query("SELECT * FROM channels WHERE favorite = 1 ORDER BY name")
    fun getFavorites(): Flow<List<ChannelEntity>>
}
