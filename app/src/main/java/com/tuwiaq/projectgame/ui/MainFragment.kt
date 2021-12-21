package com.tuwiaq.projectgame.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.tuwiaq.projectgame.R
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class MainFragment : Fragment() {
    private lateinit var btn_stage: Button
    private lateinit var sign_in: Button
    private lateinit var btn_hard: ImageButton
    private lateinit var btn_easy: ImageButton
    private lateinit var btn_M: ImageButton
    private lateinit var googleSignInClient: GoogleSignInClient
    val rc_sgin_in = 0

    private lateinit var card: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        btn_stage = view.findViewById(R.id.button2)

        card = view.findViewById(R.id.stage_mode)
        btn_stage.setOnClickListener {
            card.background = resources.getDrawable(R.drawable.trick, null)
            card.visibility = View.VISIBLE

        }
        btn_M = view.findViewById(R.id.medium_lvl)
        btn_M.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToLvlOneFragment(NumberOfCard.MEDIUM.numberOfCardToString())
            findNavController().navigate(action)
        }

        btn_easy = view.findViewById(R.id.easy_lvl)
        btn_easy.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToLvlOneFragment(NumberOfCard.EASY.numberOfCardToString())
            findNavController().navigate(action)
        }
        btn_hard = view.findViewById(R.id.hard_lvl)
        btn_hard.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToLvlOneFragment(NumberOfCard.HARD.numberOfCardToString())
            findNavController().navigate(action)
        }

        sign_in = view.findViewById(R.id.sin)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }!!
        sign_in.setOnClickListener {
            signIn()
        }



        return view
    }

    fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(
            signInIntent, rc_sgin_in
        )
    }


}