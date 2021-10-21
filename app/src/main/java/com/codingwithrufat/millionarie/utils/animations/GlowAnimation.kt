package com.codingwithrufat.millionarie.utils.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.delay


@SuppressLint("ObjectAnimatorBinding")
suspend fun Any.correctGlowAnimation(animatorSet: AnimatorSet) {

    val fadeOut = ObjectAnimator.ofFloat(this, "alpha", 1f, 0.5f)
    fadeOut.duration = 400

    val fadeIn = ObjectAnimator.ofFloat(this, "alpha", 0.5f, 1f)
    fadeIn.duration = 400

    animatorSet.play(fadeIn).after(fadeOut)

    animatorSet.start()

    /**
     * after 900 ms animation start again
     * and this causes animation repeat
      */
    delay(900L)
    animatorSet.start()
}

@SuppressLint("ObjectAnimatorBinding")
suspend fun Any.falseGlowAnimation(animatorSet: AnimatorSet) {

    val fadeOut = ObjectAnimator.ofFloat(this, "alpha", 1f, 0.5f)
    fadeOut.duration = 300

    val fadeIn = ObjectAnimator.ofFloat(this, "alpha", 0.5f, 1f)
    fadeIn.duration = 300

    animatorSet.play(fadeIn).after(fadeOut)

    animatorSet.start()

    /**
     * after 900 ms animation start again
     * and this causes animation repeat
     */
    delay(700L)
    animatorSet.start()
    delay(700L)
    animatorSet.start()
}