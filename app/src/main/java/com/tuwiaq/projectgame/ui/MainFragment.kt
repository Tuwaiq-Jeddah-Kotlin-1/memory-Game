package com.tuwiaq.projectgame.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tuwiaq.projectgame.R
import com.google.firebase.auth.FirebaseAuth
import com.tuwiaq.projectgame.model.MainViewModel
import java.io.File
import java.io.IOException
import java.util.*


class MainFragment : Fragment() {
    private lateinit var btn_stage: Button
    private lateinit var uploadImage: Button
    private lateinit var customCard: Button
    private lateinit var language_btn: Button
    private lateinit var closeBtn: ImageButton
    private lateinit var closeBtn1: ImageButton
    private lateinit var delete: ImageButton
    private lateinit var btn_hard: Button
    private lateinit var LogOutBtn: Button
    private lateinit var sin: Button
    private lateinit var btn_easy: Button
    private lateinit var camera: Button
    private lateinit var btn_M: Button
    private lateinit var total: TextView
    private lateinit var back_image: TextView
    private lateinit var emailPro: TextView
    private var photoFile: File? = null
    private var mCurrentPhotoPath: String? = null
    private lateinit var imageGalley: ImageView
    private lateinit var firebase: FirebaseAuth
    private lateinit var mediaPlayer: MediaPlayer
    private val acc_state: MainViewModel by viewModels()



    companion object {
        const val REUEST_FROM_GALLERY = 1900
        const val REUEST_FROM_CAMERA = 55

        const val GALLERY_PERMISSION_CODE = 9
        val Tag = "MainFragment"
    }

