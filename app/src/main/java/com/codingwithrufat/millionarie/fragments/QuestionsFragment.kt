package com.codingwithrufat.millionarie.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingwithrufat.millionarie.R
import com.codingwithrufat.millionarie.adapters.VariantAdapter
import com.codingwithrufat.millionarie.dialogs.showAuditoryChooseDialog
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
    MediaPlayer.OnCompletionListener {

    private val variant_type: List<String> = listOf("A:", "B:", "C:", "D:")
    private lateinit var questions: ArrayList<Question>

    private lateinit var question: Question

    // variables
    private var i: Int = 100
    private var COUNTDOWN_TIME = 60
    private var question_number = 1
    private var usedHelp_50_50: Boolean = false
    private var usedHelpPhone: Boolean = false
    private var usedHelpAuditory: Boolean = false

    // coroutine
    var timeJob: Job? = null

    // musics
    private lateinit var correct_mediaPlayer: MediaPlayer
    private lateinit var main_mediaPlayer: MediaPlayer
    private lateinit var wrong_mediaPlayer: MediaPlayer
    private lateinit var final_answer_mediaPlayer: MediaPlayer

    // objects
    private lateinit var variantAdapter: VariantAdapter
    private var moneyScheduleFragment: MoneyScheduleFragment = MoneyScheduleFragment()
    private lateinit var textAnimation: Animation
    private lateinit var recyclerViewAnimation: LayoutAnimationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        correct_mediaPlayer = MediaPlayer.create(requireActivity(), R.raw.correct_answer)
        wrong_mediaPlayer = MediaPlayer.create(requireActivity(), R.raw.wrong_answer)
        main_mediaPlayer = MediaPlayer.create(requireActivity(), R.raw.hundred_7thousend5hundred)
        final_answer_mediaPlayer = MediaPlayer.create(requireActivity(), R.raw.final_answer)
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
        customTimerCoroutine(progress = 100, time = 30)

        loadAnimation(view)

        setupMediaPlayer()

        clickedShapeableMoneyBox(view)

        clickedIcFiftyFiftyHelp(view)
        clickedIcAuditoryHelp(view)

        clickedPhoneHelp(view)

        return view
    }

    private fun setupMediaPlayer() {
        main_mediaPlayer.isLooping = true
        main_mediaPlayer.start()

        correct_mediaPlayer.setOnCompletionListener(this)
        wrong_mediaPlayer.setOnCompletionListener(this)

    }

    private fun clickedIcFiftyFiftyHelp(view: View) {

        view.ic_50_50.setOnClickListener {
            if (!usedHelp_50_50 && !usedHelpAuditory && !usedHelpPhone) {
                var correct_variant = question.correct!!
                var variantList = mutableListOf(0, 1, 2, 3)
                variantList.remove(correct_variant)
                var random_variant = variantList.random()
                variantAdapter.updateVariants(correct_variant, random_variant)
                view.ic_50_50_cancel.visibility = View.VISIBLE
                view.ic_50_50.isClickable = false
                usedHelp_50_50 = true
            }
        }
    }

    private fun clickedIcAuditoryHelp(view: View) {

        view.ic_auditory.setOnClickListener {
            if (!usedHelp_50_50 && !usedHelpAuditory && !usedHelpPhone) {
                val correct = (50..80).random()
                val other_variant1 = (6..((100 - correct) / 2)).random()
                val other_variant2 = (1..(100 - (correct + other_variant1))).random()
                val other_variant3 = 100 - (correct + other_variant1 + other_variant2)
                when (question.correct) {
                    0 -> {
                        showAuditoryChooseDialog(
                            requireContext(),
                            correct,
                            other_variant1,
                            other_variant2,
                            other_variant3
                        )
                    }
                    1 -> {
                        showAuditoryChooseDialog(
                            requireContext(),
                            other_variant1,
                            correct,
                            other_variant2,
                            other_variant3
                        )
                    }
                    2 -> {
                        showAuditoryChooseDialog(
                            requireContext(),
                            other_variant1,
                            other_variant2,
                            correct,
                            other_variant3
                        )
                    }
                    3 -> {
                        showAuditoryChooseDialog(
                            requireContext(),
                            other_variant1,
                            other_variant2,
                            other_variant3,
                            correct
                        )
                    }
                }

                view.ic_auditory_cancel.visibility = View.VISIBLE
                view.ic_auditory.isClickable = false
                usedHelpAuditory = true
            }
        }
    }

    private fun clickedPhoneHelp(view: View) {


        view.ic_phone.setOnClickListener {
            if (!usedHelp_50_50 && !usedHelpAuditory && !usedHelpPhone) {
                variantAdapter.showOnlyCorrectVariant()
                view.ic_phone_cancel.visibility = View.VISIBLE
                view.ic_phone.isClickable = false
                usedHelpPhone = true
            }
        }

    }

    private fun clickedShapeableMoneyBox(view: View) {

        view.money_shapeable.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, moneyScheduleFragment, "money_schedule")
                .addToBackStack("questions")
                .commit()
        }

    }

    private fun customTimerCoroutine(progress: Int, time: Int) {
        i = progress
        COUNTDOWN_TIME = time
        var divided_time: Int = COUNTDOWN_TIME.div(10)
        timeJob = CoroutineScope(Main).launch {
            repeat(COUNTDOWN_TIME * 10) {
                if (it % 10 == 0) {
                    COUNTDOWN_TIME--
                    view?.txt_time?.text = COUNTDOWN_TIME.toString()

                    if (COUNTDOWN_TIME == 0) {
                        main_mediaPlayer.stop()
                        wrong_mediaPlayer.start()
                    }

                }
                if (it.rem(divided_time) == 0) {
                    i--
                }
                view?.progress?.progress = i
                delay(100L)
            }
        }
    }

    private fun setWidgets(view: View) {
        view.txtQuestion.text = question.question
        view.txt_questionPoint.text = returnMoneyList()[returnMoneyList().size - question_number]
    }

    private fun loadAnimation(view: View) {

        textAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_anim)
        recyclerViewAnimation =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_anim)
        view.txtQuestion.startAnimation(textAnimation)
        view.recyclerVariants.layoutAnimation = recyclerViewAnimation

    }

    private fun setupAdapter(view: View) {

        view.recyclerVariants.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        question = questions.random()
        questions.remove(question)
        variantAdapter = VariantAdapter(requireContext(), variant_type, question, this, null, null)

        view.recyclerVariants.adapter = variantAdapter

    }

    private fun sendQuestionNumber(number: Int) {
        var args = Bundle()
        args.putInt("question_number", number)
        moneyScheduleFragment.arguments = args
    }

    override fun onCompletion(mp: MediaPlayer?) {

        if (question_number < 15) {

            main_mediaPlayer.stop()
            main_mediaPlayer.release()
            when {
                question_number < 5 -> {
                    main_mediaPlayer =
                        MediaPlayer.create(requireActivity(), R.raw.hundred_7thousend5hundred)
                }
                question_number in 5..9 -> {
                    main_mediaPlayer = MediaPlayer.create(requireActivity(), R.raw.to25thousend)
                }
                question_number in 10..11 -> {
                    main_mediaPlayer = MediaPlayer.create(requireActivity(), R.raw.to100thousend)
                }
                question_number == 12 -> {
                    main_mediaPlayer = MediaPlayer.create(requireActivity(), R.raw.music250thousend)
                }
                question_number == 13 -> {
                    main_mediaPlayer = MediaPlayer.create(requireActivity(), R.raw.music500thousend)
                }
                question_number == 14 -> {
                    main_mediaPlayer = MediaPlayer.create(requireActivity(), R.raw.music1million)
                }
            }

            main_mediaPlayer.isLooping = true
            main_mediaPlayer.start()

            when (mp) {
                correct_mediaPlayer -> {

                    question = questions.random()
                    questions.remove(question)
                    variantAdapter =
                        VariantAdapter(requireContext(), variant_type, question, this, null, null)
                    view?.recyclerVariants?.adapter = variantAdapter
                    view?.let {
                        setWidgets(it)
                        loadAnimation(it)
                    }

                    question_number += 1
                    view?.txt_questionPoint?.text =
                        returnMoneyList()[returnMoneyList().size - question_number]

                    sendQuestionNumber(question_number)

                    if (question_number <= 5) {
                        customTimerCoroutine(100, 30)
                    } else {
                        customTimerCoroutine(100, 60)
                    }

                }
                wrong_mediaPlayer -> {
                    main_mediaPlayer.stop()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, HomeFragment(), "home")
                        .commit()
                }
            }
            // we reset all helps again for the each question
            usedHelpPhone = false
            usedHelpAuditory = false
            usedHelp_50_50 = false

        } else {
            // TODO In here congratulation page is opened
        }
    }

    override fun onClickedVariantCallBack(check: Boolean) {
        timeJob?.cancel()
        main_mediaPlayer.stop()
        final_answer_mediaPlayer.start()
        final_answer_mediaPlayer.setOnCompletionListener {
            if (check) {
                correct_mediaPlayer.start()
            } else {
                wrong_mediaPlayer.start()
            }
        }
    }

}