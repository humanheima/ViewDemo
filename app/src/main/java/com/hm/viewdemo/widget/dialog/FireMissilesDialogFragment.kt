package com.hm.viewdemo.widget.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2019-10-22.
 * Desc:
 */
class FireMissilesDialogFragment : androidx.fragment.app.DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val dialog = builder.setTitle("hello world")
                    .setMessage("Fire missiles?")
                    .setPositiveButton("fire") { dialog, which ->
                        Toast.makeText(activity, "fire", Toast.LENGTH_SHORT).show()
                    }.setNegativeButton("cancel") { dialog, which ->
                        Toast.makeText(activity, "cancel", Toast.LENGTH_SHORT).show()
                    }
                    .create()
            dialog.window?.setWindowAnimations(R.style.scale_animate_dialog)
            return dialog

        } ?: throw  throw IllegalStateException("Activity cannot be null")
    }
}