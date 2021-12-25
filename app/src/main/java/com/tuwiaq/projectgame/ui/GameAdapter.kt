package com.tuwiaq.projectgame.ui

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.firebase.firestore.FirebaseFirestore
import com.tuwiaq.projectgame.R
import com.tuwiaq.projectgame.data.FlickerPhoto

import kotlinx.coroutines.*
import java.util.logging.Handler

class GameAdapter(
    var context: Context,
    var level: Int,
    val cards: List<FlickerPhoto>,
    private var cardClickListner: CardClickListner,
    val cardNum: NumberOfCard,
) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    val ar000 = mutableListOf<FlickerPhoto>()

    init {
        var ar0 = cards.shuffled()
        var ar01 = ar0.shuffled()
        ar000.addAll(ar0)
        ar000.addAll(ar01)
    }

    companion object {
        var answerList = mutableListOf<String>()
        val Tag = "GameAdapter"
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

        var image = itemView.findViewById<ImageButton>(R.id.image_btn)
        var title = itemView.findViewById<TextView>(R.id.title)
        var frontImage: ImageButton = itemView.findViewById(R.id.image_front)
        private lateinit var front_animato: AnimatorSet
        private lateinit var back_animato: AnimatorSet
        var isFront = true
        val isFrontList: MutableList<Boolean> = mutableListOf(isFront)



        @SuppressLint("CommitPrefEdits")
        fun bind(photo: FlickerPhoto?, position: Int) {
            image.load(photo?.url_s)
            title.text = photo?.title
            val db by lazy { FirebaseFirestore.getInstance() }

                var docRef = db.collection("ImageUrl").document("key")
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            frontImage.load(
                                document.data.toString().replace("{ul=", "").replace("}", "")
                            )
                            println("document.data.toString()")

                            println(document.data.toString().replace("{ul=", "").replace("}", ""))
                        } else {
                            frontImage.setBackgroundResource(R.drawable.backg)
                            Log.d(Tag, "No such document")
                        }
                    }
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
                            for (i in 0..answerList.lastIndex step (1)) {
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
        }




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_view, parent, false)

        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        var photo = ar000[position]
        holder.image.load(photo.url_s)
        holder.title.text = photo.title

        holder.bind(photo, position)


    }

    override fun getItemCount(): Int = level


}


