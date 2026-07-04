package com.pedro.cinelog.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.cinelog.data.Resultado
import com.pedro.cinelog.data.local.WatchedEntry
import com.pedro.cinelog.data.remote.dto.MovieDetailDto
import com.pedro.cinelog.data.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository.obterInstancia(application)

    private val _estadoDetalhe = MutableStateFlow<Resultado<MovieDetailDto>>(Resultado.Carregando)
    val estadoDetalhe: StateFlow<Resultado<MovieDetailDto>> = _estadoDetalhe

    fun carregarDetalhes(tmdbId: Int) {
        viewModelScope.launch {
            _estadoDetalhe.value = Resultado.Carregando
            _estadoDetalhe.value = repository.obterDetalhes(tmdbId)
        }
    }

    fun observarEntradaSalva(tmdbId: Int): Flow<WatchedEntry?> =
        repository.observarEntradaSalva(tmdbId)

    fun salvarNoDiario(entrada: WatchedEntry) {
        viewModelScope.launch {
            repository.salvarNoDiario(entrada)
        }
    }
}
