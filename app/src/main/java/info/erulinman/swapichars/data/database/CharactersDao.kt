package info.erulinman.swapichars.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import info.erulinman.swapichars.data.entity.Character

@Dao
interface CharactersDao {

    @Query("SELECT * FROM characters;")
    fun getFavorites(): LiveData<List<Character>>

    @Query("SELECT * FROM characters ORDER BY name;")
    suspend fun getCharacters(): List<Character>

    @Query("SELECT * FROM characters WHERE name like :name ORDER BY name;")
    suspend fun getCharacters(name: String): List<Character>

    @Query("SELECT * FROM characters WHERE name = :name;")
    suspend fun getByName(name: String): Character?

    @Insert
    suspend fun insert(character: Character)

    @Delete
    suspend fun delete(character: Character)
}