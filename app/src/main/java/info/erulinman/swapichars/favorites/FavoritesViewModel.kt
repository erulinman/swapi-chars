package info.erulinman.swapichars.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.erulinman.swapichars.ViewDataState
import info.erulinman.swapichars.data.DataSource
import info.erulinman.swapichars.data.entity.Character
import info.erulinman.swapichars.utils.ExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoritesViewModel<DS : DataSource>(
    private val dataSource: DS,
    private val exceptionHandler: ExceptionHandler
) : ViewModel() {

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
                    val value = ViewDataState.Error(exceptionHandler(response.exception))
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

