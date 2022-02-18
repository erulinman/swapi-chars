package info.erulinman.swapichars.presentation

interface Characters {

    suspend fun get(filter: String?): Response<List<CharacterUiEntity>>

    sealed class Response<out T> {
        class Success<out T>(val data: T) : Response<T>()
        class Failure(val message: String) : Response<Nothing>()
    }

}