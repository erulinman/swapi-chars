package info.erulinman.swapichars.presentation.characters

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
import info.erulinman.swapichars.databinding.FragmentCharactersBinding
import info.erulinman.swapichars.presentation.ViewDataState.*
import info.erulinman.swapichars.presentation.details.DetailsFragment

abstract class CharactersFragment<T : ViewModelProvider.Factory>
    : BaseFragment<FragmentCharactersBinding, SearchView>(R.menu.m_search, R.id.tb_search_view) {

    abstract var viewModelFactory: Lazy<T>

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory.get()).get(CharactersViewModel::class.java)
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCharactersBinding.inflate(inflater, container, false)

    override fun prepareToolbarActionView(view: SearchView) {
        view.isIconifiedByDefault = true
        view.queryHint = getString(R.string.sv_hint)
        view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setFilter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setFilter(newText)
                return false
            }
        })
        view.isIconified = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setTitle(R.string.app_name)
        val adapter = CharactersAdapter(
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
        viewModel.fetchCharacters()
    }

    private fun observeViewModel(adapter: CharactersAdapter) = viewModel.apply {
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