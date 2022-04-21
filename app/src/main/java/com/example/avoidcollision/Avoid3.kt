package com.example.avoidcollision

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Avoid3 : Fragment() {
    private val pong = "result"
    private var pongResult: Int? = null
    var pongCurrent = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pongResult = it.getInt("result")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_avoid3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (pongResult != null)
            pongCurrent = pongResult!!

        val shared_record: SharedPreferences = this.requireActivity().getSharedPreferences(
            "bestResult",
            AppCompatActivity.MODE_PRIVATE
        )

        val tv_current = view.findViewById<TextView>(R.id.tv_cur)
        val tv_bestrecord = view.findViewById<TextView>(R.id.best)

        if (readPong(shared_record)?.toInt()!! <= pongCurrent) {
            writePong(pongCurrent.toString(), shared_record)
        }

        tv_current.text = pongCurrent.toString()
        tv_bestrecord.text = readPong(shared_record).toString()

        view.findViewById<Button>(R.id.button_again).setOnClickListener{
            val fm = fragmentManager
            fm!!.beginTransaction()
                .replace(R.id.smash_fragment, Avoid2())
                .remove(this)
                .commit()
        }

        view?.findViewById<Button>(R.id.button_menu).setOnClickListener{
            val fm = fragmentManager
            fm!!.beginTransaction()
            fm.popBackStack()
        }
    }

    fun writePong(current: String, record: SharedPreferences) {
        val edt: SharedPreferences.Editor = record.edit()
        edt.putString("bestResult", current)
        edt.apply()
    }

    fun readPong(record: SharedPreferences): String? {
        val gtrec = record.getString("bestResult", "0")
        return gtrec
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            Avoid3().apply {
                arguments = Bundle().apply {
                    putInt(pong, param1)
                }
            }
    }
}