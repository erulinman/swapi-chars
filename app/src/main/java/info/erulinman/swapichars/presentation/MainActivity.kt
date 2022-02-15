package info.erulinman.swapichars.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import info.erulinman.swapichars.R
import info.erulinman.swapichars.core.Navigator
import info.erulinman.swapichars.databinding.ActivityMainBinding
import info.erulinman.swapichars.presentation.characters.FavoritesFragment
import info.erulinman.swapichars.presentation.characters.SearchFragment

class MainActivity : AppCompatActivity(), Toolbar, Navigator {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        if (savedInstanceState == null) navigate(SearchFragment(), false)
    }

    override fun onStart() {
        super.onStart()
        binding.botNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bnv_search -> navigate(SearchFragment(), false)
                R.id.bnv_favorites -> navigate(FavoritesFragment(), false)
                else -> error("Something wrong with MenuItem changes")
            }
            true
        }
    }

    override fun onStop() {
        super.onStop()
        binding.botNavView.setOnItemSelectedListener(null)
    }

    override fun navigate(fragment: Fragment, addToBackStack: Boolean) {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack()

        supportFragmentManager.commit {
            if (addToBackStack) addToBackStack(null)
            replace(R.id.fragmentContainerMain, fragment)
        }
    }

    override fun setTitle(title: String) {
        binding.toolbar.title = title
    }

    override fun setTitle(id: Int) {
        binding.toolbar.setTitle(id)
    }
}