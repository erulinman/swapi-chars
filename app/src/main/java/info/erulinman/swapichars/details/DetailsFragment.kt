package info.erulinman.swapichars.details

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.MaterialToolbar
import dagger.Lazy
import info.erulinman.swapichars.App
import info.erulinman.swapichars.BaseFragment
import info.erulinman.swapichars.R
import info.erulinman.swapichars.ViewModelFactory
import info.erulinman.swapichars.data.LocalDataSource
import info.erulinman.swapichars.data.entity.Character
import info.erulinman.swapichars.databinding.FragmentDetailsBinding
import javax.inject.Inject

class DetailsFragment : BaseFragment<FragmentDetailsBinding>(R.layout.fragment_details) {

    @Inject
    lateinit var viewModelFactory: Lazy<ViewModelFactory<LocalDataSource>>

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory.get()).get(DetailsViewModel::class.java)
    }

    private var _btnCheckFav: ImageButton? = null
    private val btnCheckFav get() = _btnCheckFav!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        requireArguments().getParcelable<Character>(ARG_CHARACTER)?.let {
            viewModel.setCharacter(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentDetailsBinding.bind(view)
        binding.apply {
            birthYear.text = viewModel.character.birth_year
            eyeColor.text = viewModel.character.eye_color
            gender.text = viewModel.character.gender
            hairColor.text = viewModel.character.hair_color
            height.text = viewModel.character.height
            mass.text = viewModel.character.mass
            skinColor.text = viewModel.character.skin_color
        }
        requireActivity().findViewById<MaterialToolbar>(R.id.toolbar).title =
            viewModel.character.name
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_favorite, menu)

        _btnCheckFav = menu.findItem(R.id.menu_btn_favorite).actionView as ImageButton
        btnCheckFav.setOnClickListener { viewModel.updateFavorites() }

        val theme = context?.theme

        val backgroundColorId = TypedValue()
            .apply { theme?.resolveAttribute(R.attr.colorPrimary, this, true) }
            .resourceId

        val imageColorId = TypedValue()
            .apply { theme?.resolveAttribute(R.attr.colorOnPrimary, this, true) }
            .resourceId

        btnCheckFav.backgroundTintList = resources.getColorStateList(backgroundColorId, theme)
        btnCheckFav.imageTintList = resources.getColorStateList(imageColorId, theme)

        observeViewModel()
    }

    private fun observeViewModel() = viewModel.apply {

        favorites.observe(viewLifecycleOwner) { favorites ->
            val iconResId = if (viewModel.character in favorites)
                R.drawable.ic_star_fill
            else
                R.drawable.ic_star_empty
            btnCheckFav.setImageResource(iconResId)
        }
    }

    companion object {

        private const val ARG_CHARACTER = "DetailsFragment.ARG_CHARACTER"

        fun newInstance(character: Character) = DetailsFragment().apply {
            arguments = bundleOf(ARG_CHARACTER to character)
        }
    }
}