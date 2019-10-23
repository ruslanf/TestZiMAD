package studio.bz_soft.testzimad.ui.dogs

import kotlinx.coroutines.coroutineScope
import studio.bz_soft.testzimad.data.Repository
import studio.bz_soft.testzimad.data.models.DataList
import studio.bz_soft.testzimad.root.MainRouter
import studio.bz_soft.testzimad.root.Screens

class DogsPresenter(private val router: MainRouter,
                    private val repository: Repository): DogsInterface {
    override suspend fun getListOfDogs(): List<DataList> = coroutineScope {
        repository.getListOfDogs().listOfData
    }

    override fun showDogDetailed(dog: DataList) {
        router.navigateTo(Screens.DetailedAnimalScreen(dog, Screens.DogsScreen))
    }

    override fun onBackPressed() {
        router.exit()
    }
}