package cat.xojan.random1.domain.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Images(@SerializedName("person-small") val imageUrl: String) : Parcelable