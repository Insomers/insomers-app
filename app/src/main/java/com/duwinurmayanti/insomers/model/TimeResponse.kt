package com.duwinurmayanti.insomers.model

import com.google.gson.annotations.SerializedName

data class TimeResponse(

	@field:SerializedName("currentTime")
	val currentTime: String? = null,

	@field:SerializedName("currentDate")
	val currentDate: String? = null
)
