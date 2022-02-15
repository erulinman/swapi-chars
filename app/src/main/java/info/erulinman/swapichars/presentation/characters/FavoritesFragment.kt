package info.erulinman.swapichars.presentation.characters

import dagger.Lazy
import info.erulinman.swapichars.core.di.AppComponent
import javax.inject.Inject

class FavoritesFragment : CharactersFragment<LocalViewModelFactory>() {

    @Inject
    override lateinit var viewModelFactory: Lazy<LocalViewModelFactory>

    override fun initInject(daggerComponent: AppComponent) {
        daggerComponent.inject(this)
    }

}