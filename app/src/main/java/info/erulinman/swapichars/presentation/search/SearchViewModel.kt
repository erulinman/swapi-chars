package info.erulinman.swapichars.presentation.search

import androidx.lifecycle.*
import info.erulinman.swapichars.core.di.Remote
import info.erulinman.swapichars.presentation.Character
import info.erulinman.swapichars.presentation.Characters
import info.erulinman.swapichars.presentation.Favorites
import info.erulinman.swapichars.presentation.ViewDataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel(
    private val characters: Characters,
    private val favorites: Favorites
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
                characters.getAll()
            else
                characters.getByFilter(name)

            when (response) {
                is Characters.Response.Success -> {
                    val value = if (response.data.isEmpty())
                        ViewDataState.Empty
                    else
                        ViewDataState.Loaded(response.data)
                    _viewDataState.postValue(value)
                }
                is Characters.Response.Failure -> {
                    val value = ViewDataState.Error(response.message)
                    _viewDataState.postValue(value)
                }
            }
        }
    }

    fun updateFavorites(character: Character) = viewModelScope.launch(Dispatchers.IO) {
        favorites.update(character)
    }

    fun checkInFavorites(name: String) = favorites.checkByName(name)

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    class Factory @Inject constructor(
        @Remote private val characters: Characters,
        private val favorites: Favorites
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val viewModel = when (modelClass) {
                SearchViewModel::class.java -> SearchViewModel(characters, favorites)
                else -> error("Wrong ViewModel type")
            }

            return viewModel as T
        }
    }
}

