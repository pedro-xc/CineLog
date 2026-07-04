package com.pedro.cinelog.ui.stats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.cinelog.data.local.StatusAssistido
import com.pedro.cinelog.data.repository.MovieRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class StatsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository.obterInstancia(application)

    val estatisticas: StateFlow<EstatisticasUiState> = repository.observarDiario()
        .map { lista ->
            val assistidos = lista.filter { it.status == StatusAssistido.ASSISTIDO }
            val queroAssistir = lista.filter { it.status == StatusAssistido.QUERO_ASSISTIR }
            val notasPessoais = assistidos.mapNotNull { it.notaPessoal }

            EstatisticasUiState(
                totalAssistido = assistidos.size,
                totalQueroAssistir = queroAssistir.size,
                notaMediaPessoal = if (notasPessoais.isNotEmpty()) notasPessoais.average() else null
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EstatisticasUiState())
}
