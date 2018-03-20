package ca.majime.dolater

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.animation.OvershootInterpolator
import android.view.inputmethod.EditorInfo
import ca.majime.dolater.util.MoveAnim
import ca.majime.dolater.util.database
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.db.insert


class MainActivity : AppCompatActivity() {

    private val registry = LifecycleRegistry(this)

    private val metrics = DisplayMetrics()

    lateinit var model: MainModel

    override fun getLifecycle() = registry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTheme()
        initModel()
        initDetailsView()
        handleUserEvents()
    }

    private fun initTheme() {
        val window = window
        val decor = window.decorView

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.offWhite)
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun initModel() {
        model = ViewModelProviders.of(this).get(MainModel::class.java)
        model.userInput.observe(this, Observer { handleUserInput(it ?: "") })
    }

    private fun handleUserInput(input: String) {
        val anim = MoveAnim(details)
        anim.setInterpolator(OvershootInterpolator(0.6f))
        if (input.isNotBlank()) {
            anim.moveY((details.height - resources.getDimensionPixelSize(R.dimen.margin)).toFloat(), 400)
        } else {
            anim.moveY(metrics.heightPixels.toFloat(), 300)
        }
    }

    private fun handleTimeOfDayStyle(hour: Int) {

    }

    private fun initDetailsView() {
        this.windowManager.defaultDisplay.getMetrics(metrics)
        details.y = metrics.heightPixels.toFloat()
    }

    private fun handleUserEvents() {
        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                model.action(UserInput(s.toString()))
            }
        })

        date_1.setOnClickListener { getOptClickHandler("date", 1) }
        date_2.setOnClickListener { getOptClickHandler("date", 2) }
        date_3.setOnClickListener { getOptClickHandler("date", 3) }

        time_1.setOnClickListener { getOptClickHandler("time", 1) }
        time_2.setOnClickListener { getOptClickHandler("time", 2) }
        time_3.setOnClickListener { getOptClickHandler("time", 3) }

        input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handleSubmit()
                true
            } else {
                false
            }
        }
        submit.setOnClickListener {
            handleSubmit()
        }
    }

    private fun handleSubmit() {
        database.use {
            insert("Reminder",
                    "_id" to null,
                    "text" to model.userInput.value,
                    "date" to model.date.value
            )
        }
    }

    private fun getOptClickHandler(type: String, value: Int) {
        model.action(ToggleSelection(type, value))
        if (type == "date") {
            when (value) {
                1 -> {
                    date_1.backgroundTintList = ContextCompat.getColorStateList(this, R.color.textGray)
                    date_2.backgroundTintList = null
                    date_3.backgroundTintList = null
                }
                2 -> {
                    date_1.backgroundTintList = null
                    date_2.backgroundTintList = ContextCompat.getColorStateList(this, R.color.textGray)
                    date_3.backgroundTintList = null
                }
                3 -> {
                    date_1.backgroundTintList = null
                    date_2.backgroundTintList = null
                    date_3.backgroundTintList = ContextCompat.getColorStateList(this, R.color.textGray)
                }
            }
        }
        else if (type == "time") {
            when (value) {
                1 -> {
                    time_1.backgroundTintList = ContextCompat.getColorStateList(this, R.color.textGray)
                    time_2.backgroundTintList = null
                    time_3.backgroundTintList = null
                }
                2 -> {
                    time_1.backgroundTintList = null
                    time_2.backgroundTintList = ContextCompat.getColorStateList(this, R.color.textGray)
                    time_3.backgroundTintList = null
                }
                3 -> {
                    time_1.backgroundTintList = null
                    time_2.backgroundTintList = null
                    time_3.backgroundTintList = ContextCompat.getColorStateList(this, R.color.textGray)
                }
            }
        }
    }
}
