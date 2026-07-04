package com.pedro.cinelog.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pedro.cinelog.BuildConfig
import com.pedro.cinelog.data.remote.dto.MovieResultDto
import com.pedro.cinelog.databinding.ItemMovieBinding

class SearchAdapter(
    private val aoClicarFilme: (MovieResultDto) -> Unit
) : RecyclerView.Adapter<SearchAdapter.MovieViewHolder>() {

    private var filmes: List<MovieResultDto> = emptyList()

    fun atualizarLista(novaLista: List<MovieResultDto>) {
        filmes = novaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(filmes[position])
    }

    override fun getItemCount(): Int = filmes.size

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(filme: MovieResultDto) {
            binding.textViewTitulo.text = filme.titulo
            binding.textViewAno.text = filme.dataLancamento?.take(4).orEmpty()

            val urlPoster = filme.posterPath?.let { "${BuildConfig.TMDB_IMAGE_BASE_URL}w342$it" }
            Glide.with(binding.imageViewPoster)
                .load(urlPoster)
                .into(binding.imageViewPoster)

            binding.root.setOnClickListener { aoClicarFilme(filme) }
        }
    }
}
