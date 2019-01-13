package com.companyname.noneru.data
import kotlinx.serialization.Serializable
@Serializable
data class Offer(
    val roomCount: Int,
    val totalArea: Int,
    val price: Int,
    val floorCount: Int,
    val floor: Int,
    val livingArea: Int,
    val kitchenArea: Int,
    val streetName: String,
    val houseNumber: String,
    val photos: List<String>
)