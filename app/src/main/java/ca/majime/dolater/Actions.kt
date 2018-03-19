package ca.majime.dolater


@Suppress("unused") val Any?.unit get() = Unit

sealed class Action

data class UserInput(val input: String) : Action()
data class UserSubmit(val input: String?, val day: Int?) : Action()
data class ToggleSelection(val type: String, val value: Int) : Action()