    private val vm by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)

    }

    private lateinit var card: CardView
    private lateinit var card2: CardView

    private var LoadingDialog: Dialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        uploadImage = view.findViewById(R.id.upload)
        imageGalley = view.findViewById(R.id.imageLoa)
        language_btn = view.findViewById(R.id.language)
        sin = view.findViewById<Button>(R.id.sin)
        closeBtn = view.findViewById(R.id.X)
        closeBtn1 = view.findViewById(R.id.X1)
        total = view.findViewById(R.id.total_sco)
        camera = view.findViewById(R.id.camera)
        delete = view.findViewById(R.id.delete)
        emailPro = view.findViewById(R.id.logedInAS)
        LogOutBtn = view.findViewById(R.id.LogOutBtn)
        back_image = view.findViewById(R.id.textBack)


        firebase = FirebaseAuth.getInstance()
        checkUser()



        language_btn.setOnClickListener {
            mediaPlayer = MediaPlayer.create(context, R.raw.button)
            mediaPlayer.start()
            showChangeLang()
        }


        // ---------------------------------------------------


        btn_stage.setOnClickListener {
            mediaPlayer = MediaPlayer.create(context, R.raw.button)
            mediaPlayer.start()
          //  card.background = resources.getDrawable(R.drawable.trick, null)
            sin.visibility = View.INVISIBLE
            customCard.visibility = View.INVISIBLE
            btn_stage.visibility = View.INVISIBLE
            LogOutBtn.visibility = View.INVISIBLE
            card.visibility = View.VISIBLE
            closeBtn1.setOnClickListener {
                refreshCurrentFragment()
            }

        }
        btn_M = view.findViewById(R.id.medium_lvl)
        btn_M.setOnClickListener {
            mediaPlayer = MediaPlayer.create(context, R.raw.button)
            mediaPlayer.start()

            val action =
                MainFragmentDirections.actionMainFragmentToLvlOneFragment(NumberOfCard.MEDIUM.numberOfCardToString())
            findNavController().navigate(action)
            showLoading()
            Handler(Looper.getMainLooper()).postDelayed({
                hideLoading()
            }, 3000)
        }

        btn_easy = view.findViewById(R.id.easy_lvl)
        btn_easy.setOnClickListener {
            mediaPlayer = MediaPlayer.create(context, R.raw.button)
            mediaPlayer.start()

            val action =
                MainFragmentDirections.actionMainFragmentToLvlOneFragment(NumberOfCard.EASY.numberOfCardToString())
            findNavController().navigate(action)

            showLoading()
            Handler(Looper.getMainLooper()).postDelayed({
                hideLoading()
            }, 3000)

        }
        btn_hard = view.findViewById(R.id.hard_lvl)
        btn_hard.setOnClickListener {
            mediaPlayer = MediaPlayer.create(context, R.raw.button)
            mediaPlayer.start()

            val action =
                MainFragmentDirections.actionMainFragmentToLvlOneFragment(NumberOfCard.HARD.numberOfCardToString())
            findNavController().navigate(action)

            showLoading()
            Handler(Looper.getMainLooper()).postDelayed({
                hideLoading()
            }, 3000)

            /*  android.os.Handler().postDelayed(
                  {
                      hideLoading()

                  }, 4000
              )*/
        }



        customCard.setOnClickListener {
            mediaPlayer = MediaPlayer.create(context, R.raw.button)
            mediaPlayer.start()

            if (firebase.currentUser == null) {
                Toast.makeText(requireContext(), "you must sign in first", Toast.LENGTH_SHORT)
                    .show()
            } else {

               // card2.background = resources.getDrawable(R.drawable.trick, null)
                   sin.visibility = View.INVISIBLE
                customCard.visibility = View.INVISIBLE
                btn_stage.visibility = View.INVISIBLE
                LogOutBtn.visibility = View.INVISIBLE
                card2.visibility = View.VISIBLE
                card2.onWindowFocusChanged(true)
                closeBtn.setOnClickListener {
                    refreshCurrentFragment()
                }
                camera.setOnClickListener {
                    captureImage()
                }
                delete.setOnClickListener {
                    acc_state.deleteImags(requireContext())

                }

                initUI()
            }

        }

        return view
    }

    private fun checkUser() {
        val fierbaseUser = firebase.currentUser?.email
        if (fierbaseUser != null) {
            total.text = vm.getSaveSco()
           // acc_state.getScore("")
            val email = fierbaseUser
            emailPro.text = email
            LogOutBtn.visibility = View.VISIBLE
            sin.visibility = View.GONE
            back_image.visibility = View.VISIBLE
            LogOutBtn.setOnClickListener {
                acc_state.signOutFromAcc()
                //  firebase.signOut()
                checkUser()

                refreshCurrentFragment()
            }

        } else {
            sin.visibility = View.VISIBLE
            sin.setOnClickListener {
                findNavController().navigate(R.id.logInFragment)
            }


        }
    }


    private fun initUI() {
        uploadImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
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



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            GALLERY_PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(context, "permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun captureImage() {
        if (checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0
            )
        } else {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent?.also {
                    photoFile = try {
                        createImageFile()
                    } catch (ex: IOException) {

                        null
                    }
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            requireActivity().applicationContext,
                            "com.tuwiaq.projectgame.contentprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        getActionTakePicture.launch(takePictureIntent)
                    }
                }
            }
        }
    }

    private val getActionTakePicture =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                acc_state.addImage(photoFile!!.toUri())
                imageGalley.setImageURI(photoFile!!.toUri())
            } else {
            }

        }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )
        mCurrentPhotoPath = image.absolutePath
        return image
    }


    private fun hideLoading() {
        LoadingDialog?.let {
            if (it.isShowing) it.cancel()
        }
    }

    private fun showLoading() {
        hideLoading()
        LoadingDialog = CustomProgressDialog.showLoaingDialog(requireContext())
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REUEST_FROM_GALLERY)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REUEST_FROM_GALLERY) {
            imageGalley.setImageURI(data?.data)
            acc_state.addImage(data?.data!!)
           // addEvent(data?.data!!)

        }
        when (requestCode) {
            REUEST_FROM_CAMERA ->
                if (resultCode == Activity.RESULT_OK && requestCode == REUEST_FROM_CAMERA) {
                    imageGalley.setImageURI(data?.data)
                    acc_state.addImage(data?.data!!)
                   // addEvent(data?.data!!)
                }
        }
    }

    fun showChangeLang() {
        val b = arrayOf("english", "عربي")
        val hBuild = AlertDialog.Builder(context)
        hBuild.setTitle("pick language")
        hBuild.setPositiveButton("ok") { _, _ ->
            refreshCurrentFragment()
        }
        hBuild.setSingleChoiceItems(b, -1) { dialog, which ->
            if (which == 0) {
                setLocle("en")
                refreshCurrentFragment()

            } else {
                setLocle("ar")
                refreshCurrentFragment()
            }
            dialog.dismiss()
        }
        val _dialog = hBuild.create()
        _dialog.show()

    }

    private fun setLocle(s: String) {
        val loc = Locale(s)
        Locale.setDefault(loc)
        val conf = Configuration()
        conf.locale = loc
        requireContext().resources.updateConfiguration(
            conf,
            requireContext().resources.displayMetrics
        )
        val sharedpref1 = requireContext().getSharedPreferences("My_pref", MODE_PRIVATE)
        val editor = sharedpref1.edit()
        editor.putString("My_Lang", s)
        editor.apply()

    }

    private fun refreshCurrentFragment() {
        val id = findNavController().currentDestination?.id
        findNavController().navigateUp()
        findNavController().navigate(id!!)

    }

}


