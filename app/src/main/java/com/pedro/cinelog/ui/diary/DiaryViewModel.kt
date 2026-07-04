package com.pedro.cinelog.ui.diary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.cinelog.data.local.WatchedEntry
import com.pedro.cinelog.data.repository.MovieRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class DiaryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository.obterInstancia(application)

    val listaDiario: StateFlow<List<WatchedEntry>> = repository.observarDiario()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
