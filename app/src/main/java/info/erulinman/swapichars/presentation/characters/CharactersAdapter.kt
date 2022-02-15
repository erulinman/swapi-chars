package info.erulinman.swapichars.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import info.erulinman.swapichars.databinding.RvCharacterBinding
import info.erulinman.swapichars.presentation.Character

class CharactersAdapter(
    private val setObserver: (String, ImageButton) -> Unit,
    private val onFavoriteButtonClick: (Character) -> Unit,
    private val onItemClick: (Character) -> Unit
) : ListAdapter<Character, CharactersAdapter.CharacterViewHolder>(CharacterDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = RvCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(
            currentList[position],
            setObserver,
            onFavoriteButtonClick,
            onItemClick
        )
    }

    class CharacterViewHolder(
        private val binding: RvCharacterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            character: Character,
            setObserver: (String, ImageButton) -> Unit,
            onFavoriteButtonClick: (Character) -> Unit,
            onItemClick: (Character) -> Unit
        ) {
            setObserver(character.name, binding.btnCheckFav)
            itemView.setOnClickListener { onItemClick(character) }
            binding.name.text = character.name
            binding.btnCheckFav.setOnClickListener { onFavoriteButtonClick(character) }
        }
    }
}