package studio.bz_soft.testzimad.ui.detailed

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.coroutineScope
import studio.bz_soft.testzimad.data.models.DataList
import studio.bz_soft.testzimad.root.MainRouter
import studio.bz_soft.testzimad.root.Screens

class DetailedPresenter(private val router: MainRouter,
                        private val info: Deferred<DataList>): DetailedInterface {

    override suspend fun getDetailedInfo(): DataList = coroutineScope {
        info.await()
    }

    override fun onBackPressed(screen: Screens) {
        router.backTo(screen)
    }
}