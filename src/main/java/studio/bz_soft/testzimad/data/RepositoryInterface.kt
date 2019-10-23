package studio.bz_soft.testzimad.data

import studio.bz_soft.testzimad.data.models.DataModel

interface RepositoryInterface {
    suspend fun getListOfCats(): DataModel
    suspend fun getListOfDogs(): DataModel
}