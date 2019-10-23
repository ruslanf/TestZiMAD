package studio.bz_soft.testzimad.ui.detailed

import studio.bz_soft.testzimad.data.models.DataList
import studio.bz_soft.testzimad.root.Screens

interface DetailedInterface {
    suspend fun getDetailedInfo(): DataList
    fun onBackPressed(screen: Screens)
}