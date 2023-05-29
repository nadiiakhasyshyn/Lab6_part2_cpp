package com.example.lab6_part2.data

import java.sql.SQLException
import java.sql.Types
import java.util.ArrayList

class DataBaseRepository(private val dataBaseConnector: DataBaseConnector) : Repository {
    init {
        try {
            dataBaseConnector.connection.use { conn ->
                val tableCreateStr = """
                    CREATE TABLE IF NOT EXISTS HotelRooms
                    (id INT NOT NULL AUTO_INCREMENT, Description VARCHAR(50), Capacity INT, Price DOUBLE, isOccupied BIT ,checkInDate DATE, lengthOfStay INT,  PRIMARY KEY (id));
                """.trimIndent()
                val createTable = conn.createStatement()
                createTable.execute(tableCreateStr)
            }
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    override fun getAll(): List<HotelRoom> {
        val hotelRooms: MutableList<HotelRoom> = ArrayList()
        try {
            dataBaseConnector.connection.use { connection ->
                val statement = connection.createStatement()
                val rs = statement.executeQuery("select * from HotelRooms")
                while (rs.next()) {
                    hotelRooms.add(
                            HotelRoom(
                                    rs.getInt("id"),
                                    rs.getString("Description"),
                                    rs.getInt("Capacity"),
                                    rs.getDouble("Price"),
                                    rs.getBoolean("isOccupied"),
                                    rs.getString("checkInDate"),
                                    rs.getInt("lengthOfStay")
                            )
                    )
                }
                rs.close()
            }
        } catch (exception: SQLException) {
            println("Не відбулося підключення до БД")
            exception.printStackTrace()
        }
        return hotelRooms
    }

    override fun getById(id: Int): HotelRoom {
        var hotelRoom: HotelRoom? = null
        try {
            dataBaseConnector.connection.use { connection ->
                val statement = connection.prepareStatement("select * from HotelRooms where id = ?")
                statement.setInt(1, id)
                val rs = statement.executeQuery()
                if (rs.next()) {
                    hotelRoom = HotelRoom(
                            rs.getInt("id"),
                            rs.getString("Description"),
                            rs.getInt("Capacity"),
                            rs.getDouble("Price"),
                            rs.getBoolean("isOccupied"),
                            rs.getString("checkInDate"),
                            rs.getInt("lengthOfStay")
                    )
                }
                rs.close()
            }
        } catch (exception: SQLException) {
            exception.printStackTrace()
        }
        return hotelRoom!!
    }

    override fun getAllByFree(isOccupied: Boolean): List<HotelRoom> {
        val hotelRooms: MutableList<HotelRoom> = ArrayList()
        try {
            dataBaseConnector.connection.use { connection ->
                val statement = connection.prepareStatement(
                        "select * from HotelRooms where isOccupied = ?"
                )
                statement.setBoolean(1, isOccupied)
                val rs = statement.executeQuery()
                while (rs.next()) {
                    hotelRooms.add(
                            HotelRoom(
                                    rs.getInt("id"),
                                    rs.getString("Description"),
                                    rs.getInt("Capacity"),
                                    rs.getDouble("Price"),
                                    rs.getBoolean("isOccupied"),
                                    rs.getString("checkInDate"),
                                    rs.getInt("lengthOfStay")
                            )
                    )
                }
                rs.close()
            }
        } catch (exception: SQLException) {
            println("Не відбулося підключення до БД")
            exception.printStackTrace()
        }
        return hotelRooms
    }

    override fun addHotelRoom(hotelRoom: HotelRoom?): Boolean {
        var updCount = 0
        try {
            dataBaseConnector.connection.use { conn ->
                val preparedStatement = conn.prepareStatement(
                        "INSERT INTO HotelRooms (Description, Capacity, Price, IsOccupied, CheckInDate, LengthOfStay) VALUES (?,?,?,?,?,?)"
                )
                preparedStatement.setString(1, hotelRoom!!.description)
                preparedStatement.setInt(2, hotelRoom.capacity)
                preparedStatement.setDouble(3, hotelRoom.price)
                preparedStatement.setBoolean(4, hotelRoom.isOccupied)
                if (hotelRoom?.checkInDate != null) {
                    preparedStatement.setString(5, hotelRoom.checkInDate)
                } else {
                    preparedStatement.setNull(5, Types.DATE) // або Types.TIMESTAMP залежно від типу поля у базі даних
                }

                preparedStatement.setInt(6, hotelRoom.lengthOfStay ?: 0)


                updCount = preparedStatement.executeUpdate()
            }
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
        return updCount > 0
    }

    override fun updateHotelRoom(id: Int, hotelRoom: HotelRoom): Boolean {
        var updCount = 0
        try {
            dataBaseConnector.connection.use { conn ->
                val preparedStatement = conn.prepareStatement(
                        "UPDATE HotelRooms " +
                                "SET Description = ?, Capacity = ?," +
                                "Price = ?, IsOccupied = ?, CheckInDate = ?, LengthOfStay = ? " +
                                "WHERE id = ?"
                )
                preparedStatement.setString(1, hotelRoom.description)
                preparedStatement.setInt(2, hotelRoom.capacity)
                preparedStatement.setDouble(3, hotelRoom.price)
                preparedStatement.setBoolean(4, hotelRoom.isOccupied)
                if (hotelRoom.checkInDate != null) {
                    preparedStatement.setString(5, hotelRoom.checkInDate)
                } else {
                    preparedStatement.setNull(5, Types.DATE) // або Types.TIMESTAMP залежно від типу поля у базі даних
                }

                preparedStatement.setInt(6, hotelRoom.lengthOfStay ?: 0)

                preparedStatement.setInt(7, id)
                updCount = preparedStatement.executeUpdate()
            }
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
        return updCount > 0
    }

    override fun deleteHotelRoom(id: Int): Boolean {
        var updCount = 0
        try {
            dataBaseConnector.connection.use { conn ->
                val preparedStatement = conn.prepareStatement(
                        "DELETE FROM HotelRooms WHERE id = ?"
                )
                preparedStatement.setInt(1, id)
                updCount = preparedStatement.executeUpdate()
            }
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
        return updCount > 0
    }

    override fun getAllByCapacityAndPrice(capacity: Int, maxPrice: Double): List<HotelRoom> {
        val hotelRooms: MutableList<HotelRoom> = ArrayList()
        try {
            dataBaseConnector.connection.use { connection ->
                val statement = connection.prepareStatement(
                        "SELECT * FROM HotelRooms WHERE Capacity >= ? AND Price <= ?"
                )
                statement.setInt(1, capacity)
                statement.setDouble(2, maxPrice)
                val rs = statement.executeQuery()
                while (rs.next()) {
                    hotelRooms.add(
                            HotelRoom(
                                    rs.getInt("id"),
                                    rs.getString("Description"),
                                    rs.getInt("Capacity"),
                                    rs.getDouble("Price"),
                                    rs.getBoolean("isOccupied"),
                                    rs.getString("checkInDate"),
                                    rs.getInt("lengthOfStay")
                            )
                    )
                }
                rs.close()
            }
        } catch (exception: SQLException) {
            println("Не відбулося підключення до БД")
            exception.printStackTrace()
        }
        return hotelRooms
    }
}
