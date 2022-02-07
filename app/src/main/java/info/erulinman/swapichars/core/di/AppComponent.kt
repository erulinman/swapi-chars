package info.erulinman.swapichars.core.di

import dagger.Component
import info.erulinman.swapichars.presentation.details.DetailsFragment
import info.erulinman.swapichars.presentation.favorites.FavoritesFragment
import info.erulinman.swapichars.presentation.search.SearchFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(fragment: SearchFragment)
    fun inject(fragment: FavoritesFragment)
    fun inject(fragment: DetailsFragment)
}