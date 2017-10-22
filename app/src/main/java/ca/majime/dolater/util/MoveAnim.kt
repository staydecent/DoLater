package ca.majime.dolater.util

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator

class MoveAnim(private var view: View) {

    private var interpolator: Interpolator

    init {
        interpolator = AccelerateDecelerateInterpolator()
    }

    fun setInterpolator(interpolator: Interpolator) {
        this.interpolator = interpolator
    }

    private fun move(which: String, end: Float, duration: Int) {
        val start = if (which == "Y") view.y else view.x
        val moveAnim = ObjectAnimator.ofFloat(view, which, start, end)
        moveAnim.duration = duration.toLong()
        moveAnim.interpolator = interpolator
        moveAnim.start()
    }

    fun moveY(end: Float, duration: Int) {
        move("Y", end, duration)
    }

    fun moveX(end: Float, duration: Int) {
        move("X", end, duration)
    }
}
