package info.erulinman.swapichars.details

import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.MaterialToolbar
import dagger.Lazy
import info.erulinman.swapichars.BaseFragment
import info.erulinman.swapichars.R
import info.erulinman.swapichars.ViewModelFactory
import info.erulinman.swapichars.data.LocalDataSource
import info.erulinman.swapichars.data.entity.Character
import info.erulinman.swapichars.databinding.FragmentDetailsBinding
import info.erulinman.swapichars.di.AppComponent
import javax.inject.Inject

class DetailsFragment : BaseFragment<FragmentDetailsBinding>() {

    @Inject
    lateinit var viewModelFactory: Lazy<ViewModelFactory<LocalDataSource>>

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory.get()).get(DetailsViewModel::class.java)
    }

    private var _btnCheckFav: ImageButton? = null
    private val btnCheckFav get() = _btnCheckFav!!

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDetailsBinding.inflate(inflater, container, false)

    override fun initInject(daggerComponent: AppComponent) {
        daggerComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().getParcelable<Character>(ARG_CHARACTER)?.let {
            viewModel.setCharacter(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setTitle(viewModel.character.name)
        val character = viewModel.character
        binding.apply {
            birthYear.text = character.birth_year
            eyeColor.text = character.eye_color
            gender.text = character.gender
            hairColor.text = character.hair_color
            height.text = character.height
            mass.text = character.mass
            skinColor.text = character.skin_color
        }
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