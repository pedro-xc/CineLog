package com.pedro.cinelog.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.cinelog.data.Resultado
import com.pedro.cinelog.data.remote.dto.MovieResultDto
import com.pedro.cinelog.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository.obterInstancia(application)

    private val _estadoBusca = MutableStateFlow<Resultado<List<MovieResultDto>>?>(null)
    val estadoBusca: StateFlow<Resultado<List<MovieResultDto>>?> = _estadoBusca

    fun buscar(consulta: String) {
        val consultaLimpa = consulta.trim()
        if (consultaLimpa.isEmpty()) return

        viewModelScope.launch {
            _estadoBusca.value = Resultado.Carregando
            _estadoBusca.value = repository.buscarFilmes(consultaLimpa)
        }
    }
}
