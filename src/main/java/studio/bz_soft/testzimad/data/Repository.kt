package studio.bz_soft.testzimad.data

import studio.bz_soft.testzimad.data.http.ApiClientInterface
import studio.bz_soft.testzimad.data.models.DataModel

class Repository(private val client: ApiClientInterface): RepositoryInterface {

    override suspend fun getListOfCats(): DataModel = client.getCatsList()

    override suspend fun getListOfDogs(): DataModel = client.getDogsList()
}