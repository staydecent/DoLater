package ca.majime.dolater

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor

class MainModel : ViewModel() {

    private val mutableUserInput = MutableLiveData<String>()
    private val mutableDay = MutableLiveData<Int>()

    val userInput: LiveData<String> = mutableUserInput
    val day: LiveData<Int> = mutableDay

    private val actor = actor<Action>(UI, Channel.CONFLATED) {
        for (action in this) when (action) {

            is UserInput -> {
                mutableUserInput.value = action.input
            }

            is UserSubmit -> {
                Log.d("MainModel", "UserSubmit ${action.input}")
            }

            is ToggleSelection -> {
                Log.d("MainModel", "ToggleSelection ${action.type} ${action.value}")
                // @TODO: Save toggle state for each viewName
            }
        }
    }

    fun action(action: Action) = actor.offer(action)

    override fun onCleared() = actor.cancel().unit
}
