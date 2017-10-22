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

    val userInput: LiveData<String> = mutableUserInput

    private val actor = actor<Action>(UI, Channel.CONFLATED) {
        for (action in this) when (action) {
            is UserInput -> {
                mutableUserInput.value = action.input
            }

            is UserSubmit -> {
                Log.d("MainModel", "UserSubmit ${action.input}")
            }
        }
    }

    fun action(action: Action) = actor.offer(action)

    override fun onCleared() = actor.cancel().unit
}