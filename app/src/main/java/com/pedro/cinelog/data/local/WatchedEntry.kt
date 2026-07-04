package com.pedro.cinelog.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entrada do diário pessoal de filmes/séries, salva localmente no Room.
 * Depois de salva, a tela de Diário não depende mais de chamadas à API do TMDb.
 */
@Entity(tableName = "watched_entries")
data class WatchedEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tmdbId: Int,
    val titulo: String,
    val posterUrl: String?,
    val anoLancamento: String?,
    val notaTmdb: Double?,
    val notaPessoal: Float?,
    val comentario: String?,
    val dataAssistido: Long?,
    val status: StatusAssistido
)
