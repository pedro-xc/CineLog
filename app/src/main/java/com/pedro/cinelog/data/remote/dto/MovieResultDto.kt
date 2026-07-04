package com.pedro.cinelog.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Item de resultado retornado pelo endpoint de busca (/search/movie) do TMDb.
 */
data class MovieResultDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val titulo: String,
    @SerializedName("release_date")
    val dataLancamento: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("vote_average")
    val notaMedia: Double?,
    @SerializedName("overview")
    val sinopse: String?
)
