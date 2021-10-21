package com.codingwithrufat.millionarie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codingwithrufat.millionarie.fragments.HomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, HomeFragment(), "home")
            .commit()

    }

}