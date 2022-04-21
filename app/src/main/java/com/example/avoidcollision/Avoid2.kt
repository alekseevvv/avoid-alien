package com.example.avoidcollision

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment

class Avoid2 : Fragment(), View.OnTouchListener {
    var deltaX = 0f
    var deltaY = 0f
    var listImage = mutableListOf<ImageView>()
    var he = 0
    var we = 0
    var idcount = 0
    var time = 10
    var finish = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_avoid2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startTimer()
        createShadowGranat(1)
        createGranat(1)
        createGranat(4)

    }

    fun startTimer() {
        val timer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                view?.findViewById<TextView>(R.id.textView)?.text = time.toString()
                time--
            }

            override fun onFinish() {
                startTimer()
                createGranat(1)
                time = 10
            }
        }
        timer.start()
    }

    fun createShadowGranat(i: Int) {
        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
        val height = metrics.heightPixels
        val width = metrics.widthPixels
        we = width - width / 6
        he = height - width / 6
        val im_granat = ImageView(this.context)
        im_granat.layoutParams = TableRow.LayoutParams(width / 6, width / 6)
        im_granat.setPadding(5, 5, 5, 5)
        im_granat.setImageResource(R.drawable.shdow)
        im_granat.x = (width / 2 - width / 12).toFloat()
        im_granat.y = (i * width / 4).toFloat()
        view?.findViewById<ConstraintLayout>(R.id.frameLayout2)?.addView(im_granat)
    }

    fun createGranat(i: Int) {
        try {
            val metrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
            val height = metrics.heightPixels
            val width = metrics.widthPixels
            we = width - width / 6
            he = height - width / 6
            val im_granat = ImageView(this.context)
            im_granat.layoutParams = TableRow.LayoutParams(width / 6, width / 6)
            im_granat.setPadding(5, 5, 5, 5)
            im_granat.setImageResource(R.drawable.granat)
            im_granat.x = (width / 2 - width / 12).toFloat()
            im_granat.y = (i * width / 4).toFloat()
            im_granat.tag = idcount
            im_granat.id = 0
            idcount++
            listImage.add(im_granat)
            view?.findViewById<ConstraintLayout>(R.id.frameLayout2)?.addView(im_granat)
            granatAnim(im_granat, height.toFloat(), width.toFloat())
        } catch (e: Throwable) {

        }
    }

    fun granatAnim(im_granat: ImageView, height: Float, width: Float) {
        val h = (0..height.toInt() - im_granat.height).random()
        val w = (0..width.toInt() - im_granat.width).random()
        im_granat.setOnTouchListener(this)
        val animX = ValueAnimator.ofFloat(im_granat.x, w.toFloat())
            .apply {
                duration = 8000
                interpolator = LinearInterpolator()
            }
        animX.addUpdateListener { animation ->
            if (!finish) {
                if (im_granat.id == 1) {
                    animation.pause()
                }
                val x = animation.animatedValue as Float
                im_granat.x = x

                for (i in 0..idcount - 1) {
                    if (im_granat.tag != i) {
                        try {
                            if (checkCollision(im_granat, view?.findViewWithTag(i)!!)) {
                                avoidFinish()
                            }
                        } catch (e: Throwable) {
                        }
                    }
                }
            }
        }
        animX.start()

        val animY = ValueAnimator.ofFloat(im_granat.y, h.toFloat())
            .apply {
                duration = 8000
                interpolator = LinearInterpolator()
            }
        animY.addUpdateListener { animation ->
            if (!finish) {

                if (im_granat.id == 1) {
                    animation.pause()
                }
                val y = animation.animatedValue as Float
                im_granat.y = y
            }
        }
        animY.start()
        animY.doOnEnd {
            granatAnim(im_granat, height, width)
        }
    }


    @SuppressLint("ResourceType")
    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        if (p0 != null)
            when (p1?.action) {
                MotionEvent.ACTION_DOWN -> {
                    p0.id = 1
                    deltaX = p1.rawX - p0.x
                    deltaY = p1.rawY - p0.y
                }
                MotionEvent.ACTION_MOVE -> {
                    p0.x = p1.rawX - deltaX
                    p0.y = p1.rawY - deltaY
                }
                MotionEvent.ACTION_UP -> {
                    p0.id = 0
                    granatAnim(listImage[p0.tag as Int], he.toFloat(), we.toFloat())
                }
            }
        return true
    }

    fun checkCollision(barrier: ImageView, dog: ImageView): Boolean {
        val xy = IntArray(2)

        barrier.getLocationInWindow(xy)
        val br = Rect(
            xy[0], xy[1],
            (xy[0] + barrier.width * 0.8).toInt(), (xy[1] + barrier.height * 0.8f).toInt()
        )

        dog.getLocationInWindow(xy)
        val dg = Rect(
            xy[0], xy[1],
            (xy[0] + dog.width * 0.8).toInt(), (xy[1] + dog.height * 0.8f).toInt()
        )
        if (Rect.intersects(dg, br)) {
            return true
        }
        return false
    }


    fun avoidFinish() {
        finish = true
        fragmentManager
            ?.beginTransaction()
            ?.add(
                R.id.smash_fragment,
                Avoid3.newInstance(idcount),
            )
            ?.commit()
    }
}