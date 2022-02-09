package info.erulinman.swapichars.data

import info.erulinman.swapichars.data.database.CharactersDao
import info.erulinman.swapichars.presentation.Characters
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalCharactersRepository @Inject constructor(
    private val charactersDao: CharactersDao,
    private val exceptionHandler: ExceptionHandler
) : Characters {

    override suspend fun getAll() = try {
        val data = charactersDao.getCharacters().map { dbEntity ->
            dbEntity.toCharacter()
        }
        Characters.Response.Success(data)
    } catch (e: Exception) {
        Characters.Response.Failure(exceptionHandler(e))
    }

    override suspend fun getByFilter(filter: String) = try {
        val data = charactersDao.getCharacters(filter).map { dbEntity ->
            dbEntity.toCharacter()
        }
        Characters.Response.Success(data)
    } catch (e: Exception) {
        Characters.Response.Failure(exceptionHandler(e))
    }

}