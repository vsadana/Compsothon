package com.example.composothon
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel

data class Question(
    var type: String,
    var answer: String,
    var options: List<String>,
    var logoImage: Painter,
    var squadLogos: List<Painter>,
    var name: String,
)

data class PeopleData(
    var name: String,
    var squad: String,
    var logoImage: Painter,
)

data class SquadData(
    var name: String,
    var logoImage: Painter,
)

class ScoreViewModel : ViewModel() {
    var value = 0
}