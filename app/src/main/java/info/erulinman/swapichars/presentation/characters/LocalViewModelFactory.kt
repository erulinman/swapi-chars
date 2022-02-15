package info.erulinman.swapichars.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import info.erulinman.swapichars.core.di.Local
import info.erulinman.swapichars.presentation.Characters
import info.erulinman.swapichars.presentation.Favorites
import javax.inject.Inject

class LocalViewModelFactory @Inject constructor(
    @Local private val characters: Characters,
    private val favorites: Favorites
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = if (modelClass == CharactersViewModel::class.java)
            CharactersViewModel(characters, favorites)
        else
            error("Invalid ViewModel type: $modelClass")

        return viewModel as T
    }
}