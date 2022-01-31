package info.erulinman.swapichars.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import info.erulinman.swapichars.data.entity.Character

@Database(entities = [Character::class], version = 1)
abstract class CharactersDatabase : RoomDatabase() {

    abstract fun charactersDao(): CharactersDao
}