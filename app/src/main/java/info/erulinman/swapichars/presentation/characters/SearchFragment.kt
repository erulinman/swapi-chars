package info.erulinman.swapichars.presentation.characters

import dagger.Lazy
import info.erulinman.swapichars.core.di.AppComponent
import javax.inject.Inject

class SearchFragment : CharactersFragment<RemoteViewModelFactory>() {

    @Inject
    override lateinit var viewModelFactory: Lazy<RemoteViewModelFactory>

    override fun initInject(daggerComponent: AppComponent) {
        daggerComponent.inject(this)
    }

}