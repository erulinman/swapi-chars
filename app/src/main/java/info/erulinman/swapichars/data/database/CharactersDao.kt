package info.erulinman.swapichars.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CharactersDao {

    @Query("SELECT * FROM characters;")
    fun getFavorites(): LiveData<List<CharacterDbEntity>>

    @Query("SELECT * FROM characters ORDER BY name;")
    suspend fun getCharacters(): List<CharacterDbEntity>

    @Query("SELECT * FROM characters WHERE name like :name ORDER BY name;")
    suspend fun getCharacters(name: String): List<CharacterDbEntity>

    @Query("SELECT * FROM characters WHERE name = :name;")
    suspend fun getByName(name: String): CharacterDbEntity?

    @Insert
    suspend fun insert(characterDbEntity: CharacterDbEntity)

    @Delete
    suspend fun delete(characterDbEntity: CharacterDbEntity)
}