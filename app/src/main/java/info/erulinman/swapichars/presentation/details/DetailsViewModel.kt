package info.erulinman.swapichars.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.erulinman.swapichars.presentation.DataSource
import info.erulinman.swapichars.data.entity.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel<DS : DataSource>(private val dataSource: DS) : ViewModel() {

    private var _character: Character? = null
    val character get() = _character!!

    val favorites = dataSource.getFavorites()

    fun setCharacter(character: Character) {
        _character = character
    }

    fun updateFavorites() = viewModelScope.launch(Dispatchers.IO) {
        dataSource.updateFavorites(character)
    }
}