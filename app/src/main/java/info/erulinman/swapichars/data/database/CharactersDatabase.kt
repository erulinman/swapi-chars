package info.erulinman.swapichars.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [CharacterDbEntity::class]
)
abstract class CharactersDatabase : RoomDatabase() {

    abstract fun charactersDao(): CharactersDao
}