package com.example.lab6_part2

import com.example.lab6_part2.data.HotelRoom
import com.example.lab6_part2.data.Repository
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.CheckBox
import javafx.scene.control.TextField
import javafx.stage.Stage

class AddHotelRoomController {
    private var _repository: Repository? = null
    private var _mainController: MainController? = null

    fun set_mainController(mainController: MainController?) {
        _mainController = mainController
    }

    fun set_repository(repository: Repository?) {
        _repository = repository
    }

    @FXML
    lateinit var description: TextField

    @FXML
    lateinit var capacity: TextField

    @FXML
    lateinit var price: TextField

    @FXML
    lateinit var isOccupied: CheckBox

    @FXML
    lateinit var checkInDate: TextField

    @FXML
    lateinit var lengthOfStay: TextField

    @FXML
    fun addHotelRoomToFile(actionEvent: ActionEvent) {
        val description_ = description.text
        val capacity_ = capacity.text.toInt()
        val price_ = price.text.toDouble()
        val isOccupiedValue_ = isOccupied.isSelected
        val checkInDate_ = if (checkInDate.text.isEmpty()) null else checkInDate.text
        val lengthOfStay_ = if (lengthOfStay.text.isEmpty()) null else lengthOfStay.text.toIntOrNull()

        val newHotelRoom = HotelRoom(
                description_,
                capacity_,
                price_,
                isOccupiedValue_,
                checkInDate_,
                lengthOfStay_ ?: 0 // Додано ?: 0 для випадку, коли lengthOfStay_ має значення null
        )
        _repository?.addHotelRoom(newHotelRoom)

        val source = actionEvent.source as Node
        val stage: Stage = source.scene.window as Stage
        _mainController?.updateListsView()
        stage.close()
    }
}
