package info.erulinman.swapichars.di

import dagger.Component
import info.erulinman.swapichars.details.DetailsFragment
import info.erulinman.swapichars.favorites.FavoritesFragment
import info.erulinman.swapichars.search.SearchFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, NetworkModule::class])
interface AppComponent {

    fun inject(fragment: SearchFragment)
    fun inject(fragment: FavoritesFragment)
    fun inject(fragment: DetailsFragment)
}