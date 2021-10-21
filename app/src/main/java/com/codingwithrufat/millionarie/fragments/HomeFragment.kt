package com.codingwithrufat.millionarie.fragments

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainerView
import com.codingwithrufat.millionarie.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private lateinit var main_soundPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        soundSetup()

        view.rel_play.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, QuestionsFragment(), "questions")
                .commit()
        }

        return view
    }

    private fun soundSetup(){
        main_soundPlayer = MediaPlayer.create(requireContext(), R.raw.main_music)
        main_soundPlayer.isLooping = true
        main_soundPlayer.start()
    }

    override fun onDetach() {
        super.onDetach()
        main_soundPlayer.stop()
        main_soundPlayer.release()
    }
}