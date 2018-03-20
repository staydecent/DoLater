package ca.majime.dolater

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import ca.majime.dolater.util.nextFriday
import ca.majime.dolater.util.nextWeek
import ca.majime.dolater.util.setHour
import ca.majime.dolater.util.tomorrow
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import org.threeten.bp.LocalDateTime


class MainModel : ViewModel() {

    private val mutableUserInput = MutableLiveData<String>()
    private val mutableDate = MutableLiveData<LocalDateTime>()

    val userInput: LiveData<String> = mutableUserInput
    val date: LiveData<LocalDateTime> = mutableDate

    private val actor = actor<Action>(UI, Channel.CONFLATED) {
        for (action in this) when (action) {

            is UserInput -> {
                mutableUserInput.value = action.input
            }

            is ToggleSelection -> {
                Log.d("MainModel", "ToggleSelection ${action.type} ${action.value}")

                when (action.type) {
                    "date" -> {
                        when (action.value) {
                            1 -> mutableDate.value = tomorrow(mutableDate.value)
                            2 -> mutableDate.value = nextFriday(mutableDate.value)
                            3 -> mutableDate.value = nextWeek(mutableDate.value)
                        }
                    }
                    "time" -> {
                        when (action.value) {
                            1 -> mutableDate.value = setHour(mutableDate.value, 12)
                            2 -> mutableDate.value = setHour(mutableDate.value, 12)
                            3 -> mutableDate.value = setHour(mutableDate.value, 18)
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
