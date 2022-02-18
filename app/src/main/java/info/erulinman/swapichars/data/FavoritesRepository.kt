package info.erulinman.swapichars.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import info.erulinman.swapichars.data.database.CharacterDbEntity
import info.erulinman.swapichars.data.database.CharactersDao
import info.erulinman.swapichars.presentation.CharacterUiEntity
import info.erulinman.swapichars.presentation.Favorites
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val charactersDao: CharactersDao
) : Favorites {

    override fun checkByName(name: String): LiveData<Boolean> =
        Transformations.map(charactersDao.checkByName(name)) {
            it != null
        }

    override suspend fun update(character: CharacterUiEntity): Boolean {
        val characterDbEntity = CharacterDbEntity(
            character.name,
            character.birthYear,
            character.eyeColor,
            character.gender,
            character.hairColor,
            character.height,
            character.mass,
            character.skinColor
        )
        val item = charactersDao.getByName(character.name)
        if (item != null) {
            charactersDao.delete(characterDbEntity)
        } else {
            charactersDao.insert(characterDbEntity)
        }

        return true
    }

}