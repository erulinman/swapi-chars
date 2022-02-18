package info.erulinman.swapichars.presentation.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.erulinman.swapichars.presentation.CharacterUiEntity
import info.erulinman.swapichars.presentation.Characters
import info.erulinman.swapichars.presentation.Characters.Response.Failure
import info.erulinman.swapichars.presentation.Characters.Response.Success
import info.erulinman.swapichars.presentation.Favorites
import info.erulinman.swapichars.presentation.ViewDataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CharactersViewModel(
    private val characters: Characters,
    private val favorites: Favorites
) : ViewModel() {

    private var job: Job? = null

    private var filter: String? = null

    private val _viewDataState =
        MutableLiveData<ViewDataState<List<CharacterUiEntity>>>(ViewDataState.Loaded(listOf()))
    val viewDataState: LiveData<ViewDataState<List<CharacterUiEntity>>> = _viewDataState

    fun setFilter(filter: String?) {
        this.filter = filter
        fetchCharacters()
    }

    fun fetchCharacters() {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            _viewDataState.postValue(ViewDataState.Loading)

            val response = characters.get(filter)

            when (response) {
                is Success -> _viewDataState.postValue(
                    if (response.data.isEmpty())
                        ViewDataState.Empty
                    else
                        ViewDataState.Loaded(response.data)
                )
                is Failure -> _viewDataState.postValue(
                    ViewDataState.Error(response.message)
                )
            }
        }
    }

    fun updateFavorites(characterUiEntity: CharacterUiEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { favorites.update(characterUiEntity) }
            if (result.await()) fetchCharacters()
        }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}

