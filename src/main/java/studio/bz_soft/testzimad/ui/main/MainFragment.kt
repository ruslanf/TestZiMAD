package studio.bz_soft.testzimad.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.view.*
import org.koin.android.ext.android.inject
import studio.bz_soft.testzimad.R
import studio.bz_soft.testzimad.root.BackPressedInterface
import studio.bz_soft.testzimad.root.Screens
import studio.bz_soft.testzimad.ui.cats.CatsFragment
import studio.bz_soft.testzimad.ui.dogs.DogsFragment

class MainFragment : Fragment(), BackPressedInterface {

    private val presenter: MainPresenter by inject()

    private val catsFragment = CatsFragment.instance()
    private val dogsFragment = DogsFragment.instance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            if (savedInstanceState == null) renderFragment(view, Screens.CatsScreen)
            mainBottomNavigationMenu.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menuTitleCat -> {
                        renderFragment(this, Screens.CatsScreen)
                        true
                    }
                    R.id.menuTitleDog -> {
                        renderFragment(this, Screens.DogsScreen)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackPressed()
        return (getCurrentFragment() as? BackPressedInterface)?.onBackPressed() ?: false
    }

    private fun getCurrentFragment(): Fragment? =
        view?.let {
            childFragmentManager.findFragmentById(it.frameLayoutMain.id)
        }

    private fun renderFragment(v: View, screen: Screens) {
        v.apply {
            childFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayoutMain, if (screen is Screens.CatsScreen) catsFragment else dogsFragment)
                .commit()
        }
    }

    companion object {
        fun instance(): MainFragment = MainFragment()
    }
}