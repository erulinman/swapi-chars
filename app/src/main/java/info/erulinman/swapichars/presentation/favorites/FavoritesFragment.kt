package info.erulinman.swapichars.presentation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import dagger.Lazy
import info.erulinman.swapichars.R
import info.erulinman.swapichars.core.BaseFragment
import info.erulinman.swapichars.core.di.AppComponent
import info.erulinman.swapichars.databinding.FragmentFavoritesBinding
import info.erulinman.swapichars.presentation.CharacterItemDecoration
import info.erulinman.swapichars.presentation.ViewDataState.*
import info.erulinman.swapichars.presentation.details.DetailsFragment
import javax.inject.Inject

class FavoritesFragment :
    BaseFragment<FragmentFavoritesBinding, SearchView>(R.menu.m_search, R.id.tb_search_view) {

    @Inject
    lateinit var viewModelFactory: Lazy<FavoritesViewModel.Factory>

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory.get()).get(FavoritesViewModel::class.java)
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFavoritesBinding.inflate(inflater, container, false)

    override fun initInject(daggerComponent: AppComponent) {
        daggerComponent.inject(this)
    }

    override fun prepareToolbarActionView(view: SearchView) {
        view.isIconifiedByDefault = true
        view.queryHint = getString(R.string.sv_hint)
        view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.fetchCharacters(query ?: "")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.fetchCharacters(newText ?: "")
                return false
            }
        })
        view.isIconified = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.fetchCharacters()
        toolbar.setTitle(R.string.app_name)
        val adapter = FavoriteAdapter(
            setViewModelObserver = { name, button ->
                viewModel.checkInFavorites(name).observe(viewLifecycleOwner) { inFavorites ->
                    val iconResId = if (inFavorites)
                        R.drawable.ic_star_fill
                    else
                        R.drawable.ic_star_empty
                    button.setImageResource(iconResId)
                }
            },
            onFavoriteButtonClick = { character ->
                viewModel.updateFavorites(character)
            },
            onItemClick = { character ->
                navigator.navigate(DetailsFragment.newInstance(character), true)
            }
        )
        binding.characters.addItemDecoration(CharacterItemDecoration(R.dimen.recycler_view_gaps))
        binding.characters.adapter = adapter
        observeViewModel(adapter)
    }

    private fun observeViewModel(adapter: FavoriteAdapter) = viewModel.apply {
        viewDataState.observe(viewLifecycleOwner) { viewDataState ->
            if (viewDataState == null) {
                binding.characters.isVisible = false
                binding.progressBar.isVisible = false
                binding.message.isVisible = true
                binding.message.setText(R.string.tv_request_restart)
                return@observe
            }
            when (viewDataState) {
                is Empty -> {
                    binding.characters.isVisible = false
                    binding.progressBar.isVisible = false
                    binding.message.isVisible = true
                    binding.message.setText(R.string.tv_nothing_found)
                }
                is Error -> {
                    binding.characters.isVisible = false
                    binding.progressBar.isVisible = false
                    binding.message.isVisible = true
                    binding.message.text = viewDataState.message
                }
                is Loaded -> {
                    binding.characters.isVisible = true
                    binding.progressBar.isVisible = false
                    binding.message.isVisible = false
                    adapter.submitList(viewDataState.data)
                }
                is Loading -> {
                    binding.characters.isVisible = true
                    binding.progressBar.isVisible = true
                    binding.message.isVisible = false
                }
            }
        }
    }
}