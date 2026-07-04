package com.pedro.cinelog.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.pedro.cinelog.BuildConfig
import com.pedro.cinelog.R
import com.pedro.cinelog.data.Resultado
import com.pedro.cinelog.data.local.StatusAssistido
import com.pedro.cinelog.data.local.WatchedEntry
import com.pedro.cinelog.data.remote.dto.MovieDetailDto
import com.pedro.cinelog.databinding.FragmentDetailBinding
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()
    private val tmdbId: Int by lazy { requireArguments().getInt("tmdbId") }

    private var detalheAtual: MovieDetailDto? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.carregarDetalhes(tmdbId)

        binding.botaoSalvar.setOnClickListener { salvarNoDiario() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.estadoDetalhe.collect { estado -> atualizarUiDetalhe(estado) }
                }
                launch {
                    viewModel.observarEntradaSalva(tmdbId).collect { entrada ->
                        entrada?.let { preencherComEntradaSalva(it) }
                    }
                }
            }
        }
    }

    private fun atualizarUiDetalhe(estado: Resultado<MovieDetailDto>) {
        binding.progressBarDetalhe.isVisible = estado is Resultado.Carregando
        binding.scrollViewDetalhe.isVisible = estado is Resultado.Sucesso
        binding.textViewErroDetalhe.isVisible = estado is Resultado.Erro

        when (estado) {
            is Resultado.Sucesso -> {
                detalheAtual = estado.dados
                preencherDetalhes(estado.dados)
            }
            is Resultado.Erro -> {
                binding.textViewErroDetalhe.text = estado.mensagem
            }
            else -> Unit
        }
    }

    private fun preencherDetalhes(filme: MovieDetailDto) {
        binding.textViewTituloDetalhe.text = filme.titulo
        val ano = filme.dataLancamento?.take(4).orEmpty()
        val nota = filme.notaMedia?.let { "⭐ %.1f".format(it) }.orEmpty()
        binding.textViewAnoNotaTmdb.text = listOf(ano, nota).filter { it.isNotEmpty() }.joinToString(" • ")
        binding.textViewSinopse.text = filme.sinopse.orEmpty()

        val urlPoster = filme.posterPath?.let { "${BuildConfig.TMDB_IMAGE_BASE_URL}w500$it" }
        Glide.with(binding.imageViewPosterGrande)
            .load(urlPoster)
            .into(binding.imageViewPosterGrande)
    }

    private fun preencherComEntradaSalva(entrada: WatchedEntry) {
        if (binding.campoNotaPessoal.text.isNullOrEmpty()) {
            entrada.notaPessoal?.let { binding.campoNotaPessoal.setText(it.toString()) }
        }
        if (binding.campoComentario.text.isNullOrEmpty()) {
            binding.campoComentario.setText(entrada.comentario.orEmpty())
        }
        when (entrada.status) {
            StatusAssistido.ASSISTIDO -> binding.radioButtonAssistido.isChecked = true
            StatusAssistido.QUERO_ASSISTIR -> binding.radioButtonQueroAssistir.isChecked = true
        }
    }

    private fun salvarNoDiario() {
        val filme = detalheAtual ?: return

        val status = if (binding.radioGroupStatus.checkedRadioButtonId == binding.radioButtonAssistido.id) {
            StatusAssistido.ASSISTIDO
        } else {
            StatusAssistido.QUERO_ASSISTIR
        }
        val notaPessoal = binding.campoNotaPessoal.text?.toString()?.replace(',', '.')?.toFloatOrNull()
        val comentario = binding.campoComentario.text?.toString()?.trim().takeUnless { it.isNullOrEmpty() }

        val entrada = WatchedEntry(
            tmdbId = filme.id,
            titulo = filme.titulo,
            posterUrl = filme.posterPath?.let { "${BuildConfig.TMDB_IMAGE_BASE_URL}w342$it" },
            anoLancamento = filme.dataLancamento?.take(4),
            notaTmdb = filme.notaMedia,
            notaPessoal = notaPessoal,
            comentario = comentario,
            dataAssistido = if (status == StatusAssistido.ASSISTIDO) System.currentTimeMillis() else null,
            status = status
        )

        viewModel.salvarNoDiario(entrada)
        Toast.makeText(requireContext(), R.string.mensagem_salvo_com_sucesso, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
