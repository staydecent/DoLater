package ca.majime.dolater

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import java.util.*


class MainModel : ViewModel() {

    private val mutableUserInput = MutableLiveData<String>()
    private val mutableDate = MutableLiveData<Date>()

    val userInput: LiveData<String> = mutableUserInput
    val date: LiveData<Date> = mutableDate

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
                val calendar = Calendar.getInstance()

                if (action.type == "date") {
                    when (action.value) {
                        1 -> {
                            calendar.add(Calendar.DAY_OF_YEAR, 1)
                            val tomorrow = calendar.time
                            Log.d("MainModel", "Tomorrow ${tomorrow}")
                            mutableDate.value = calendar.time
                        }
                    }
                } else if (action.type == "time") {
                    when (action.value) {
                        1 -> {
                            calendar.set(Calendar.HOUR_OF_DAY, 8)
                            val tomorrow = calendar.time
                            Log.d("MainModel", "Morning ${tomorrow}")
                        }
                    }
                }

                // @TODO: Save toggle state for each viewName
            }
        }
    }

    fun action(action: Action) = actor.offer(action)

    override fun onCleared() = actor.cancel().unit
}
