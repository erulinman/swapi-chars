package info.erulinman.swapichars.presentation.details

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import dagger.Lazy
import info.erulinman.swapichars.R
import info.erulinman.swapichars.core.BaseFragment
import info.erulinman.swapichars.core.di.AppComponent
import info.erulinman.swapichars.databinding.FragmentDetailsBinding
import info.erulinman.swapichars.presentation.CharacterUiEntity
import javax.inject.Inject

class DetailsFragment
    : BaseFragment<FragmentDetailsBinding, ImageButton>(R.menu.m_favorite, R.id.tb_btn_favorite) {

    @Inject
    lateinit var viewModelFactory: Lazy<DetailsViewModel.Factory>

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory.get()).get(DetailsViewModel::class.java)
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDetailsBinding.inflate(inflater, container, false)

    override fun initInject(daggerComponent: AppComponent) {
        daggerComponent.inject(this)
    }

    override fun prepareToolbarActionView(view: ImageButton) {
        val theme = context?.theme
        val backgroundColor = TypedValue().apply {
            theme?.resolveAttribute(R.attr.colorPrimary, this, true)
        }
        val imageColor = TypedValue().apply {
            theme?.resolveAttribute(R.attr.colorOnPrimary, this, true)
        }
        view.backgroundTintList = resources.getColorStateList(backgroundColor.resourceId, theme)
        view.imageTintList = resources.getColorStateList(imageColor.resourceId, theme)
        view.setOnClickListener { viewModel.updateFavorites() }
        observeViewModel(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().getParcelable<CharacterUiEntity>(ARG_CHARACTER)?.let {
            viewModel.setCharacter(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setTitle(viewModel.character.name)
        val character = viewModel.character
        binding.apply {
            birthYear.text = character.birthYear
            eyeColor.text = character.eyeColor
            gender.text = character.gender
            hairColor.text = character.hairColor
            height.text = character.height
            mass.text = character.mass
            skinColor.text = character.skinColor
        }
    }

    private fun observeViewModel(btnFav: ImageButton) = viewModel.apply {
        checkInFavorites(character.name).observe(viewLifecycleOwner) { inFavorites ->
            btnFav.setImageResource(
                if (inFavorites)
                    R.drawable.ic_star_fill
                else
                    R.drawable.ic_star_empty
            )
        }
    }

    companion object {

        private const val ARG_CHARACTER = "DetailsFragment.ARG_CHARACTER"

        fun newInstance(CharacterUiEntity: CharacterUiEntity) = DetailsFragment().apply {
            arguments = bundleOf(ARG_CHARACTER to CharacterUiEntity)
        }
    }
}