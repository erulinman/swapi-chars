package info.erulinman.swapichars.data

import info.erulinman.swapichars.data.database.CharactersDao
import info.erulinman.swapichars.data.network.ApiService
import info.erulinman.swapichars.data.network.CharacterApiEntity
import info.erulinman.swapichars.presentation.Characters
import info.erulinman.swapichars.presentation.Characters.Response.Failure
import info.erulinman.swapichars.presentation.Characters.Response.Success
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteCharactersRepository @Inject constructor(
    private val api: ApiService,
    private val charactersDao: CharactersDao,
    private val exceptionHandler: ExceptionHandler
) : Characters {

    override suspend fun get(filter: String?) = try {
        var page = 1
        var response = if (filter.isNullOrEmpty())
            api.getCharacters()
        else
            api.getCharacters(filter)
        val data = mutableListOf<CharacterApiEntity>()
        data.addAll(response.results)
        while (response.next != null) {
            page++
            response = api.getCharacters(page)
            data.addAll(response.results)
        }
        Success(data.map { characterApiEntity ->
            val inFavorites = charactersDao.getByName(characterApiEntity.name) != null
            characterApiEntity.toCharacterUiEntity(inFavorites)
        })
    } catch (e: Exception) {
        Failure(exceptionHandler(e))
    }
}