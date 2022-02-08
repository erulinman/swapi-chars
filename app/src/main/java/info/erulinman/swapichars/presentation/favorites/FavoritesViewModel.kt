package info.erulinman.swapichars.presentation.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.erulinman.swapichars.presentation.Character
import info.erulinman.swapichars.presentation.DataSource
import info.erulinman.swapichars.presentation.ViewDataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoritesViewModel<DS : DataSource>(private val dataSource: DS) : ViewModel() {

    private var job: Job? = null

    private val _viewDataState =
        MutableLiveData<ViewDataState<List<Character>>>(ViewDataState.Loaded(listOf()))
    val viewDataState: LiveData<ViewDataState<List<Character>>> = _viewDataState

    init {
        fetchCharacters()
    }

    fun fetchCharacters(name: String? = null) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            _viewDataState.postValue(ViewDataState.Loading)

            val response = if (name == null)
                dataSource.getCharacters()
            else
                dataSource.getCharacters(name)

            when (response) {
                is DataSource.Response.Success -> {
                    val value = if (response.data.isEmpty())
                        ViewDataState.Empty
                    else
                        ViewDataState.Loaded(response.data)
                    _viewDataState.postValue(value)
                }
                is DataSource.Response.Failure -> {
                    val value = ViewDataState.Error(response.message)
                    _viewDataState.postValue(value)
                }
            }
        }
    }

    fun updateFavorites(character: Character) = viewModelScope.launch(Dispatchers.IO) {
        val result = async { dataSource.updateFavorites(character) }
        if (result.await()) fetchCharacters()
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}

