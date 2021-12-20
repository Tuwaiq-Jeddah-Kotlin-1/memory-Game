package com.tuwiaq.projectgame.ui

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tuwiaq.projectgame.R
import com.tuwiaq.projectgame.data.FlickerPhoto
import kotlinx.coroutines.*
import kotlin.math.log

//var context: Context?
class GameAdapter(
    var context: Context,
    var level: Int,
    val cards: List<FlickerPhoto>,
    private var cardClickListner: CardClickListner,
    val cardNum: NumberOfCard,
) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    /* val ar1:MutableList<FlickerPhoto> = cards.shuffled().toMutableList()
     val ar2:MutableList<FlickerPhoto> = cards.shuffled().toMutableList()
     var ar3:MutableList<FlickerPhoto> = (ar1+ar2).toMutableList()*/
    //var ar4 = ar3.flatten()
    //var cards: List<FlickerPhoto>

    val ar000 = mutableListOf<FlickerPhoto>()
    private lateinit var sharedPreferences: SharedPreferences

    init {
        var ar0 = cards.shuffled()
        var ar01 = ar0.shuffled()
        ar000.addAll(ar0)
        ar000.addAll(ar01)
    }


    companion object {
        private const val Tag = "GameAdapter"
        private const val MARGIN_SIZE = 10
        var answerList = mutableListOf<String>()
        var answer = ""
        var any = mutableListOf<Any>()
        var isClicked = false
        var hardTime = mutableListOf<Any>()
        const val STOCK_SHARED_KEY = "lastStockValue"
        var score = 0


    }

    interface CardClickListner {
        fun onClick(position: Int, applicationContext: Context)
    }

    inner class ViewHolder(itemView: View, private var applicationContext: Context) :
        RecyclerView.ViewHolder(itemView) {
        /* init {
              binding.photo1 = PhotosViewModel()
          }*/
        //  var getScore = itemView.findViewById<TextView>(R.id.scoreGet)
        var image = itemView.findViewById<ImageButton>(R.id.image_btn)
        var title = itemView.findViewById<TextView>(R.id.title)
        var frontImage = itemView.findViewById<ImageButton>(R.id.image_front)
        val savedScore = itemView.findViewById<TextView>(R.id.count)
        private lateinit var front_animato: AnimatorSet
        private lateinit var back_animato: AnimatorSet
        var isFront = true
        val isFrontList: MutableList<Boolean> = mutableListOf(isFront)


        //        var isClicable = false
        var clicked = 0
        var turnOver = false
        var lastClicked = -1
        val scale: Float = applicationContext.resources.displayMetrics.density


        @SuppressLint("CommitPrefEdits")
        fun bind(photo: FlickerPhoto?, position: Int) {
            var id: Int
            val isFaceUp: Boolean = false
            var isMatch: Boolean = false
            image.load(photo?.url)
            title.text = photo?.title


            val sp: SharedPreferences = context.getSharedPreferences("key", 0)

            fun imageAnimation(
                image: ImageButton,
                frontImage: ImageButton,
                isFront: MutableList<Boolean>
            ) {

                front_animato = AnimatorInflater.loadAnimator(
                    applicationContext,
                    R.animator.front_animator1
                ) as AnimatorSet
                back_animato = AnimatorInflater.loadAnimator(
                    applicationContext,
                    R.animator.back_animator
                ) as AnimatorSet

                Log.e("Tag", "memoryGame $frontImage")

                if (isFront[0]) {
                    front_animato.setTarget(frontImage)
                    back_animato.setTarget(image)
                    front_animato.start()
                    back_animato.start()
                    isFront[0] = false

                } else {
                    front_animato.setTarget(image)
                    back_animato.setTarget(frontImage)
                    back_animato.start()
                    front_animato.start()
                    isFront[0] = true
                }
            }
            //---------------------------------------
            hardTime.apply {
                add(image)
                add(frontImage)
                add(isFrontList)
            }
            //--------------------------------------------

            image.setOnClickListener {
                if (isClicked) {

                } else {
                    when {
                        answerList.contains(title.text) -> {

                        }
                        answer.isEmpty() -> {
                            any.add(0, image)
                            any.add(1, frontImage)
                            any.add(2, isFrontList)
                            answer = title.text.toString()
                            imageAnimation(image, frontImage, isFrontList)
                        }
                        answer == title.text && image == any[0] -> {
//                            imageAnimation(image, frontImage, isFrontList)
                            //   answer = ""
                        }
                        answer == title.text && image != any[0] -> {
                            answerList.add(answer)
                            for (i in 0..answerList.lastIndex step(1)){
                                score += 50
                                // savedScore.text = score.toString()
                                val sharedPreference = context.getSharedPreferences(
                                    STOCK_SHARED_KEY,
                                    Context.MODE_PRIVATE
                                )
                                var editor = sharedPreference.edit()
                                editor.putInt("username", score)
                                editor.apply()
                                Log.e("this is adapter score", score.toString())

                            }
                            answer = ""
                            imageAnimation(image, frontImage, isFrontList)
                        }
                        else -> {
                            isClicked = !isClicked
                            answer = ""
                            imageAnimation(image, frontImage, isFrontList)
                            GlobalScope.launch(Dispatchers.Main) {
                                delay(1000)
                                imageAnimation(image, frontImage, isFrontList)
                                any[0].apply {
                                    imageAnimation(
                                        this as ImageButton,
                                        any[1] as ImageButton,
                                        any[2] as MutableList<Boolean>
                                    )
                                }
                                isClicked = !isClicked
                            }

                            cardClickListner.onClick(position, context)
                        }
                    }
                }
            }

            // var boardsize:MemoCard

            //ar3 = cards

            /*   image.setOnClickListener {
                   Log.i(Tag, "afapte $position")
                   cardClickListner.onClick(position,)
               }*/
            /*     for (i in position.until(-1)) {
                           title.textSize = 0.0f
                     image.setOnClickListener {
                         if (title.text == title.text){
                             front_animato.setTarget(frontImage)
                             back_animato.setTarget(image)
                             front_animato.start()
                             back_animato.start()
                             isFront = false
                         }
                         if (clicked == 0) {
                             lastClicked = i
                         }
                         clicked++
                     }



                 }*/


/*
              Log.i("Tag1111", "shaffles $ar3")
              // val cardBack = boardgames
              front_animato = AnimatorInflater.loadAnimator(
                  applicationContext,
                  R.animator.front_animator1
              ) as AnimatorSet
              back_animato = AnimatorInflater.loadAnimator(
                  applicationContext,
                  R.animator.back_animator
              ) as AnimatorSet

              var clicked = 0
              var turnOver = false
              var lastClicked = -1
                  image.setOnClickListener {
                      Log.e("Tag", "memoryGame $frontImage")
                      if (title.text == title.text) {
                          if(clicked == 0){

                          }
                          if (isFront) {
                              front_animato.setTarget(frontImage)
                              back_animato.setTarget(image)
                              front_animato.start()
                              back_animato.start()
                              isFront = false



                              *//*   if (title.text == title.text){
                          title = title
                          image.isClickable = false
                      }*//*

                          } else {
                              front_animato.setTarget(image)
                              back_animato.setTarget(frontImage)
                              back_animato.start()
                              front_animato.start()
                              isFront = true
                          }


                      }
                      Log.e("Tag", "i am inside adapter ${frontImage.background}")

                  }*/
            if (position == (level - 1)) {
                isClicked = true
                GlobalScope.launch(Dispatchers.Main) {
                    for (index in 0..hardTime.lastIndex step (3)) {
                        GlobalScope.launch(Dispatchers.Main) {
                            delay(1000)
                            hardTime[index].apply {
                                imageAnimation(
                                    this as ImageButton,
                                    hardTime[index + 1] as ImageButton,
                                    hardTime[index + 2] as MutableList<Boolean>
                                )
                            }
                            delay(3000)
                            hardTime[index].apply {
                                imageAnimation(
                                    this as ImageButton,
                                    hardTime[index + 1] as ImageButton,
                                    hardTime[index + 2] as MutableList<Boolean>
                                )
                            }
                        }
                    }
                    delay(5000)
                    isClicked = false
                }
            }
            // Extreme Hard Level --------------------------------------------
            /*
            if(position == (level-1)) {
                isClicked = !isClicked
                GlobalScope.launch(Dispatchers.Main) {
                for (index in 0..hardTime.lastIndex step(3) ) {
//                        delay(500)
                        hardTime[index].apply {
                            imageAnimation(
                                this as ImageButton,
                                hardTime[index+1] as ImageButton,
                                hardTime[index+2] as MutableList<Boolean>
                            )
                        }
                        delay(2000)
                        hardTime[index].apply {
                            imageAnimation(
                                this as ImageButton,
                                hardTime[index+1] as ImageButton,
                                hardTime[index+2] as MutableList<Boolean>
                            )
                        }

                    }
                    isClicked = !isClicked
                }
            }
            */
            //--------------------------------------------------------------------
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /*val width = parent.width / 2 - (2 * MARGIN_SIZE)
        val height = parent.height / 4 - (2 * MARGIN_SIZE)
        var onSide = min(width, height)*/
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_view, parent, false)
/*        var pram =
            view.findViewById<CardView>(R.id.cardView).layoutParams as ViewGroup.MarginLayoutParams
        pram.width = onSide
        pram.height = onSide
        pram.setMargins(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE)*/
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        var photo = ar000[position]
        holder.image.load(photo.url)
        holder.title.text = photo.title
        holder.bind(photo, position)


        /* var imag1 = cards.take(cardNum.numParis())
         var random = (imag1 + imag1)*/
        // random.map { MemoCard() }

    }

    override fun getItemCount(): Int = level


}


/*val p = binding.imageBtn.load(photo?.url)
         binding.photo1?.vmPhoto = photo*/

// binding.imageBtn.setImageResource(R.drawable.ic_launcher_background)
// binding.imageBtn

// image.setImageResource(photo[position])
// image.setImageResource(if (cards[position].isFaceUp)cards[position].id else R.drawable.ic_launcher_background)
// image.alpha = if (cards[position].isMatch) .4f else 1.0f
//  if (cards[position].isMatch) ContextCompat.getColor(context!!,R.color.white) else null
//1 : 30:38

/* image.setOnClickListener {
     Log.i(Tag,"Click on position $position")
 }*/
