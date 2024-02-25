package com.lonewolf.ghwedey.resources

import android.widget.ProgressBar
import okhttp3.OkHttpClient
import okhttp3.Request

object API {

    fun getUrban(search: String): String? {
        var res ="[]"

            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://mashape-community-urban-dictionary.p.rapidapi.com/define?term=${search}")
                .get()
                .addHeader("X-RapidAPI-Key", "6f1121b31amsh09c0b8a1212bdfdp18ac72jsn592c77fcd053")
                .addHeader("X-RapidAPI-Host", "mashape-community-urban-dictionary.p.rapidapi.com")
                .build()

            val response = client.newCall(request).execute()
           res = response.body?.string().toString()
//        println("wres : "+response.body?.string().toString())
        return res


    }


}