package info.erulinman.swapichars.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import info.erulinman.swapichars.data.database.CharacterDbEntity
import info.erulinman.swapichars.data.database.CharactersDao
import info.erulinman.swapichars.presentation.Character
import info.erulinman.swapichars.presentation.Favorites
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val charactersDao: CharactersDao
) : Favorites {

    override fun checkByName(name: String): LiveData<Boolean> =
        Transformations.map(charactersDao.checkByName(name)) {
            it != null
        }

    override suspend fun update(character: Character): Boolean {
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