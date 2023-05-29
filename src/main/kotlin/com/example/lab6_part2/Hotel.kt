package com.example.lab6_part2

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import java.io.IOException


class Hotel : Application() {
    @Throws(IOException::class)
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(
                Hotel::class.java.getResource("main-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 320.0, 240.0)
        stage.title = "Hotel rooms"
        stage.scene = scene
        stage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Hotel::class.java,  *args)
        }
    }
}