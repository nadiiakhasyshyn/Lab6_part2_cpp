package com.example.lab6_part2.data


interface Repository {
    fun getAll(): List<HotelRoom>
    fun getById(id: Int): HotelRoom
    fun getAllByFree(isOccupied: Boolean): List<HotelRoom>
    fun addHotelRoom(hotelRoom: HotelRoom?): Boolean
    fun updateHotelRoom(id: Int, hotelRoom: HotelRoom): Boolean
    fun deleteHotelRoom(id: Int): Boolean
    fun getAllByCapacityAndPrice(capacity: Int, maxPrice: Double): List<HotelRoom>
}

