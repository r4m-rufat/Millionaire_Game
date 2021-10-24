package com.codingwithrufat.millionarie.dialogs

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.animation.AnimationUtils
import com.codingwithrufat.millionarie.R
import kotlinx.android.synthetic.main.dialog_for_auditory_choose.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun showAuditoryChooseDialog(
    context: Context,
    progressA: Int,
    progressB: Int,
    progressC: Int,
    progressD: Int,
){

    val dialog = Dialog(context, R.style.DialogStyle)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.dialog_for_auditory_choose)

    dialog.progressBarA.progress = progressA
    dialog.progressBarB.progress = progressB
    dialog.progressBarC.progress = progressC
    dialog.progressBarD.progress = progressD

    var a = 1
    var b = 1
    var c = 1
    var d = 1

    CoroutineScope(Main).launch {
        repeat(progressA){
            dialog.progressBarA.progress = a
            dialog.percentA.text = "$a%"
            a++
            delay(20L)
        }

    }

    CoroutineScope(Main).launch {

        repeat(progressB){
            dialog.progressBarB.progress = b
            dialog.percentB.text = "$b%"
            b++
            delay(30L)
        }

    }

    CoroutineScope(Main).launch {

        repeat(progressC){
            dialog.progressBarC.progress = c
            dialog.percentC.text = "$c%"
            c++
            delay(35L)
        }

    }

    CoroutineScope(Main).launch {

        repeat(progressD){
            dialog.progressBarD.progress = d
            dialog.percentD.text = "$d%"
            d++
            delay(30L)
        }

    }

    dialog.show()

}