package com.lonewolf.ghwedey.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.lonewolf.ghwedey.R
import com.lonewolf.ghwedey.dialogue.Show_Me

class RecyclerViewAdapterList(activity : Activity, arrayList: ArrayList<HashMap<String, String>>)
    : RecyclerView.Adapter<RecyclerViewAdapterList.MyHolder>(){

    private var activity: Activity
    private var arrayList: ArrayList<HashMap<String, String>>
    //private var storage: Storage
    init {
        this.arrayList = arrayList
        this.activity = activity
       // storage = Storage(activity)
    }


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val word : TextView
        val meaning : TextView
        val linear : LinearLayout
        val card : MaterialCardView


        init {
            word = itemView.findViewById(R.id.txtWord)
            meaning = itemView.findViewById(R.id.txtMeaning)
            linear = itemView.findViewById(R.id.linMain)
            card = itemView.findViewById(R.id.cardMain)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_list, parent, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val hash = arrayList[position]
        holder.word.text = hash["word"]
        var mean  = hash["meaning"]
        if(mean?.length!! >100){
            mean = hash["meaning"]?.substring(0..100)
            mean = "${mean}...."
        }
        holder.meaning.text = mean

        holder.itemView.setOnClickListener {
            Show_Me.more(activity, holder.linear, hash)
        }

        if(hash["type"].equals("Ghana")){
            holder.card.backgroundTintList = ContextCompat.getColorStateList(activity, R.color.colorPrimary)
            holder.word.setTextColor(ContextCompat.getColorStateList(activity, R.color.black))
            holder.meaning.setTextColor(ContextCompat.getColorStateList(activity, R.color.black))
        }else{
            holder.card.backgroundTintList = ContextCompat.getColorStateList(activity, R.color.colorSecondary)
            holder.word.setTextColor(ContextCompat.getColorStateList(activity, R.color.white))
            holder.meaning.setTextColor(ContextCompat.getColorStateList(activity, R.color.white))
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}