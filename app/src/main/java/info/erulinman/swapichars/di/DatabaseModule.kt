package info.erulinman.swapichars.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import info.erulinman.swapichars.data.database.CharactersDatabase
import javax.inject.Singleton

@Module
class DatabaseModule(private val context: Context) {

    @Provides
    fun provideContext() = context

    @Singleton
    @Provides
    fun provideDatabase(context: Context) = Room.databaseBuilder(
        context,
        CharactersDatabase::class.java,
        "Characters.db"
    ).build()

    @Singleton
    @Provides
    fun provideCharactersDao(database: CharactersDatabase) = database.charactersDao()
}