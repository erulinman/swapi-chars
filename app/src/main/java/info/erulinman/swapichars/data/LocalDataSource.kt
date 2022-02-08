package info.erulinman.swapichars.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import info.erulinman.swapichars.data.database.CharacterDbEntity
import info.erulinman.swapichars.data.database.CharactersDao
import info.erulinman.swapichars.presentation.Character
import info.erulinman.swapichars.presentation.DataSource
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val charactersDao: CharactersDao,
    private val exceptionHandler: ExceptionHandler
) : DataSource {

    override suspend fun getCharacters() = try {
        val data = charactersDao.getCharacters().map { dbEntity ->
            dbEntity.toCharacter()
        }
        DataSource.Response.Success(data)
    } catch (e: Exception) {
        DataSource.Response.Failure(exceptionHandler(e))
    }

    override suspend fun getCharacters(name: String) = try {
        val data = charactersDao.getCharacters("%$name%").map { dbEntity ->
            dbEntity.toCharacter()
        }
        DataSource.Response.Success(data)
    } catch (e: Exception) {
        DataSource.Response.Failure(exceptionHandler(e))
    }

    override fun getFavorites(): LiveData<List<Character>> {
        return Transformations.map(charactersDao.getFavorites()) { list ->
            list.map { it.toCharacter() }
        }
    }

    override suspend fun updateFavorites(character: Character): Boolean {
        val characterEntity = CharacterDbEntity(
            character.name,
            character.birthYear,
            character.eyeColor,
            character.gender,
            character.hairColor,
            character.height,
            character.mass,
            character.skinColor
        )
        val item = charactersDao.getByName(characterEntity.name)
        if (item != null)
            charactersDao.delete(characterEntity)
        else
            charactersDao.insert(characterEntity)
        return true
    }
}