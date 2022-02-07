package info.erulinman.swapichars.core

import androidx.fragment.app.Fragment

interface Navigator {
    fun <T: Fragment> navigate(fragment: T, addToBackStack: Boolean)
}
