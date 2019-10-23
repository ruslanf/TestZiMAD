package studio.bz_soft.testzimad.data.http

import studio.bz_soft.testzimad.data.models.DataModel

interface ApiClientInterface {
    suspend fun getCatsList(): DataModel
    suspend fun getDogsList(): DataModel
}