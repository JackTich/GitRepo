package com.jacktich.gitrepo.utils

import android.content.Context
import android.opengl.Visibility
import android.view.View
import android.widget.Toast
import com.jacktich.gitrepo.R

object Extensions {
    fun showServerError(context: Context) =
        Toast.makeText(context, context.getString(R.string.error_connection), Toast.LENGTH_SHORT)
            .show()

    fun View.makeVisible() {
        this.visibility = View.VISIBLE
    }

    fun View.makeGone(){
        this.visibility = View.GONE
    }
}