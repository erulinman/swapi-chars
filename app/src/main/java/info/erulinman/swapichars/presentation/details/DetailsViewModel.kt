package info.erulinman.swapichars.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import info.erulinman.swapichars.presentation.CharacterUiEntity
import info.erulinman.swapichars.presentation.Favorites
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsViewModel(
    private val favorites: Favorites
) : ViewModel() {

    private var _characterUiEntity: CharacterUiEntity? = null
    val character get() = checkNotNull(_characterUiEntity)

    fun setCharacter(characterUiEntity: CharacterUiEntity) {
        _characterUiEntity = characterUiEntity
    }

    fun updateFavorites() = viewModelScope.launch(Dispatchers.IO) {
        favorites.update(character)
    }

    fun checkInFavorites(name: String) = favorites.checkByName(name)

    class Factory @Inject constructor(
        private val favorites: Favorites
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val viewModel = when (modelClass) {
                DetailsViewModel::class.java -> DetailsViewModel(favorites)
                else -> error("Invalid ViewModel type: $modelClass")
            }

            return viewModel as T
        }
    }
}