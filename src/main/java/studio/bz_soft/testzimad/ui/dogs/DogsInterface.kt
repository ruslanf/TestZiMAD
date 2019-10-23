package studio.bz_soft.testzimad.ui.dogs

import studio.bz_soft.testzimad.data.models.DataList

interface DogsInterface {
    suspend fun getListOfDogs(): List<DataList>
    fun showDogDetailed(dog: DataList)
    fun onBackPressed()
}