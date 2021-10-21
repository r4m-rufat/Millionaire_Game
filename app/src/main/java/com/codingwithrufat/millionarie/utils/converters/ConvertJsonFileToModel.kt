package com.codingwithrufat.millionarie.utils.converters

import android.content.Context
import com.codingwithrufat.millionarie.models.Question
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.io.IOException

fun convertJsonFileToModel(context: Context): ArrayList<Question>{

    var inputStream = ""

    try {
        inputStream = context.assets.open("questions.json").bufferedReader().use {
            it.readText()
        }
    }catch (e: IOException){
        e.printStackTrace()
    }

    val gson = Gson()
    val questionList = object : TypeToken<List<Question>>() {}.type

    var questions: ArrayList<Question> = gson.fromJson(inputStream, questionList)

    return questions

}