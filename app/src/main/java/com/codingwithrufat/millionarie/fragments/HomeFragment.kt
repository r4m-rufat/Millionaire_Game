package com.codingwithrufat.millionarie.fragments

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainerView
import com.codingwithrufat.millionarie.R
import com.codingwithrufat.millionarie.utils.default.returnMoneyList
import com.codingwithrufat.millionarie.utils.internal_storage.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private lateinit var main_soundPlayer: MediaPlayer
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        preferenceManager = PreferenceManager(requireContext())

        soundSetup()
        setHighScore(view)

        view.rel_play.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, QuestionsFragment(), "questions")
                .commit()
        }

        return view
    }

    private fun setHighScore(view: View){
        try {
            view.txt_highScore.text = returnMoneyList()[returnMoneyList().size - preferenceManager.getInt("high_score")]
        }catch (e: IndexOutOfBoundsException){
            view.txt_highScore.text = "$ 0"
        }
    }

    private fun soundSetup(){
        main_soundPlayer = MediaPlayer.create(requireContext(), R.raw.main_music)
        main_soundPlayer.isLooping = true
        main_soundPlayer.start()
    }

    override fun onStop() {
        super.onStop()
        main_soundPlayer.stop()
    }

    override fun onResume() {
        super.onResume()
        if (!main_soundPlayer.isPlaying){
            main_soundPlayer.start()
        }
    }

}