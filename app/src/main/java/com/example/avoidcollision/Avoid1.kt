package com.example.avoidcollision

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class Avoid1 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_avoid1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tab_play = view?.findViewById<TextView>(R.id.tap_play)

        val ls = ValueAnimator.ofFloat(1f, 0f)
            .apply {
                duration = 1000
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
                interpolator = LinearInterpolator()
            }
        ls.addUpdateListener { animation ->
            tab_play.alpha = animation.animatedValue as Float
        }

        ls.start()

        view.findViewById<Button>(R.id.button_exit).setOnClickListener {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        view.findViewById<ConstraintLayout>(R.id.frameLayout).setOnClickListener {
            val fm = fragmentManager
            fm!!.beginTransaction()
                .replace(R.id.smash_fragment, Avoid2(), "2")
                .addToBackStack(null)
                .commit()
        }
    }

}