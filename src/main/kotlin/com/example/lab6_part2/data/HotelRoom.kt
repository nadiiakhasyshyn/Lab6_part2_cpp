package com.example.lab6_part2.data
import java.io.Serializable

class HotelRoom : Serializable, Comparable<HotelRoom> {
    var id = 0
    var description: String? = null
    var capacity = 0
    var price = 0.0
    var isOccupied = false
    var checkInDate: String? = null
    var lengthOfStay: Int? = null

    constructor(description_: String, capacityValue: Int, priceValue: Double, isOccupiedValue: Boolean, checkInDateValue: String?, lengthOfStayValue: Int) {
        description = description_
        capacity = capacityValue
        price = priceValue
        isOccupied = isOccupiedValue
        checkInDate = checkInDateValue
        lengthOfStay = lengthOfStayValue
    }

    constructor(description: String?, capacity: Int, price: Double, isOccupied: Boolean) {
        this.description = description
        this.capacity = capacity
        this.price = price
        this.isOccupied = isOccupied
    }

    constructor(id: Int, description: String?, capacity: Int, price: Double, isOccupied: Boolean,
                checkInDate: String?, lengthOfStay: Int?) {
        this.id = id
        this.description = description
        this.capacity = capacity
        this.price = price
        this.isOccupied = isOccupied
        this.checkInDate = checkInDate
        this.lengthOfStay = lengthOfStay
    }

    fun check(isOccupied: Boolean): Boolean {
        return !isOccupied
    }

    fun check(capacity: Int, price: Double): Boolean {
        return this.price <= price && this.capacity == capacity
    }

    override fun toString(): String {
        return "HotelRoom(description='$description', capacity=$capacity, price=$price, isOccupied=$isOccupied, checkInDate=$checkInDate, lengthOfStay=$lengthOfStay)"
    }

    override fun compareTo(other: HotelRoom): Int {
        if (!isOccupied && other.isOccupied) {
            return -1
        }
        return if (isOccupied && !other.isOccupied) {
            1
        } else 0
    }
}
