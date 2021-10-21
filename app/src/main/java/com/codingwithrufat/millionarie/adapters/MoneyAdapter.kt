package com.codingwithrufat.millionarie.adapters

import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.codingwithrufat.millionarie.R
import kotlinx.android.synthetic.main.layout_money_box_item.view.*
import kotlinx.android.synthetic.main.layout_variant_item.view.*
import kotlinx.android.synthetic.main.layout_variant_item.view.shapeImageView

class MoneyAdapter(
    context: Context,
    moneyList: List<String>,
    current_money: Int
) : RecyclerView.Adapter<MoneyAdapter.ViewHolder>() {

    private val context: Context = context
    private val moneyList: List<String> = moneyList
    private val current_money: Int = current_money

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.layout_money_box_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.txt_money.text = moneyList[position]

        if (position == 0){
            holder.shapeableImageView.background =
                ContextCompat.getDrawable(context, R.drawable.background_million_dollor_box)
            holder.txt_money.textSize = 24f
            holder.txt_money.typeface = Typeface.DEFAULT_BOLD
        }

        if (position == current_money) {
            holder.shapeableImageView.background =
                ContextCompat.getDrawable(context, R.drawable.background_selected_variant)
        }

        if (position>current_money){
            holder.shapeableImageView.setBackgroundColor(Color.parseColor("#A6194673"))
        }

    }

    override fun getItemCount(): Int {
        return moneyList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txt_money = itemView.txt_money
        val shapeableImageView = itemView.shapeImageView

    }

}