package com.example.lab6_part2

import com.example.lab6_part2.data.DataBaseConnector
import com.example.lab6_part2.data.DataBaseRepository
import com.example.lab6_part2.data.HotelRoom
import com.example.lab6_part2.data.Repository
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.ComboBox
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.io.IOException
import java.net.URL
import java.util.*
import java.util.function.Function
import java.util.function.Predicate
import kotlin.collections.ArrayList


class MainController : Initializable {
    @FXML
    lateinit var listHotelRooms: ListView<HotelRoom>

    @FXML
    lateinit var freeRoomsCombo: ComboBox<String>

    @FXML
    lateinit var capacityTextField: TextField

    @FXML
    lateinit var maxPriceTextField: TextField

    private var repository: Repository? = null

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        repository = DataBaseRepository(DataBaseConnector("hotelRoomsDB"))
        updateListsView()

        freeRoomsCombo.setOnAction(EventHandler { actionEvent: ActionEvent -> filterByFree(actionEvent) })

        capacityTextField.setOnAction(EventHandler { actionEvent: ActionEvent -> filterByCapacityAndPrice(actionEvent) })

        maxPriceTextField.setOnAction(EventHandler { actionEvent: ActionEvent -> filterByCapacityAndPrice(actionEvent) })
    }

    fun updateListsView() {
        val hotelRooms = repository!!.getAll()
        val hotelRoomsList: ObservableList<HotelRoom> = FXCollections.observableArrayList(hotelRooms)
        listHotelRooms.items = hotelRoomsList

        val freeRooms: MutableList<String> = ArrayList()
        freeRooms.addAll(
                hotelRooms.stream()
                        .map(Function<HotelRoom, String> { hotelRoom: HotelRoom ->
                            if (hotelRoom.isOccupied) "зайнято" else "вільно"
                        })
                        .distinct()
                        .toList()
        )
        freeRooms.add("all")

        val descriptionList: ObservableList<String> = FXCollections.observableArrayList(freeRooms)
        freeRoomsCombo.items = descriptionList
        freeRoomsCombo.selectionModel.select(freeRooms.size - 1)
    }


    @FXML
    fun deleteHotelRoom(actionEvent: ActionEvent?) {
        val toDelete = listHotelRooms.selectionModel.selectedItem as HotelRoom
        repository?.deleteHotelRoom(toDelete.id)
        updateListsView()
    }

    @FXML
    fun addNewHotelRoom(actionEvent: ActionEvent?) {
        val newWindow = Stage()
        val loader = FXMLLoader(MainController::class.java.getResource("add-hotel-room-view.fxml"))
        var root: Parent? = null
        root = try {
            loader.load()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        newWindow.title = "Додати номер"
        newWindow.scene = Scene(root, 250.0, 220.0)
        val secondController = loader.getController<AddHotelRoomController>()
        secondController.set_repository(repository)
        secondController.set_mainController(this)
        newWindow.show()
    }


    fun filterByFree(actionEvent: ActionEvent?) {
        val isOccupied: String = freeRoomsCombo.selectionModel.selectedItem
        val hotelRooms: List<HotelRoom>
        if ("all" == isOccupied) {
            hotelRooms = repository!!.getAll()
            Collections.sort(hotelRooms)
        } else {
            val isOccupiedValue = "зайнято" == isOccupied
            hotelRooms = repository!!.getAllByFree(isOccupiedValue)
        }
        val hotelRoomsList: ObservableList<HotelRoom> = FXCollections.observableArrayList(hotelRooms)
        listHotelRooms.items = hotelRoomsList
        applyFilters()
    }

    fun filterByCapacityAndPrice(actionEvent: ActionEvent?) {
        val capacityText = capacityTextField.text
        val maxPriceText = maxPriceTextField.text
        if (capacityText.isEmpty() && maxPriceText.isEmpty()) {
            // Якщо обидва поля порожні, забираємо всі фільтри
            listHotelRooms.items = FXCollections.observableArrayList(repository!!.getAll())
            return
        }
        val capacity = if (capacityText.isEmpty()) 0 else capacityText.toInt()
        val maxPrice = if (maxPriceText.isEmpty()) Double.MAX_VALUE else maxPriceText.toDouble()
        val filteredRooms: List<HotelRoom> = repository!!.getAllByCapacityAndPrice(capacity, maxPrice)
        val hotelRoomsList: ObservableList<HotelRoom> = FXCollections.observableArrayList(filteredRooms)
        listHotelRooms.items = hotelRoomsList
        applyFilters()
    }

    fun applyFilters() {
        val isOccupied: String = freeRoomsCombo.selectionModel.selectedItem
        val capacityText = capacityTextField.text
        val maxPriceText = maxPriceTextField.text
        val filterByOccupancy = "all" != isOccupied
        val filterByCapacity = !capacityText.isBlank()
        val filterByPrice = !maxPriceText.isBlank()
        val currentHotelRoomsList: ObservableList<HotelRoom> = listHotelRooms.items
        val filteredRooms: MutableList<HotelRoom> = ArrayList(currentHotelRoomsList)

        if (filterByOccupancy) {
            val isOccupiedValue = "зайнято" == isOccupied
            filteredRooms.removeIf(Predicate<HotelRoom> { hotelRoom: HotelRoom -> hotelRoom.isOccupied != isOccupiedValue })
        }

        if (filterByCapacity) {
            val capacity = capacityText.toIntOrNull() ?: 0
            filteredRooms.removeIf(Predicate<HotelRoom> { hotelRoom: HotelRoom -> hotelRoom.capacity != capacity })
        }

        if (filterByPrice) {
            val maxPrice = maxPriceText.toDoubleOrNull() ?: Double.MAX_VALUE
            filteredRooms.removeIf(Predicate<HotelRoom> { hotelRoom: HotelRoom -> hotelRoom.price > maxPrice })
        }

        filteredRooms.sort()
        val filteredHotelRoomsList: ObservableList<HotelRoom> = FXCollections.observableArrayList(filteredRooms)
        listHotelRooms.items = filteredHotelRoomsList
    }

    fun editHotelRoom(actionEvent: ActionEvent?) {
        // TODO
        // редагування вибраного зі списку елемента
        // пропонується розробити самостійно
        // за прикладом створення вікна для додавання
        // нового елемента в список
    }
}
