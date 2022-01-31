package info.erulinman.swapichars.data

import androidx.lifecycle.LiveData
import info.erulinman.swapichars.data.database.CharactersDao
import info.erulinman.swapichars.data.entity.Character
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val charactersDao: CharactersDao) :
    DataSource {

    override suspend fun getCharacters() = try {
        val data = charactersDao.getCharacters()
        DataSource.Response.Success(data)
    } catch (e: Exception) {
        DataSource.Response.Failure(e)
    }

    override suspend fun getCharacters(name: String) = try {
        val data = charactersDao.getCharacters("%$name%")
        DataSource.Response.Success(data)
    } catch (e: Exception) {
        DataSource.Response.Failure(e)
    }

    override fun getFavorites(): LiveData<List<Character>> = charactersDao.getFavorites()

    override suspend fun updateFavorites(character: Character): Boolean {
        val item = charactersDao.getByName(character.name)

        if (item != null) charactersDao.delete(character)
        else charactersDao.insert(character)
        return true
    }
}