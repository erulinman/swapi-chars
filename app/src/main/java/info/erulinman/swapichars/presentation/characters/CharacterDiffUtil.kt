package info.erulinman.swapichars.presentation.characters

import androidx.recyclerview.widget.DiffUtil
import info.erulinman.swapichars.presentation.CharacterUiEntity

object CharacterDiffUtil : DiffUtil.ItemCallback<CharacterUiEntity>() {

    override fun areItemsTheSame(oldItem: CharacterUiEntity, newItem: CharacterUiEntity) =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: CharacterUiEntity, newItem: CharacterUiEntity) =
        oldItem == newItem
}