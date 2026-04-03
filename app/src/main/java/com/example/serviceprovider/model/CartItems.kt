package com.example.serviceprovider.model

data class BookingItems(
    val serviceName : String?=null,
    val servicePrice : String?=null,
    val serviceDescription : String?=null,
    val serviceImage : String?=null,
    val serviceQuantity : Int?=null,
    val serviceFacility : String?=null
)