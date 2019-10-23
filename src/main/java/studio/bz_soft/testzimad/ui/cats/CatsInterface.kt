package studio.bz_soft.testzimad.ui.cats

import studio.bz_soft.testzimad.data.models.DataList

interface CatsInterface {
    suspend fun getListOfCats(): List<DataList>
    fun showCatDetailed(cat: DataList)
    fun onBackPressed()
}