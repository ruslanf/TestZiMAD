package studio.bz_soft.testzimad.data.http

import retrofit2.Call
import retrofit2.http.GET
import studio.bz_soft.testzimad.data.models.DataModel

interface DataApiInterface {

    @GET("api.php?query=cat")
    fun getCatsList(): Call<DataModel>

    @GET("api.php?query=dog")
    fun getDogsList(): Call<DataModel>
}