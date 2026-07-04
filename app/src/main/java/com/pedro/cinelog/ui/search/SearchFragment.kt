package com.pedro.cinelog.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedro.cinelog.R
import com.pedro.cinelog.data.Resultado
import com.pedro.cinelog.data.remote.dto.MovieResultDto
import com.pedro.cinelog.databinding.FragmentSearchBinding
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SearchAdapter { filme -> irParaDetalhes(filme) }
        binding.recyclerViewResultados.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewResultados.adapter = adapter

        binding.campoBusca.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.buscar(textView.text.toString())
                true
            } else {
                false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.estadoBusca.collect { estado -> atualizarUi(estado) }
            }
        }
    }

    private fun atualizarUi(estado: Resultado<List<MovieResultDto>>?) {
        binding.progressBarBusca.isVisible = estado is Resultado.Carregando

        when (estado) {
            null -> {
                binding.textViewErro.isVisible = false
            }
            is Resultado.Carregando -> {
                binding.textViewErro.isVisible = false
            }
            is Resultado.Sucesso -> {
                adapter.atualizarLista(estado.dados)
                binding.textViewErro.isVisible = estado.dados.isEmpty()
                binding.textViewErro.text = getString(R.string.mensagem_nenhum_resultado)
            }
            is Resultado.Erro -> {
                adapter.atualizarLista(emptyList())
                binding.textViewErro.isVisible = true
                binding.textViewErro.text = estado.mensagem
            }
        }
    }

    private fun irParaDetalhes(filme: MovieResultDto) {
        val argumentos = Bundle().apply { putInt("tmdbId", filme.id) }
        findNavController().navigate(R.id.action_searchFragment_to_detailFragment, argumentos)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
