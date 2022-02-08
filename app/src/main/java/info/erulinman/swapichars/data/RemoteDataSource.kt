package info.erulinman.swapichars.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import info.erulinman.swapichars.data.database.CharacterDbEntity
import info.erulinman.swapichars.data.database.CharactersDao
import info.erulinman.swapichars.data.network.ApiService
import info.erulinman.swapichars.data.network.CharacterApiEntity
import info.erulinman.swapichars.presentation.Character
import info.erulinman.swapichars.presentation.DataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val api: ApiService,
    private val charactersDao: CharactersDao,
    private val exceptionHandler: ExceptionHandler
) : DataSource {

    override suspend fun getCharacters() = try {
        var page = 1
        var response = api.getCharacters()
        val data = mutableListOf<CharacterApiEntity>()
        data.addAll(response.results)
        while (response.next != null) {
            page++
            response = api.getCharacters(page)
            data.addAll(response.results)
        }
        DataSource.Response.Success(data.map { it.toCharacter() })
    } catch (e: Exception) {
        DataSource.Response.Failure(exceptionHandler(e))
    }

    override suspend fun getCharacters(name: String) = try {
        var page = 1
        var response = api.getCharacters(name)
        val data = mutableListOf<CharacterApiEntity>()
        data.addAll(response.results)
        while (response.next != null) {
            page++
            response = api.getCharacters(page)
            data.addAll(response.results)
        }
        DataSource.Response.Success(data.map { it.toCharacter() })
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
        if (item != null) charactersDao.delete(characterEntity)
        else charactersDao.insert(characterEntity)
        return true
    }
}