package com.tuwiaq.projectgame.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.tuwiaq.projectgame.R
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tuwiaq.projectgame.model.MainViewModel

import kotlinx.coroutines.tasks.await
import java.util.*


class MainFragment : Fragment() {
    private lateinit var btn_stage: Button
    private lateinit var sign_in: Button
    private lateinit var uploadImage: Button
    private lateinit var saveImage: Button
    private lateinit var customCard: Button
    private lateinit var language_btn: Button
    private lateinit var closeBtn: ImageButton
    private lateinit var closeBtn1: ImageButton
    private lateinit var btn_hard: Button
    private lateinit var btn_easy: Button
    private lateinit var btn_M: Button
    private lateinit var total: TextView
    private lateinit var imageGalley:ImageView
    private lateinit var googleSignInClient: GoogleSignInClient
    val rc_sgin_in = 0

    companion object{
        const val REUEST_FROM_GALLERY = 1900
        const val GALLERY_PERMISSION_CODE = 9
        val Tag = "MainFragment"
    }
  private val db by lazy { FirebaseFirestore.getInstance() }
    private val vm by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private lateinit var card: CardView
    private lateinit var card2: CardView

     private var LoadingDialog:Dialog? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
           setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        btn_stage = view.findViewById(R.id.stage)
        customCard = view.findViewById(R.id.custom)
        card = view.findViewById(R.id.stage_mode)
        card2 = view.findViewById(R.id.customCard)
        uploadImage =view.findViewById(R.id.upload)
        imageGalley = view.findViewById(R.id.imageLoa)
        language_btn = view.findViewById(R.id.language)
        closeBtn = view.findViewById(R.id.X)
        closeBtn1 = view.findViewById(R.id.X1)
        total = view.findViewById(R.id.total_sco)
        total.text = vm.getSaveSco()

        val action = (activity as AppCompatActivity).supportActionBar
        action?.title =resources.getString(R.string.app_name)

        loadLoc()
        language_btn.setOnClickListener {
            showChangeLang()
        }

        btn_stage.setOnClickListener {
            card.background = resources.getDrawable(R.drawable.trick, null)
            card.visibility = View.VISIBLE
            closeBtn1.setOnClickListener {
                refreshCurrentFragment()
            }

        }
        btn_M = view.findViewById(R.id.medium_lvl)
        btn_M.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToLvlOneFragment(NumberOfCard.MEDIUM.numberOfCardToString())
            findNavController().navigate(action)
            showLoading()
            android.os.Handler().postDelayed(
                {hideLoading()

                },4000)
        }

        btn_easy = view.findViewById(R.id.easy_lvl)
        btn_easy.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToLvlOneFragment(NumberOfCard.EASY.numberOfCardToString())
            findNavController().navigate(action)

            showLoading()
            android.os.Handler().postDelayed(
                {hideLoading()

                },4000)


        }
        btn_hard = view.findViewById(R.id.hard_lvl)
        btn_hard.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToLvlOneFragment(NumberOfCard.HARD.numberOfCardToString())
            findNavController().navigate(action)
            showLoading()
            android.os.Handler().postDelayed(
                {hideLoading()

                },4000)
        }

        sign_in = view.findViewById(R.id.sin)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }!!
        sign_in.setOnClickListener {
            signIn()
        }
        customCard.setOnClickListener {
            card2.background = resources.getDrawable(R.drawable.trick, null)
            card2.visibility = View.VISIBLE
            closeBtn.setOnClickListener {
                refreshCurrentFragment()
            }
            initUI()

        }

        return view
    }
    fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(
            signInIntent, rc_sgin_in
        )
    }
    private fun initUI() {
            uploadImage.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(
                            requireContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_DENIED
                    ) {
                        //permission denied
                        val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                        //show popup to request runtime permission
                        requestPermissions(permission, GALLERY_PERMISSION_CODE)
                    } else {
                        //permission already granted
                        pickImageFromGallery()
                    }
                } else {
                    //system os is < Marshmallow
                    pickImageFromGallery()
                }
            }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        when(requestCode)
        {
            GALLERY_PERMISSION_CODE ->
            {
                if (grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else
                {
                    //permission from popup denied
                    Toast.makeText(context,"permission denied",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun hideLoading(){
        LoadingDialog?.let {
            if (it.isShowing)it.cancel()
        }
    }
    private fun showLoading(){
        hideLoading()
        LoadingDialog = CustomProgressDialog.showLoaingDialog(requireContext())
    }
    private fun pickImageFromGallery(){

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REUEST_FROM_GALLERY)

    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REUEST_FROM_GALLERY )
        {
            imageGalley.setImageURI(data?.data)
            addEvent(data?.data!!)
        }
    }

     fun addEvent( imageUri: Uri) {
        val formatter = SimpleDateFormat("yyy-MM-dd-HH-mm-ss")
        val now = Date()
        val fileName = formatter.format(now)
        var storageRef = Firebase.storage.reference.child("images/$fileName.jpg")

        try {
            //Upload the image to FireStorage
            storageRef.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        //Add event to FireStore
                        val docData = hashMapOf(
                            "ul" to it.toString()
                        )
                        db.collection("ImageUrl").document("key").set(docData)
                    }.addOnFailureListener {
                        Log.e(Tag, "Error upload the image ", it)

                    }
                }


        } catch (e: Exception) {
            Log.e(Tag, "Error add the event ", e)

        }

    }

    fun showChangeLang(){
        val b = arrayOf("english","عربي")
        val hBuild = AlertDialog.Builder(context)
        hBuild.setTitle("pick language")
        hBuild.setPositiveButton("ok"){_,_->
            refreshCurrentFragment()
        }
        hBuild.setSingleChoiceItems(b,-1){dialog,which ->
            if (which == 0){
                setLocle("ar")
                refreshCurrentFragment()

            }else{
                setLocle("en")
                refreshCurrentFragment()
            }
            dialog.dismiss()
        }
        val _dialog = hBuild.create()
        _dialog.show()

    }

    private fun setLocle(s: String){
        val loc = Locale(s)
        Locale.setDefault(loc)
        val conf = Configuration()
        conf.locale = loc
        requireContext().resources.updateConfiguration(conf,requireContext().resources.displayMetrics)
        var sharedpref1 = requireContext().getSharedPreferences("My_pref",MODE_PRIVATE)
        var editor = sharedpref1.edit()
        editor.putString("My_Lang",s)
        editor.apply()

    }
    private fun loadLoc(){
        var sher = requireContext().getSharedPreferences("My_pref",MODE_PRIVATE)
        val lang = sher.getString("My_Lang","")
        setLocle(lang.toString())
    }

    private fun refreshCurrentFragment() {
           val id = findNavController().currentDestination?.id
        findNavController().navigateUp()
        findNavController().navigate(id!!)

    }

}


