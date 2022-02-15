package info.erulinman.swapichars.core

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import info.erulinman.swapichars.App
import info.erulinman.swapichars.core.di.AppComponent
import info.erulinman.swapichars.presentation.Toolbar

abstract class BaseFragment<VB : ViewBinding, AV : View>(
    private val menuId: Int,
    private val actionViewResId: Int
) : Fragment() {

    private var daggerComponent: AppComponent? = null

    private var _binding: VB? = null
    protected val binding get() = checkNotNull(_binding)

    private var _toolbar: Toolbar? = null
    protected val toolbar get() = checkNotNull(_toolbar)

    private var _navigator: Navigator? = null
    protected val navigator get() = checkNotNull(_navigator)

    protected abstract fun initBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected abstract fun initInject(daggerComponent: AppComponent)

    protected abstract fun prepareToolbarActionView(view: AV)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _toolbar = context as Toolbar
        _navigator = context as Navigator
        daggerComponent = (context.applicationContext as App).appComponent
        initInject(checkNotNull(daggerComponent))
    }

    override fun onDetach() {
        super.onDetach()
        daggerComponent = null
        _toolbar = null
        _navigator = null
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(menuId, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val view = menu.findItem(actionViewResId).actionView as? AV
        prepareToolbarActionView(checkNotNull(view))
    }
}