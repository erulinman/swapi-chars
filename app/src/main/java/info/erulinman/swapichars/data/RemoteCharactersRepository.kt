package info.erulinman.swapichars.data

import info.erulinman.swapichars.data.network.ApiService
import info.erulinman.swapichars.data.network.CharacterApiEntity
import info.erulinman.swapichars.presentation.Characters
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteCharactersRepository @Inject constructor(
    private val api: ApiService,
    private val exceptionHandler: ExceptionHandler
) : Characters {

    override suspend fun getAll() = try {
        var page = 1
        var response = api.getCharacters()
        val data = mutableListOf<CharacterApiEntity>()
        data.addAll(response.results)
        while (response.next != null) {
            page++
            response = api.getCharacters(page)
            data.addAll(response.results)
        }
        Characters.Response.Success(data.map { it.toCharacter() })
    } catch (e: Exception) {
        Characters.Response.Failure(exceptionHandler(e))
    }

    override suspend fun getByFilter(filter: String) = try {
        var page = 1
        var response = api.getCharacters(filter)
        val data = mutableListOf<CharacterApiEntity>()
        data.addAll(response.results)
        while (response.next != null) {
            page++
            response = api.getCharacters(page)
            data.addAll(response.results)
        }
        Characters.Response.Success(data.map { it.toCharacter() })
    } catch (e: Exception) {
        Characters.Response.Failure(exceptionHandler(e))
    }
}