package info.erulinman.swapichars.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import info.erulinman.swapichars.R
import info.erulinman.swapichars.databinding.RvCharacterBinding
import info.erulinman.swapichars.presentation.CharacterUiEntity

class CharactersAdapter(
    private val onFavoriteButtonClick: (CharacterUiEntity) -> Unit,
    private val onItemClick: (CharacterUiEntity) -> Unit
) : ListAdapter<CharacterUiEntity, CharactersAdapter.CharacterViewHolder>(CharacterDiffUtil) {

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
            onFavoriteButtonClick,
            onItemClick
        )
    }

    class CharacterViewHolder(
        private val binding: RvCharacterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var inFavorites: Boolean = false
            set(value) {
                val iconResId = if (value)
                    R.drawable.ic_star_fill
                else
                    R.drawable.ic_star_empty
                binding.btnCheckFav.setImageResource(iconResId)
                field = value
            }

        fun bind(
            character: CharacterUiEntity,
            onFavoriteButtonClick: (CharacterUiEntity) -> Unit,
            onItemClick: (CharacterUiEntity) -> Unit
        ) {
            binding.name.text = character.name
            inFavorites = character.favorite
            itemView.setOnClickListener { onItemClick(character) }
            binding.btnCheckFav.setOnClickListener {
                inFavorites = !inFavorites
                onFavoriteButtonClick(character)
            }
        }
    }
}