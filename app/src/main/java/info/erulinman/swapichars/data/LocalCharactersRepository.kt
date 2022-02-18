package info.erulinman.swapichars.data

import info.erulinman.swapichars.data.database.CharactersDao
import info.erulinman.swapichars.presentation.Characters
import info.erulinman.swapichars.presentation.Characters.Response.Failure
import info.erulinman.swapichars.presentation.Characters.Response.Success
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalCharactersRepository @Inject constructor(
    private val charactersDao: CharactersDao,
    private val exceptionHandler: ExceptionHandler
) : Characters {

    override suspend fun get(filter: String?) = try {
        val data = if (filter.isNullOrEmpty())
            charactersDao.getCharacters()
        else
            charactersDao.getCharacters("%$filter%")
        val mappedData = data.map { characterDbEntity ->
            characterDbEntity.toCharacterUiEntity()
        }
        Success(mappedData)
    } catch (e: Exception) {
        Failure(exceptionHandler(e))
    }
}