package info.erulinman.swapichars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import info.erulinman.swapichars.data.DataSource
import info.erulinman.swapichars.details.DetailsViewModel
import info.erulinman.swapichars.search.FavoritesViewModel
import info.erulinman.swapichars.search.SearchViewModel
import javax.inject.Inject

class ViewModelFactory<DS : DataSource> @Inject constructor(private val dataSource: DS) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            SearchViewModel::class.java -> SearchViewModel(dataSource)
            FavoritesViewModel::class.java -> FavoritesViewModel(dataSource)
            DetailsViewModel::class.java -> DetailsViewModel(dataSource)
            else -> error("Wrong ViewModel type")
        }

        return viewModel as T
    }
}