package com.codingwithrufat.millionarie.models

data class Question(
	var correct: Int? = null,
	var question: String? = null,
	var content: ArrayList<String>? = null
)

