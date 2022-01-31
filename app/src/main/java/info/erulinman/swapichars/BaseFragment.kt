package info.erulinman.swapichars

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding>(contentLayoutId: Int) : Fragment(contentLayoutId) {

    protected var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}