package info.erulinman.swapichars.presentation.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import dagger.Lazy
import info.erulinman.swapichars.R
import info.erulinman.swapichars.presentation.ViewDataState.*
import info.erulinman.swapichars.presentation.ViewModelFactory
import info.erulinman.swapichars.core.BaseFragment
import info.erulinman.swapichars.data.RemoteDataSource
import info.erulinman.swapichars.databinding.FragmentSearchBinding
import info.erulinman.swapichars.presentation.details.DetailsFragment
import info.erulinman.swapichars.core.di.AppComponent
import info.erulinman.swapichars.presentation.CharacterItemDecoration
import javax.inject.Inject

class SearchFragment :
    BaseFragment<FragmentSearchBinding, SearchView>(R.menu.m_search, R.id.tb_search_view) {

    @Inject
    lateinit var viewModelFactory: Lazy<ViewModelFactory<RemoteDataSource>>

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory.get()).get(SearchViewModel::class.java)
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSearchBinding.inflate(inflater, container, false)

    override fun initInject(daggerComponent: AppComponent) {
        daggerComponent.inject(this)
    }

    override fun prepareToolbarActionView(view: SearchView) {
        view.isIconifiedByDefault = true
        view.queryHint = getString(R.string.sv_hint)
        view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val name = query ?: ""
                viewModel.fetchCharacters(name)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        view.isIconified = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setTitle(R.string.app_name)
        val adapter = SearchAdapter(viewModel) { character ->
            navigator.navigate(DetailsFragment.newInstance(character), true)
        }
        binding.characters.addItemDecoration(CharacterItemDecoration(R.dimen.recycler_view_gaps))
        binding.characters.adapter = adapter
        observeViewModel(adapter)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeViewModel(adapter: SearchAdapter) = viewModel.apply {
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
        favorites.observe(viewLifecycleOwner) { favorites ->
            if (favorites == null) return@observe
            adapter.favorites = favorites
            adapter.notifyDataSetChanged()
        }
    }
}