package info.erulinman.swapichars.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import info.erulinman.swapichars.presentation.Character
import info.erulinman.swapichars.presentation.Favorites
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsViewModel(
    private val favorites: Favorites
) : ViewModel() {

    private var _character: Character? = null
    val character get() = _character!!

    fun setCharacter(character: Character) {
        _character = character
    }

    fun updateFavorites() = viewModelScope.launch(Dispatchers.IO) {
        favorites.update(character)
    }

    fun checkInFavorites(name: String) = favorites.checkByName(name)

    class Factory @Inject constructor(
        private val favorites: Favorites
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val viewModel = when (modelClass) {
                DetailsViewModel::class.java -> DetailsViewModel(favorites)
                else -> error("Wrong ViewModel type")
            }

            return viewModel as T
        }
    }
}