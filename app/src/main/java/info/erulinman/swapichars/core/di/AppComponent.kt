package info.erulinman.swapichars.core.di

import dagger.Component
import info.erulinman.swapichars.presentation.characters.FavoritesFragment
import info.erulinman.swapichars.presentation.characters.SearchFragment
import info.erulinman.swapichars.presentation.details.DetailsFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(fragment: DetailsFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: FavoritesFragment)
}