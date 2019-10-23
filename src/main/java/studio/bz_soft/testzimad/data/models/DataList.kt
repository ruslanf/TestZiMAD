package studio.bz_soft.testzimad.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataList(
    @SerializedName("url") val url: String?,
    @SerializedName("title") val title: String?
) : Parcelable {
    constructor() : this("", "")
}