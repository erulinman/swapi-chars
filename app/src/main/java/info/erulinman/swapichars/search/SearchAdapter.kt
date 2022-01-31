package info.erulinman.swapichars.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import info.erulinman.swapichars.R
import info.erulinman.swapichars.data.entity.Character
import info.erulinman.swapichars.databinding.RvCharacterBinding
import info.erulinman.swapichars.utils.CharacterDiffUtil

class SearchAdapter(
    private val viewModel: SearchViewModel<*>,
    private val onClick: (Character) -> Unit
) : ListAdapter<Character, SearchAdapter.SearchViewHolder>(CharacterDiffUtil) {

    var favorites = listOf<Character>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = RvCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchViewHolder(viewModel, binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(favorites, currentList[position], onClick)
    }

    class SearchViewHolder(
        private val viewModel: SearchViewModel<*>,
        private val binding: RvCharacterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(favorites: List<Character>, character: Character, onClick: (Character) -> Unit) {
            itemView.setOnClickListener { onClick(character) }
            val iconResId = if (character in favorites)
                R.drawable.ic_star_fill
            else
                R.drawable.ic_star_empty
            binding.btnCheckFav.setImageResource(iconResId)
            binding.name.text = character.name
            binding.btnCheckFav.setOnClickListener {
                viewModel.updateFavorites(character)
            }
        }
    }
}