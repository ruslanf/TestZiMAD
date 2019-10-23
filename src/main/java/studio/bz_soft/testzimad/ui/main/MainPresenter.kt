package studio.bz_soft.testzimad.ui.main

import studio.bz_soft.testzimad.root.MainRouter
import studio.bz_soft.testzimad.root.Screens

class MainPresenter(private val router: MainRouter): MainInterface {

    override fun navigate(screen: Screens) {
        router.navigateTo(screen)
    }

    override fun onBackPressed() {
        router.exit()
    }
}