package com.proficiencyassesment.model

import com.google.gson.annotations.SerializedName

data class Facts(@SerializedName("title") val title : String?,
                 @SerializedName("rows") val rows : List<CountryInformation>?)