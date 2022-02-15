package info.erulinman.swapichars.core

import androidx.fragment.app.Fragment

interface Navigator {
    fun navigate(fragment: Fragment, addToBackStack: Boolean)
}
