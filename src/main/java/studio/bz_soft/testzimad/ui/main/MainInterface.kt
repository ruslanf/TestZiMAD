package studio.bz_soft.testzimad.ui.main

import studio.bz_soft.testzimad.root.Screens

interface MainInterface {
    fun navigate(screen: Screens)
    fun onBackPressed()
}