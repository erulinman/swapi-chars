package info.erulinman.swapichars.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import info.erulinman.swapichars.data.entity.Character

@Database(
    version = 1,
    entities = [Character::class]
)
abstract class CharactersDatabase : RoomDatabase() {

    abstract fun charactersDao(): CharactersDao
}