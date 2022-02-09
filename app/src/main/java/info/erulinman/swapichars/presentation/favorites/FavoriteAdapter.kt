package info.erulinman.swapichars.presentation.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import info.erulinman.swapichars.databinding.RvCharacterBinding
import info.erulinman.swapichars.presentation.Character
import info.erulinman.swapichars.presentation.CharacterDiffUtil

class FavoriteAdapter(
    private val setViewModelObserver: (String, ImageButton) -> Unit,
    private val onFavoriteButtonClick: (Character) -> Unit,
    private val onItemClick: (Character) -> Unit
) : ListAdapter<Character, FavoriteAdapter.FavoriteViewHolder>(CharacterDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = RvCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(
            currentList[position],
            setViewModelObserver,
            onFavoriteButtonClick,
            onItemClick
        )
    }

    class FavoriteViewHolder(
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