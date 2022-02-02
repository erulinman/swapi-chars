package info.erulinman.swapichars

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import info.erulinman.swapichars.di.AppComponent

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var daggerComponent: AppComponent? = null

    private var _binding: VB? = null
    protected val binding get() = checkNotNull(_binding)

    abstract fun initBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    abstract fun initInject(daggerComponent: AppComponent)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        daggerComponent = (context.applicationContext as App).appComponent
        initInject(checkNotNull(daggerComponent))
    }

    override fun onDetach() {
        super.onDetach()
        daggerComponent = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = initBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}