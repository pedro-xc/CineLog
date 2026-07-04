package com.pedro.cinelog.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Resposta completa do endpoint /search/movie do TMDb.
 */
data class MovieSearchResponseDto(
    @SerializedName("page")
    val pagina: Int,
    @SerializedName("results")
    val resultados: List<MovieResultDto>,
    @SerializedName("total_pages")
    val totalPaginas: Int,
    @SerializedName("total_results")
    val totalResultados: Int
)
