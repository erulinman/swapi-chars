package info.erulinman.swapichars.utils

import androidx.recyclerview.widget.DiffUtil
import info.erulinman.swapichars.data.entity.Character

object CharacterDiffUtil : DiffUtil.ItemCallback<Character>() {

    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem == newItem
    }
}