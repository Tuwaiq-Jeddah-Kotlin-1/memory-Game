package com.tuwiaq.projectgame.ui

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tuwiaq.projectgame.R
import com.tuwiaq.projectgame.data.FlickerPhoto
import com.tuwiaq.projectgame.model.MainViewModel

import kotlinx.coroutines.*

class GameAdapter(
    var context: Context,
    var level: Int,
    val cards: List<FlickerPhoto>,
    private var cardClickListner: CardClickListner,
    val cardNum: NumberOfCard,val vm1:MainViewModel
) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {


    val ar000 = mutableListOf<FlickerPhoto>()
    var correctCount = 0
    val maxCorrect = cards.size
    var answerList = mutableListOf<String>()
    var any = mutableListOf<Any>()
    var hardTime = mutableListOf<Any>()
    var answer = ""
    var score = 0
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var  mediaPlayer1: MediaPlayer
    private lateinit var mediaPlayer2: MediaPlayer




    init {
        val ar0 = cards.shuffled()
        val ar01 = ar0.shuffled()
        ar000.addAll(ar0)
        ar000.addAll(ar01)

    }

    companion object {
        var isClicked = false
        const val STOCK_SHARED_KEY = "lastStockValue"
    }

    interface CardClickListner {
        fun onClick(position: Int, applicationContext: Context)
        fun onWin(gg:Boolean)
    }

    inner class ViewHolder(itemView: View, private var applicationContext: Context) :
        RecyclerView.ViewHolder(itemView) {

        val image = itemView.findViewById<ImageButton>(R.id.image_btn)
        val title = itemView.findViewById<TextView>(R.id.title)
        val frontImage: ImageButton = itemView.findViewById(R.id.image_front)
        private lateinit var front_animato: AnimatorSet
        private lateinit var back_animato: AnimatorSet
        private lateinit var firebase: FirebaseAuth

        var isFront = true
        val isFrontList: MutableList<Boolean> = mutableListOf(isFront)

        @SuppressLint("CommitPrefEdits")
        fun bind(photo: FlickerPhoto?, position: Int) {
            image.load(photo?.url_s)
            title.text = photo?.title
            val db by lazy { FirebaseFirestore.getInstance() }
            firebase = FirebaseAuth.getInstance()

            var docRef = firebase.currentUser?.uid?.let { db.collection("ImageUrl").document(it) }
            if (docRef != null) {
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            frontImage.load(
                                document.data.toString().replace("{ul=", "").replace("}", "")
                            )

                            println(document.data.toString().replace("{ul=", "").replace("}", ""))
                        } else {
                            frontImage.setBackgroundResource(R.drawable.backg)
                        }
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
                            answer = ""
                            imageAnimation(image, frontImage, isFrontList)
                            mediaPlayer =  MediaPlayer.create(context,R.raw.wincard)
                            mediaPlayer.start()
                            score += 50
                            vm1.gameScore = score

                            cardClickListner.onClick(position, context)
                            correctCount++
                            if (correctCount == maxCorrect) {
                                vm1.saveScore(score)
                                 mediaPlayer1 = MediaPlayer.create(context,R.raw.win)
                                mediaPlayer1.start()
                                dialogWin(it)



                                Toast.makeText(context, "YOU WON", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else -> {
                            isClicked = !isClicked
                            answer = ""
                            mediaPlayer2 = MediaPlayer.create(context,R.raw.failedcard)
                            mediaPlayer2.start()

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
                        }
                    }
                }
            }

            if (position == (level -1)|| position == (level -2) ) {
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
    fun dialogWin(view:View){
        val views = View.inflate(context,R.layout.dialog_win,null)
        val builder = AlertDialog.Builder(context)
        builder.setView(views)
        val dialog = builder.create()
        dialog.show()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val home = views.findViewById<Button>(R.id.homePage)
        home.setOnClickListener {

            cardClickListner.onWin(true)
            dialog.dismiss()

        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val photo = ar000[position]
        holder.image.load(photo.url_s)
        holder.title.text = photo.title
        holder.title.textSize = 0.0f
        holder.bind(photo, position)

    }

    override fun getItemCount(): Int = level

}


