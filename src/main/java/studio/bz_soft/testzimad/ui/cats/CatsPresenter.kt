package studio.bz_soft.testzimad.ui.cats

import kotlinx.coroutines.coroutineScope
import studio.bz_soft.testzimad.data.Repository
import studio.bz_soft.testzimad.data.models.DataList
import studio.bz_soft.testzimad.root.MainRouter
import studio.bz_soft.testzimad.root.Screens

class CatsPresenter(private val router: MainRouter,
                    private val repository: Repository): CatsInterface {

    override suspend fun getListOfCats() = coroutineScope {
        repository.getListOfCats().listOfData
    }

    override fun showCatDetailed(cat: DataList) {
        router.navigateTo(Screens.DetailedAnimalScreen(cat, Screens.CatsScreen))
    }

    override fun onBackPressed() {
        router.exit()
    }
}