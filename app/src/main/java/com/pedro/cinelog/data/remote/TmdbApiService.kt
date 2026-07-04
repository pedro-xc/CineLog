package com.pedro.cinelog.data.remote

import com.pedro.cinelog.data.remote.dto.MovieDetailDto
import com.pedro.cinelog.data.remote.dto.MovieSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Endpoints da API do TMDb usados pelo app.
 * A api_key é injetada automaticamente em toda chamada pelo ApiKeyInterceptor,
 * então não precisa ser passada aqui.
 */
interface TmdbApiService {

    @GET("search/movie")
    suspend fun buscarFilmes(
        @Query("query") consulta: String,
        @Query("page") pagina: Int = 1,
        @Query("language") idioma: String = "pt-BR"
    ): MovieSearchResponseDto

    @GET("movie/{id}")
    suspend fun obterDetalhes(
        @Path("id") tmdbId: Int,
        @Query("language") idioma: String = "pt-BR"
    ): MovieDetailDto
}
