package ca.majime.dolater

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import ca.majime.dolater.util.nextFriday
import ca.majime.dolater.util.nextWeek
import ca.majime.dolater.util.tomorrow
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import org.threeten.bp.LocalDate


class MainModel : ViewModel() {

    private val mutableUserInput = MutableLiveData<String>()
    private val mutableDate = MutableLiveData<LocalDate>()
    private val mutableTimeOfDay = MutableLiveData<Int>()

    val userInput: LiveData<String> = mutableUserInput
    val date: LiveData<LocalDate> = mutableDate
    val timeOfDay: LiveData<Int> = mutableTimeOfDay

    private val actor = actor<Action>(UI, Channel.CONFLATED) {
        for (action in this) when (action) {

            is UserInput -> {
                mutableUserInput.value = action.input
            }

            is UserSubmit -> {
                Log.d("MainModel", "UserSubmit ${action.input} - ${mutableDate.value} - ${mutableTimeOfDay.value}")
            }

            is ToggleSelection -> {
                Log.d("MainModel", "ToggleSelection ${action.type} ${action.value}")

                when (action.type) {
                    "date" -> {
                        when (action.value) {
                            1 -> mutableDate.value = tomorrow()
                            2 -> mutableDate.value = nextFriday()
                            3 -> mutableDate.value = nextWeek()
                        }
                    }
                    "time" -> {
                        when (action.value) {
                            1 -> mutableTimeOfDay.value = 8
                            2 -> mutableTimeOfDay.value = 12
                            3 -> mutableTimeOfDay.value = 18
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
