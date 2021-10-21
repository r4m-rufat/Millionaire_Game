package com.codingwithrufat.millionarie.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.core.animation.addListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingwithrufat.millionarie.R
import com.codingwithrufat.millionarie.adapters.VariantAdapter
import com.codingwithrufat.millionarie.models.Question
import com.codingwithrufat.millionarie.utils.converters.convertJsonFileToModel
import com.codingwithrufat.millionarie.utils.default.returnMoneyList
import kotlinx.android.synthetic.main.fragment_questions.*
import kotlinx.android.synthetic.main.fragment_questions.view.*
import kotlinx.android.synthetic.main.layout_variant_item.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [QuestionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuestionsFragment : Fragment(),
VariantAdapter.OnClickVariantItem,
MediaPlayer.OnCompletionListener{

    private val variant_type: List<String> = listOf("A:", "B:", "C:", "D:")
    private lateinit var questions: ArrayList<Question>

    private lateinit var question: Question

    // variables
    private var i: Int = 100
    private var COUNTDOWN_TIME = 60
    private var question_number = 1

    // coroutine
    var timeJob: Job? = null

    // musics
    private lateinit var correct_mediaPlayer: MediaPlayer
    private lateinit var main_mediaPlayer: MediaPlayer
    private lateinit var wrong_mediaPlayer: MediaPlayer

    // objects
    private lateinit var variantAdapter: VariantAdapter
    private var moneyScheduleFragment: MoneyScheduleFragment = MoneyScheduleFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        correct_mediaPlayer = MediaPlayer.create(requireActivity(), R.raw.correct_answer)
        wrong_mediaPlayer = MediaPlayer.create(requireActivity(), R.raw.wrong_answer)
        main_mediaPlayer = MediaPlayer.create(requireActivity(), R.raw.hundred_thousend)
        questions = convertJsonFileToModel(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_questions, container, false)
        setupAdapter(view)
        setWidgets(view)
        customTimerCoroutine()

        setupMediaPlayer()

        clickedShapeableMoneyBox(view)

        return view
    }

    private fun setupMediaPlayer(){
        main_mediaPlayer.isLooping = true
        main_mediaPlayer.start()

        correct_mediaPlayer.setOnCompletionListener(this)
        wrong_mediaPlayer.setOnCompletionListener(this)

    }

    private fun clickedShapeableMoneyBox(view: View){

        view.money_shapeable.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, moneyScheduleFragment, "money_schedule")
                .commit()
        }

    }

    private fun customTimerCoroutine(){
        i = 100
        COUNTDOWN_TIME = 60
        timeJob = CoroutineScope(Main).launch {
            repeat(600){
                if (it%10==0){
                    COUNTDOWN_TIME--
                    view?.txt_time?.text = COUNTDOWN_TIME.toString()

                    if(COUNTDOWN_TIME==0){
                        main_mediaPlayer.stop()
                        main_mediaPlayer.release()
                        wrong_mediaPlayer.start()
                    }

                }
                if (it%6==0){
                    i--
                }
                view?.progress?.progress = i
                delay(100L)
            }
        }
    }

    private fun setWidgets(view: View){
        view.txtQuestion.text = question.question
        view.txt_questionPoint.text = returnMoneyList()[returnMoneyList().size-question_number]
    }
    
    private fun setupAdapter(view: View){

        view.recyclerVariants.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        question = questions.random()
        questions.remove(question)
        variantAdapter = VariantAdapter(requireContext(), variant_type, question, this)

        view.recyclerVariants.adapter = variantAdapter

    }

    private fun sendQestionNumber(number: Int){
        var args = Bundle()
        args.putInt("question_number", number)
        moneyScheduleFragment.arguments = args
    }



    override fun onCompletion(mp: MediaPlayer?) {

        when(mp){
            correct_mediaPlayer -> {
                question = questions.random()
                questions.remove(question)
                variantAdapter = VariantAdapter(requireContext(), variant_type, question, this)
                view?.recyclerVariants?.adapter = variantAdapter
                view?.let { setWidgets(it) }

                question_number+=1
                view?.txt_questionPoint?.text = returnMoneyList()[returnMoneyList().size-question_number]

                sendQestionNumber(question_number)

                customTimerCoroutine()
            }
            wrong_mediaPlayer -> {
                main_mediaPlayer.stop()
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, HomeFragment(), "home")
                    .commit()
            }
        }

        main_mediaPlayer = MediaPlayer.create(requireActivity(), R.raw.hundred_thousend)
        main_mediaPlayer.isLooping = true
        main_mediaPlayer.start()
    }

    override fun onClickedVariantCallBack(check: Boolean) {
        if (check){
            correct_mediaPlayer.start()
        }else{
            wrong_mediaPlayer.start()
        }
        timeJob?.cancel()
        main_mediaPlayer.stop()
    }

}