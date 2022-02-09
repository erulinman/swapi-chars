package info.erulinman.swapichars.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import info.erulinman.swapichars.databinding.RvCharacterBinding
import info.erulinman.swapichars.presentation.Character
import info.erulinman.swapichars.presentation.CharacterDiffUtil

class SearchAdapter(
    private val setViewModelObserver: (String, ImageButton) -> Unit,
    private val onFavoriteButtonClick: (Character) -> Unit,
    private val onItemClick: (Character) -> Unit
) : ListAdapter<Character, SearchAdapter.SearchViewHolder>(CharacterDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = RvCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(
            currentList[position],
            setViewModelObserver,
            onFavoriteButtonClick,
            onItemClick
        )
    }

    class SearchViewHolder(
        private val binding: RvCharacterBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            character: Character,
            setViewModelObserver: (String, ImageButton) -> Unit,
            onFavoriteButtonClick: (Character) -> Unit,
            onItemClick: (Character) -> Unit
        ) {
            setViewModelObserver(character.name, binding.btnCheckFav)
            itemView.setOnClickListener { onItemClick(character) }
            binding.name.text = character.name
            binding.btnCheckFav.setOnClickListener { onFavoriteButtonClick(character) }
        }
    }
}