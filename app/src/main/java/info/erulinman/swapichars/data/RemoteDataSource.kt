package info.erulinman.swapichars.data

import androidx.lifecycle.LiveData
import info.erulinman.swapichars.data.database.CharactersDao
import info.erulinman.swapichars.data.entity.Character
import info.erulinman.swapichars.data.network.ApiService
import info.erulinman.swapichars.presentation.DataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val api: ApiService,
    private val charactersDao: CharactersDao
) : DataSource {

    override suspend fun getCharacters() = try {
        var page = 1
        var response = api.getCharacters()
        val data = mutableListOf<Character>()
        data.addAll(response.results)
        while (response.next != null) {
            page++
            response = api.getCharacters(page)
            data.addAll(response.results)
        }
        DataSource.Response.Success(data)
    } catch (e: Exception) {
        DataSource.Response.Failure(e)
    }

    override suspend fun getCharacters(name: String) = try {
        var page = 1
        var response = api.getCharacters(name)
        val data = mutableListOf<Character>()
        data.addAll(response.results)
        while (response.next != null) {
            page++
            response = api.getCharacters(page)
            data.addAll(response.results)
        }
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