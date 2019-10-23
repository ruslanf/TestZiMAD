package studio.bz_soft.testzimad.ui.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.app_bar_layout.*
import org.koin.android.ext.android.inject
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import studio.bz_soft.testzimad.R
import studio.bz_soft.testzimad.root.BackPressedInterface
import studio.bz_soft.testzimad.root.Screens

class RootActivity : AppCompatActivity() {

    private val router by inject<Router>()
    private val navigatorHolder by inject<NavigatorHolder>()
    private val fragmentNavigator = SupportAppNavigator(this, supportFragmentManager, R.id.frameLayoutRoot)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) router.newRootScreen(Screens.MainScreen)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(fragmentNavigator)
    }

    override fun onBackPressed() {
        when (val supportFM = supportFragmentManager.findFragmentById(R.id.frameLayoutRoot)) {
            is BackPressedInterface -> when (supportFM.onBackPressed()) { false -> router.exit() }
            else -> router.exit()
        }
    }
}
