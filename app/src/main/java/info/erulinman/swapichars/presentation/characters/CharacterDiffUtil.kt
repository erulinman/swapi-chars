package info.erulinman.swapichars.presentation.characters

import androidx.recyclerview.widget.DiffUtil
import info.erulinman.swapichars.presentation.Character

object CharacterDiffUtil : DiffUtil.ItemCallback<Character>() {

    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem == newItem
    }
}