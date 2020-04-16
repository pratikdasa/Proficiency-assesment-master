package com.proficiencyassesment.utils

import android.app.AlertDialog
import android.content.Context
import com.proficiencyassesment.R

object PopupUtils {
    fun showAlertDialog(mContext: Context?, title: String?, message: String?) {
        val builder: AlertDialog.Builder? = mContext?.let { AlertDialog.Builder(it) }
        builder?.setTitle(title)
        builder?.setMessage(message)
        builder?.setPositiveButton(mContext.getString(R.string.close), null)
        builder?.show()
    }
}