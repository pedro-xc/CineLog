package com.pedro.cinelog.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchedEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvar(entrada: WatchedEntry): Long

    @Update
    suspend fun atualizar(entrada: WatchedEntry)

    @Delete
    suspend fun remover(entrada: WatchedEntry)

    @Query("SELECT * FROM watched_entries ORDER BY dataAssistido DESC, id DESC")
    fun observarTodas(): Flow<List<WatchedEntry>>

    @Query("SELECT * FROM watched_entries WHERE tmdbId = :tmdbId LIMIT 1")
    suspend fun buscarPorTmdbId(tmdbId: Int): WatchedEntry?

    @Query("SELECT * FROM watched_entries WHERE tmdbId = :tmdbId LIMIT 1")
    fun observarPorTmdbId(tmdbId: Int): Flow<WatchedEntry?>
}
