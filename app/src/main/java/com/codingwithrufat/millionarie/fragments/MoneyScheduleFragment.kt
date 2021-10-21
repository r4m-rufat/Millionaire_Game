package com.codingwithrufat.millionarie.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingwithrufat.millionarie.R
import com.codingwithrufat.millionarie.adapters.MoneyAdapter
import com.codingwithrufat.millionarie.utils.default.returnMoneyList
import kotlinx.android.synthetic.main.fragment_money_schedule.view.*

class MoneyScheduleFragment : Fragment() {

    private lateinit var moneyAdapter: MoneyAdapter
    private var current_position: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            current_position = it.getInt("question_number")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_money_schedule, container, false)

        setupAdapter(view)
        return view

    }

    private fun setupAdapter(view: View) {

        moneyAdapter = MoneyAdapter(
            requireContext(),
            returnMoneyList(),
            returnMoneyList().size - current_position
        )

        view.recyclerMoney.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = moneyAdapter
            scrollToPosition(returnMoneyList().size - current_position)
        }

    }

}