package com.tuwiaq.projectgame.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import androidx.core.graphics.drawable.toDrawable
import com.tuwiaq.projectgame.R

object CustomProgressDialog {
    fun showLoaingDialog(context: Context):Dialog{
        val progressDialog = Dialog(context)
        progressDialog.let {
            it.show()
            it.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            it.setContentView(R.layout.progress_dialog_view)
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
            return it
        }
    }

}