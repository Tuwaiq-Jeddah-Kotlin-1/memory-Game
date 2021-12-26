package com.tuwiaq.projectgame.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
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

/*    object dailogWin1{
        fun win(){
            val view = View.inflate(context,R.layout.dialog_win,null)
            val builder = AlertDialog.Builder(context)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }*/

/*    fun show (context: Context):Dialog{
        return show(context,null)

    }
    fun show(context: Context, title1: CharSequence?):Dialog{
        val inflater = (context as Activity).layoutInflater
        val view = inflater.inflate(R.layout.progress_dialog_view,null)
        val title = view.findViewById<TextView>(R.id.cp_title)
        if (title1 != null ){
            title.text = title1.toString()
        }
        val card = view.findViewById<CardView>(R.id.cp_cardview)
        card.setCardBackgroundColor(Color.parseColor("#70000000"))
        title.setTextColor(Color.WHITE)
        dialog = CustomDialog(context)
        dialog.setContentView(view)
        dialog.show()
        return dialog
    }
    private fun setColorFilter(drawable:Drawable,color:Int){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        }else{
            drawable.setColorFilter(color,PorterDuff.Mode.SRC_ATOP)
        }
    }

    class CustomDialog(context: Context):Dialog(context,R.style.CustomDialogTheme) {
        init {
            window?.decorView?.rootView?.setBackgroundResource(R.color.cardview_dark_background)
            window?.decorView?.setOnApplyWindowInsetsListener{_,insets ->
                insets.consumeSystemWindowInsets()
            }
        }
    }*/
}