package info.erulinman.swapichars.core.di

import dagger.Binds
import dagger.Module
import info.erulinman.swapichars.data.FavoritesRepository
import info.erulinman.swapichars.data.LocalCharactersRepository
import info.erulinman.swapichars.data.RemoteCharactersRepository
import info.erulinman.swapichars.presentation.Characters
import info.erulinman.swapichars.presentation.Favorites
import javax.inject.Qualifier

@Module(
    includes = [
        NetworkModule::class,
        DatabaseModule::class
    ]
)
interface DataModule {

    @Binds
    fun bindFavoritesRepository_to_Favorites(
        favoritesRepository: FavoritesRepository
    ): Favorites

    @Binds
    @Local
    fun bindLocalCharactersRepository_to_Characters(
        localCharactersRepository: LocalCharactersRepository
    ): Characters

    @Binds
    @Remote
    fun bindRemoteCharactersRepository_to_Characters(
        remoteCharactersRepository: RemoteCharactersRepository
    ): Characters
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Local

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Remote