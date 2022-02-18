package info.erulinman.swapichars.presentation

import androidx.lifecycle.LiveData

interface Favorites {

    fun checkByName(name: String): LiveData<Boolean>

    suspend fun update(character: CharacterUiEntity): Boolean

}
