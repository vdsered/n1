package com.companyname.noneru.data

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class N1Photo(
    @SerializedName("1200x900p")
    val url: String
)

data class N1Params(
    @SerializedName("rooms_count")
    val roomCount: Int,
    @SerializedName("total_area")
    val totalArea: Int,
    @SerializedName("living_area")
    val livingArea: Int,
    @SerializedName("kitchen_area")
    val kitchenArea: Int,
    @SerializedName("floors_count")
    val floorCount: Int,
    @SerializedName("floor")
    val floor: Int,
    @SerializedName("house_addresses")
    val houseAddresses: List<N1HouseAddress>?,
    @SerializedName("price")
    val price: Int
)

data class N1Street(
    @SerializedName("name_ru")
    val name: String?
)

data class N1HouseAddress(
    @SerializedName("street")
    val street: N1Street?,
    @SerializedName("house_number")
    val houseNumber: String?
)

data class N1Result(
    @SerializedName("photo_count")
    val photoCount: Int,
    @SerializedName("photos")
    val photos: List<N1Photo>,
    @SerializedName("params")
    val params: N1Params
)
data class N1ResultSet(
    @SerializedName("count")
    val count: Long
)

data class N1Metadata(
    @SerializedName("resultset")
    val resultSet: N1ResultSet
)

data class N1Response(
    @SerializedName("metadata")
    val metadata: N1Metadata,
    @SerializedName("result")
    val results: List<N1Result>
)

interface N1Api {
    @GET("?filter[region_id]=1054&query[0][deal_type]=sell&query[0][rubric]" +
            "=flats&query[0][status]=published&sort=-creation_date")
    fun retrieveOffers(@Query("limit") limit: Long,
                       @Query("offset") offset: Long): Call<N1Response>
}

