package info.erulinman.swapichars.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CharactersDao {

    @Query("SELECT * FROM characters WHERE name = :name;")
    fun checkByName(name: String): LiveData<CharacterDbEntity>

    @Query("SELECT * FROM characters WHERE name = :name;")
    suspend fun getByName(name: String): CharacterDbEntity?

    @Query("SELECT * FROM characters ORDER BY name;")
    suspend fun getCharacters(): List<CharacterDbEntity>

    @Query("SELECT * FROM characters WHERE name like :name ORDER BY name;")
    suspend fun getCharacters(name: String): List<CharacterDbEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(characterDbEntity: CharacterDbEntity)

    @Delete
    suspend fun delete(characterDbEntity: CharacterDbEntity)
}