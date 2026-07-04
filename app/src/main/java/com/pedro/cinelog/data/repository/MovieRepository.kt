package com.pedro.cinelog.data.repository

import android.content.Context
import com.pedro.cinelog.data.Resultado
import com.pedro.cinelog.data.local.CineLogDatabase
import com.pedro.cinelog.data.local.WatchedEntry
import com.pedro.cinelog.data.local.WatchedEntryDao
import com.pedro.cinelog.data.remote.RetrofitClient
import com.pedro.cinelog.data.remote.TmdbApiService
import com.pedro.cinelog.data.remote.dto.MovieDetailDto
import com.pedro.cinelog.data.remote.dto.MovieResultDto
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

/**
 * Unifica a API remota do TMDb com a persistência local (Room).
 * Depois que um filme/série é salvo no diário, as telas que dependem apenas
 * do diário (Diário e Estatísticas) não fazem mais nenhuma chamada de rede.
 */
class MovieRepository(
    private val apiService: TmdbApiService,
    private val dao: WatchedEntryDao
) {

    suspend fun buscarFilmes(consulta: String): Resultado<List<MovieResultDto>> {
        return try {
            val resposta = apiService.buscarFilmes(consulta = consulta)
            Resultado.Sucesso(resposta.resultados)
        } catch (e: IOException) {
            Resultado.Erro("Sem conexão com a internet. Verifique sua rede e tente novamente.")
        } catch (e: HttpException) {
            Resultado.Erro("Erro ao buscar filmes (código ${e.code()}). Tente novamente mais tarde.")
        } catch (e: Exception) {
            Resultado.Erro("Ocorreu um erro inesperado ao buscar filmes.")
        }
    }

    suspend fun obterDetalhes(tmdbId: Int): Resultado<MovieDetailDto> {
        return try {
            Resultado.Sucesso(apiService.obterDetalhes(tmdbId))
        } catch (e: IOException) {
            Resultado.Erro("Sem conexão com a internet. Verifique sua rede e tente novamente.")
        } catch (e: HttpException) {
            Resultado.Erro("Erro ao carregar detalhes (código ${e.code()}). Tente novamente mais tarde.")
        } catch (e: Exception) {
            Resultado.Erro("Ocorreu um erro inesperado ao carregar os detalhes.")
        }
    }

    fun observarDiario(): Flow<List<WatchedEntry>> = dao.observarTodas()

    fun observarEntradaSalva(tmdbId: Int): Flow<WatchedEntry?> = dao.observarPorTmdbId(tmdbId)

    suspend fun buscarEntradaSalva(tmdbId: Int): WatchedEntry? = dao.buscarPorTmdbId(tmdbId)

    /** Insere uma nova entrada ou atualiza a existente, mantendo apenas uma por tmdbId. */
    suspend fun salvarNoDiario(entrada: WatchedEntry) {
        val existente = dao.buscarPorTmdbId(entrada.tmdbId)
        if (existente != null) {
            dao.atualizar(entrada.copy(id = existente.id))
        } else {
            dao.salvar(entrada)
        }
    }

    suspend fun removerDoDiario(entrada: WatchedEntry) = dao.remover(entrada)

    companion object {
        @Volatile
        private var instancia: MovieRepository? = null

        fun obterInstancia(context: Context): MovieRepository {
            return instancia ?: synchronized(this) {
                instancia ?: MovieRepository(
                    apiService = RetrofitClient.apiService,
                    dao = CineLogDatabase.obterInstancia(context).watchedEntryDao()
                ).also { instancia = it }
            }
        }
    }
}
