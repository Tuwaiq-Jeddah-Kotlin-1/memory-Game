package com.tuwiaq.projectgame.ui

import android.animation.AnimatorSet
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tuwiaq.projectgame.R
import com.tuwiaq.projectgame.data.FlickerPhoto
import com.tuwiaq.projectgame.model.MainViewModel
import com.tuwiaq.projectgame.ui.GameAdapter.Companion.STOCK_SHARED_KEY


class LvlFragment : Fragment() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var yvAdapter: GameAdapter
    private lateinit var itCard: List<FlickerPhoto>
    private lateinit var viewModel: MainViewModel
 //   private lateinit var m1: MemoryGame
    private lateinit var bored: NumberOfCard
    private lateinit var adapter: GameAdapter
    private lateinit var image: ImageView
    private lateinit var frontImage: ImageView
    private lateinit var front_animato: AnimatorSet
    private lateinit var back_animato: AnimatorSet
    private lateinit var savedScore: TextView
    private lateinit var sharedPreference: SharedPreferences

/*    private lateinit var cardScreen: RecyclerView
    private lateinit var playerTime:TextView
    private lateinit var playerScore:TextView*/

    private val vm by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    interface MyCallback {
        // Declaration of the template function for the interface
        fun updateMyText(myString: String?)
    }

    companion object {
        private const val Tag = "MainActivity"
    }


    private lateinit var cardNum: NumberOfCard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_lvl_one, container, false)
        // binding.rvRecycleView.adapter = GameAdapter(this)


        // binding = vm//attach your viewModel to xml


        return view
        // binding = DataBindingUtil.getBinding<FragmentLvlBinding>(this,)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lvl: LvlFragmentArgs by navArgs()
        var level = lvl.lvl.toNumberOfCard()
        cardNum = level
        var levelHeight = level.getHeight()

        //    val bored1 = bored


        /*var adapter =GameAdapter()
        recyclerView.adapter = adapter*/

        recyclerView = view.findViewById(R.id.rvRecycleView)

        /*  if (recyclerView != null) {
              val layoutManager = StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL)
              layoutManager.gapStrategy =
                  StaggeredGridLayoutManager.GAP_HANDLING_NONE
              recyclerView.setLayoutManager(layoutManager)
              recyclerView.setItemAnimator(DefaultItemAnimator())
          }*/
        //    val kk  = MemoryGame(cardNum)
        // recyclerView.adapter = context?.let { GameAdapter(it,8) }
        // m1 = MemoryGame(cardNum)
        savedScore = view.findViewById(R.id.count)
        // val kkk1 = sharedPreferences.getInt("id_key",0)
        val sharedPreference =
            context!!.getSharedPreferences(STOCK_SHARED_KEY, Context.MODE_PRIVATE)
        //sharedPreference.getInt("username", savedScore.text.toString()).toString()


        recyclerView.setHasFixedSize(true)


        recyclerView.layoutManager = GridLayoutManager(context, cardNum.getWidth())
        vm.fetchIntrestingList(levelHeight / 2).observe(viewLifecycleOwner) {
            recyclerView.adapter = GameAdapter(
                requireContext(),
                levelHeight,
                it.photos.photo,
                object : GameAdapter.CardClickListner {
                    override fun onClick(position: Int, applicationContext: Context) {
                        image = view.findViewById<ImageButton>(R.id.image_btn)
                        savedScore.text = sharedPreference.getInt("username", 0).toString()

                        Log.e("lvl fragment", savedScore.toString())

                    }


                }, cardNum
            )


        }
    }
}
