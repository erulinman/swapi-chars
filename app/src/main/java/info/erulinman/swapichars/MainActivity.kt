package info.erulinman.swapichars

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import info.erulinman.swapichars.databinding.ActivityMainBinding
import info.erulinman.swapichars.favorites.FavoritesFragment
import info.erulinman.swapichars.search.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        if (savedInstanceState == null) loadFragment(SearchFragment())
    }

    override fun onStart() {
        super.onStart()
        binding.botNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bnv_search -> loadFragment(SearchFragment())
                R.id.bnv_favorites -> loadFragment(FavoritesFragment())
                else -> error("Something wrong with MenuItem changes")
            }
            true
        }
    }

    override fun onStop() {
        super.onStop()
        binding.botNavView.setOnItemSelectedListener(null)
    }

    private fun <T : Fragment> loadFragment(fragment: T) {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack()
        supportFragmentManager.commit {
            replace(R.id.fragmentContainerMain, fragment)
        }
    }
}