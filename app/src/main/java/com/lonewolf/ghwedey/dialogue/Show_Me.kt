package com.lonewolf.ghwedey.dialogue

import android.app.Activity
import android.app.AlertDialog
import android.text.SpannedString
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.lonewolf.ghwedey.R
import com.lonewolf.ghwedey.databinding.LayoutMoreBinding
import kotlin.math.roundToInt

object Show_Me {
    private lateinit var alertDialog: AlertDialog.Builder
    private lateinit var alert : AlertDialog

    fun more(activity: Activity,linearLayout: LinearLayout, hashMap: HashMap<String, String>){
        val layoutInflater = LayoutInflater.from(activity)
        val view = layoutInflater.inflate(R.layout.layout_more, linearLayout, false)
        val binding = LayoutMoreBinding.bind(view)
        binding.txtTopic.text = hashMap["word"]
        binding.txtMeaning.text = hashMap["meaning"]
        binding.txtDate.text = "On ${hashMap["date"]}"
        binding.txtPublish.text = "By : ${hashMap["by"]}"

        val string: SpannedString = buildSpannedString {
            bold {
                append("EXAMPLE : ")
            }

            append(hashMap["example"])
        }
        binding.txtExample.text = string
        var tUp = 0
        var tDown = 0

        if(hashMap["tUp"]!!.isNotEmpty()){
            tUp = hashMap["tUp"]!!.toInt()
        }

        if(hashMap["tDown"]!!.isNotEmpty()){
            tDown = hashMap["tDown"]!!.toInt()
        }

        val tTot = tUp+ tDown
        val thumbsUp  = Math.round(((tUp.toFloat()/tTot)*100)*100.0)/100.00
        val thumbsDown = Math.round(((tDown.toFloat()/tTot)*100)*100.0)/100.00

        binding.txtTUp.text = "$thumbsUp%"
        binding.txtTDown.text = "$thumbsDown%"

        alertDialog = AlertDialog.Builder(activity)
        alertDialog.setView(view)
        alert = alertDialog.create()
        alert.show()

    }
}