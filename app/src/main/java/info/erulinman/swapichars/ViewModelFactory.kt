package info.erulinman.swapichars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import info.erulinman.swapichars.data.DataSource
import info.erulinman.swapichars.details.DetailsViewModel
import info.erulinman.swapichars.favorites.FavoritesViewModel
import info.erulinman.swapichars.search.SearchViewModel
import info.erulinman.swapichars.utils.ErrorHandler
import javax.inject.Inject

class ViewModelFactory<DS : DataSource> @Inject constructor(
    private val dataSource: DS,
    private val errorHandler: ErrorHandler
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            SearchViewModel::class.java -> SearchViewModel(dataSource, errorHandler)
            FavoritesViewModel::class.java -> FavoritesViewModel(dataSource, errorHandler)
            DetailsViewModel::class.java -> DetailsViewModel(dataSource)
            else -> error("Wrong ViewModel type")
        }

        return viewModel as T
    }
}