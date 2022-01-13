package com.tuwiaq.projectgame.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tuwiaq.projectgame.R
import com.tuwiaq.projectgame.model.MainViewModel


class LvlFragment : Fragment() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var savedScore: TextView

    private val vm by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private lateinit var cardNum: NumberOfCard

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_lvl_one, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lvl: LvlFragmentArgs by navArgs()
        val level = lvl.lvl.toNumberOfCard()
        cardNum = level
        val levelHeight = level.getHeight()

        recyclerView = view.findViewById(R.id.rvRecycleView)

        savedScore = view.findViewById(R.id.count)


        recyclerView.setHasFixedSize(true)


        savedScore.text = vm.gameScore.toString()

        recyclerView.layoutManager = GridLayoutManager(context, cardNum.getWidth())
        vm.fetchIntrestingList(levelHeight / 2).observe(viewLifecycleOwner) {

            recyclerView.adapter = GameAdapter(
                requireContext(),
                levelHeight,
                it.photos.photo,
                object : GameAdapter.CardClickListner {
                    override fun onClick(position: Int, applicationContext: Context) {
                        savedScore.text = vm.gameScore.toString()
                    }

                    override fun onWin(gg: Boolean) {
                     if (gg){
                       if (levelHeight == 8){
                           val action = LvlFragmentDirections.actionLvlOneFragmentSelf(NumberOfCard.MEDIUM.numberOfCardToString())
                           findNavController().navigate(action)

                       }else if(levelHeight == 16){
                           val action = LvlFragmentDirections.actionLvlOneFragmentSelf(NumberOfCard.HARD.numberOfCardToString())
                           findNavController().navigate(action)

                       }else{

                         findNavController().navigate(R.id.action_lvlOneFragment_to_mainFragment)

                       }
                     }
                    }
                },cardNum,vm1 = vm
            )

        }
    }
}
