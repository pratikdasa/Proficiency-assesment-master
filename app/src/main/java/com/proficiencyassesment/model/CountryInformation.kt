package com.proficiencyassesment.model

import com.google.gson.annotations.SerializedName

data class CountryInformation(
    @SerializedName("title") val title : String?,
    @SerializedName("description") val description : String?,
    @SerializedName("imageHref") val imageHref : String?
)