package info.erulinman.swapichars.favorites

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.MaterialToolbar
import dagger.Lazy
import info.erulinman.swapichars.BaseFragment
import info.erulinman.swapichars.R
import info.erulinman.swapichars.ViewDataState.*
import info.erulinman.swapichars.ViewModelFactory
import info.erulinman.swapichars.data.LocalDataSource
import info.erulinman.swapichars.data.entity.Character
import info.erulinman.swapichars.databinding.FragmentFavoritesBinding
import info.erulinman.swapichars.details.DetailsFragment
import info.erulinman.swapichars.di.AppComponent
import info.erulinman.swapichars.utils.CharacterItemDecoration
import info.erulinman.swapichars.utils.ErrorHandler
import javax.inject.Inject

class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>() {

    @Inject
    lateinit var errorHandler: ErrorHandler

    @Inject
    lateinit var viewModelFactory: Lazy<ViewModelFactory<LocalDataSource>>

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory.get()).get(FavoritesViewModel::class.java)
    }

    private var _searchView: SearchView? = null
    private val searchView get() = _searchView!!

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFavoritesBinding.inflate(inflater, container, false)

    override fun initInject(daggerComponent: AppComponent) {
        daggerComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = FavoriteAdapter(viewModel) { character ->
            openDetailsFragment(character)
        }
        binding.characters.addItemDecoration(CharacterItemDecoration(R.dimen.recycler_view_gaps))
        binding.characters.adapter = adapter
        observeViewModel(adapter)
        requireActivity().findViewById<MaterialToolbar>(R.id.toolbar).setTitle(R.string.app_name)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        _searchView = menu.findItem(R.id.tb_search_view).actionView as SearchView
        searchView.isIconifiedByDefault = true
        searchView.queryHint = getString(R.string.sv_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.fetchCharacters(query ?: "")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.fetchCharacters(newText ?: "")
                return false
            }

        })
        searchView.isIconified = false
    }

    private fun openDetailsFragment(character: Character) {
        val fragment = DetailsFragment.newInstance(character)
        parentFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.fragmentContainerMain, fragment)
        }
    }

    private fun observeViewModel(adapter: FavoriteAdapter) = viewModel.apply {
        viewDataState.observe(viewLifecycleOwner) { viewDataState ->
            if (viewDataState == null) return@observe
            when (viewDataState) {
                is Error -> {
                    binding.characters.isVisible = false
                    binding.progressBar.isVisible = false
                    binding.message.isVisible = true
                    binding.message.text = errorHandler(viewDataState.exception)
                }
                is Loaded -> {
                    binding.characters.isVisible = true
                    binding.progressBar.isVisible = false
                    binding.message.isVisible = if (viewDataState.data.isEmpty()) {
                        binding.message.setText(R.string.tv_nothing_found)
                        true
                    } else {
                        false
                    }
                    adapter.submitList(viewDataState.data)

                }
                is Loading -> {
                    binding.characters.isVisible = false
                    binding.progressBar.isVisible = true
                    binding.message.isVisible = false
                }
            }
        }
    }
}