package com.pedro.cinelog.ui.diary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pedro.cinelog.data.local.StatusAssistido
import com.pedro.cinelog.data.local.WatchedEntry
import com.pedro.cinelog.databinding.ItemDiaryBinding

class DiaryAdapter(
    private val aoClicarItem: (WatchedEntry) -> Unit
) : RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

    private var entradas: List<WatchedEntry> = emptyList()

    fun atualizarLista(novaLista: List<WatchedEntry>) {
        entradas = novaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): DiaryViewHolder {
        val binding = ItemDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        holder.bind(entradas[position])
    }

    override fun getItemCount(): Int = entradas.size

    inner class DiaryViewHolder(private val binding: ItemDiaryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entrada: WatchedEntry) {
            binding.textViewTitulo.text = entrada.titulo
            binding.textViewAno.text = entrada.anoLancamento.orEmpty()
            binding.textViewStatus.text = when (entrada.status) {
                StatusAssistido.ASSISTIDO -> "Assistido"
                StatusAssistido.QUERO_ASSISTIR -> "Quero assistir"
            }
            binding.textViewNotaPessoal.text = entrada.notaPessoal?.let { "Sua nota: %.1f".format(it) }.orEmpty()

            Glide.with(binding.imageViewPoster)
                .load(entrada.posterUrl)
                .into(binding.imageViewPoster)

            binding.root.setOnClickListener { aoClicarItem(entrada) }
        }
    }
}
