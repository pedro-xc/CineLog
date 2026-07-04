package com.pedro.cinelog.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Resposta do endpoint /movie/{id} do TMDb, com os detalhes completos de um filme.
 */
data class MovieDetailDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val titulo: String,
    @SerializedName("overview")
    val sinopse: String?,
    @SerializedName("release_date")
    val dataLancamento: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("vote_average")
    val notaMedia: Double?,
    @SerializedName("runtime")
    val duracaoMinutos: Int?
)
