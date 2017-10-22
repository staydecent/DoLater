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
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.OvershootInterpolator
import android.view.inputmethod.EditorInfo
import ca.majime.dolater.util.MoveAnim
import kotlinx.android.synthetic.main.activity_main.*


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
        input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                model.action(UserSubmit(model.userInput.value))
                true
            } else {
                false
            }
        }
    }
}
