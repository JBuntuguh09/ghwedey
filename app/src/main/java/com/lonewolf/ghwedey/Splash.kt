package com.lonewolf.ghwedey

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.lonewolf.ghwedey.databinding.ActivitySplashBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat

class Splash : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    var prog = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navTo()
    }

    private  fun navTo() {

        moveProg()
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(binding.materialCardView)
        YoYo.with(Techniques.BounceInRight).delay(1000).duration(3000).playOn(binding.txtGh)
        YoYo.with(Techniques.BounceInLeft).delay(1000).duration(3000).playOn(binding.txtWeDey)


    }
    private fun moveProg(){
        val selTime = 4000

        val cDown= object : CountDownTimer(selTime.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Used for formatting digit to be in 2 digits only
                prog +=25
                binding.progressBar.setProgress(prog)
                 println(prog)

            }

            // When the task is over it will print 00:00:00 there
            override fun onFinish() {
                val intent = Intent(this@Splash, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        cDown.start()

    }
}