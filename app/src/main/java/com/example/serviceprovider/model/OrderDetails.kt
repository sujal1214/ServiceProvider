package com.example.serviceprovider.model

import android.os.Parcel
import android.os.Parcelable

class OrderDetails(): Parcelable {
    var userUid : String?=null
    var userName : String?=null
    var serviceNames : MutableList<String>?=null
    var serviceImages : MutableList<String>?=null
    var servicePrices : MutableList<String>?=null
    var serviceQuantities : MutableList<Int>?=null
    var address : String?=null
    var totalPrice : String?=null
    var phoneNumber :String?=null
    var orderAccepted : Boolean = false
    var paymentReceived : Boolean = false
    var itemPushKey : String?=null
    var currentTime : Long = 0

    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        userName = parcel.readString()
        address = parcel.readString()
        totalPrice = parcel.readString()
        phoneNumber = parcel.readString()
        orderAccepted = parcel.readByte() != 0.toByte()
        paymentReceived = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }

    constructor(
        userId: String,
        name: String,
        foodItemName: ArrayList<String>,
        foodItemPrice: ArrayList<String>,
        foodItemImage: ArrayList<String>,
        foodItemQuantities: ArrayList<Int>,
        address: String,
        totalAmount: String,
        phone: String,
        time: Long,
        itemPushKey: String?,
        b: Boolean,
        b1: Boolean
    ): this() {
        this.userUid = userId
        this.userName = name
        this.serviceNames = foodItemName
        this.servicePrices = foodItemPrice
        this.serviceImages = foodItemImage
        this.serviceQuantities = foodItemQuantities
        this.address = address
        this.totalPrice = totalAmount
        this.phoneNumber = phone
        this.currentTime = time
        this.itemPushKey = itemPushKey
        this.orderAccepted = b
        this.paymentReceived = b1
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(userName)
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNumber)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentReceived) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetails>{
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<out OrderDetails?> {
            return arrayOfNulls(size)
        }
    }
}