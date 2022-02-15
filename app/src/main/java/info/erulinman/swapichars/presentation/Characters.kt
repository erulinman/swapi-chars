package info.erulinman.swapichars.presentation

interface Characters {

    suspend fun getAll(): Response<List<Character>>

    suspend fun getByFilter(filter: String): Response<List<Character>>

    sealed class Response<out T> {
        class Success<out T>(val data: T) : Response<T>()
        class Failure(val message: String) : Response<Nothing>()
    }

}