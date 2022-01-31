package info.erulinman.swapichars.search

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
import info.erulinman.swapichars.App
import info.erulinman.swapichars.BaseFragment
import info.erulinman.swapichars.R
import info.erulinman.swapichars.ViewDataState.*
import info.erulinman.swapichars.ViewModelFactory
import info.erulinman.swapichars.data.RemoteDataSource
import info.erulinman.swapichars.data.entity.Character
import info.erulinman.swapichars.databinding.FragmentSearchBinding
import info.erulinman.swapichars.details.DetailsFragment
import info.erulinman.swapichars.utils.CharacterItemDecoration
import info.erulinman.swapichars.utils.ErrorHandler
import javax.inject.Inject

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    @Inject
    lateinit var errorHandler: ErrorHandler

    @Inject
    lateinit var viewModelFactory: Lazy<ViewModelFactory<RemoteDataSource>>

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory.get()).get(SearchViewModel::class.java)
    }

    private var _adapterSearch: SearchAdapter? = null
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
        _binding = FragmentSearchBinding.bind(view)
        _adapterSearch = SearchAdapter(viewModel) { character ->
            openDetailsFragment(character)
        }
        binding.characters.addItemDecoration(CharacterItemDecoration(R.dimen.recycler_view_gaps))
        binding.characters.adapter = adapter
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
                val name = query ?: ""
                viewModel.fetchCharacters(name)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
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
        favorites.observe(viewLifecycleOwner) { favorites ->
            if (favorites == null) return@observe
            adapter.favorites = favorites
            adapter.notifyDataSetChanged()
        }
    }
}