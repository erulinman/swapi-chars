package info.erulinman.swapichars.presentation

import androidx.lifecycle.LiveData

interface DataSource {

    suspend fun getCharacters(): Response<List<Character>>

    suspend fun getCharacters(name: String): Response<List<Character>>

    fun getFavorites(): LiveData<List<Character>>

    suspend fun updateFavorites(character: Character): Boolean

    sealed class Response<out T> {
        class Success<out T>(val data: T) : Response<T>()
        class Failure(val exception: Exception) : Response<Nothing>()
    }
}