package studio.bz_soft.testzimad.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DataModel(
    @SerializedName("message") val message: String?,
    @SerializedName("data") val listOfData: List<DataList>
) : Serializable
