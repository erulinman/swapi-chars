package info.erulinman.swapichars.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import info.erulinman.swapichars.presentation.details.DetailsViewModel
import info.erulinman.swapichars.presentation.favorites.FavoritesViewModel
import info.erulinman.swapichars.presentation.search.SearchViewModel
import javax.inject.Inject

class ViewModelFactory<DS : DataSource> @Inject constructor(
    private val dataSource: DS,
    private val exceptionHandler: ExceptionHandler
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            SearchViewModel::class.java -> SearchViewModel(dataSource, exceptionHandler)
            FavoritesViewModel::class.java -> FavoritesViewModel(dataSource, exceptionHandler)
            DetailsViewModel::class.java -> DetailsViewModel(dataSource)
            else -> error("Wrong ViewModel type")
        }

        return viewModel as T
    }
}