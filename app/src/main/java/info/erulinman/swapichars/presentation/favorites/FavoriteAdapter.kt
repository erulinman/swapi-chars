package info.erulinman.swapichars.presentation.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import info.erulinman.swapichars.R
import info.erulinman.swapichars.databinding.RvCharacterBinding
import info.erulinman.swapichars.presentation.Character
import info.erulinman.swapichars.presentation.CharacterDiffUtil

class FavoriteAdapter(
    private val viewModel: FavoritesViewModel<*>,
    private val onClick: (Character) -> Unit
) : ListAdapter<Character, FavoriteAdapter.FavoriteViewHolder>(CharacterDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = RvCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteViewHolder(viewModel, binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(currentList[position], onClick)
    }

    class FavoriteViewHolder(
        private val viewModel: FavoritesViewModel<*>,
        private val binding: RvCharacterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character, onClick: (Character) -> Unit) {
            itemView.setOnClickListener { onClick(character) }
            binding.name.text = character.name
            binding.btnCheckFav.setImageResource(R.drawable.ic_star_fill)
            binding.btnCheckFav.setOnClickListener {
                viewModel.updateFavorites(character)
            }
        }
    }
}