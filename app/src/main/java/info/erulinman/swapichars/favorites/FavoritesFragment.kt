package info.erulinman.swapichars.favorites

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.MaterialToolbar
import dagger.Lazy
import info.erulinman.swapichars.*
import info.erulinman.swapichars.data.LocalDataSource
import info.erulinman.swapichars.data.entity.Character
import info.erulinman.swapichars.databinding.FragmentFavoritesBinding
import info.erulinman.swapichars.details.DetailsFragment
import info.erulinman.swapichars.search.FavoritesViewModel
import info.erulinman.swapichars.utils.CharacterItemDecoration
import javax.inject.Inject

class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>(R.layout.fragment_favorites) {

    @Inject
    lateinit var viewModelFactory: Lazy<ViewModelFactory<LocalDataSource>>

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory.get()).get(FavoritesViewModel::class.java)
    }

    private var _adapterSearch: FavoriteAdapter? = null
    private val adapter get() = _adapterSearch!!

    private var _searchView: SearchView? = null
    private val searchView get() = _searchView!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentFavoritesBinding.bind(view)
        _adapterSearch = FavoriteAdapter(viewModel) { character ->
            openDetailsFragment(character)
        }
        binding.characters.addItemDecoration(CharacterItemDecoration(R.dimen.recycler_view_gaps))
        binding.characters.adapter = adapter
        viewModel.fetchCharacters()
        observeViewModel()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _adapterSearch = null
    }

    private fun openDetailsFragment(character: Character) {
        val fragment = DetailsFragment.newInstance(character)
        parentFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.fragmentContainerMain, fragment)
        }
    }

    private fun observeViewModel() = viewModel.apply {
        viewDataState.observe(viewLifecycleOwner) { viewDataState ->
            if (viewDataState == null) return@observe
            when (viewDataState) {
                is ViewDataState.Error -> {
                    binding.characters.isVisible = false
                    binding.progressBar.isVisible = false
                    binding.message.isVisible = true
                    binding.message.text = viewDataState.msg
                }
                is ViewDataState.Loaded -> {
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
                is ViewDataState.Loading -> {
                    binding.characters.isVisible = true
                    binding.progressBar.isVisible = true
                    binding.message.isVisible = false
                }
            }
        }
    }
}