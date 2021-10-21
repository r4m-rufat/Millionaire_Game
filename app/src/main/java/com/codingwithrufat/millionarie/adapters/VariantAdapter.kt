package com.codingwithrufat.millionarie.adapters

import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.codingwithrufat.millionarie.R
import com.codingwithrufat.millionarie.models.Question
import com.codingwithrufat.millionarie.utils.animations.falseGlowAnimation
import kotlinx.android.synthetic.main.layout_variant_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VariantAdapter(
    context: Context,
    variants: List<String>,
    question: Question,
    listener: OnClickVariantItem
) : RecyclerView.Adapter<VariantAdapter.ViewHolder>() {

    private val context: Context = context
    private val variants: List<String> = variants
    private var question: Question = question
    private val listener = listener
    private var clickedItem = false

    private val TAG = "VariantAdapter"

    private var animatorSet: AnimatorSet = AnimatorSet()

    fun updateQuestion(newQuestion: Question){
        question = newQuestion
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_variant_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: VariantAdapter.ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.txt_variant.text = question.content?.get(position)
        holder.txt_variant_type.text = variants[position]

        holder.shapeableImageView.setOnClickListener {

            if (!clickedItem) {
                holder.shapeableImageView.background =
                    ContextCompat.getDrawable(context, R.drawable.background_selected_variant)
                CoroutineScope(Main).launch {
                    delay(800L)
                    // glow animation is started from here
                    if (position == question.correct) {
                        listener.onClickedVariantCallBack(true)
                        holder.shapeableImageView.falseGlowAnimation(animatorSet)
                    } else {
                        listener.onClickedVariantCallBack(false)
                        holder.shapeableImageView.background =
                            ContextCompat.getDrawable(context, R.drawable.background_false_variant)
                        holder.shapeableImageView.falseGlowAnimation(animatorSet)
                    }
                }
            }

            Log.d(TAG, "onBindViewHolder: Item is $clickedItem")

            clickedItem = true

        }

    }

    override fun getItemCount(): Int {
        if (variants == null) {
            return 0
        }
        return variants.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val shapeableImageView = itemView.shapeImageView
        val txt_variant_type = itemView.variant_type
        val txt_variant = itemView.txt_variant

    }

    interface OnClickVariantItem {
        fun onClickedVariantCallBack(check: Boolean)
    }

}