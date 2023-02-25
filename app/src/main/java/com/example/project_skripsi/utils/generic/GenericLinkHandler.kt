package com.example.project_skripsi.utils.generic

import android.content.Context
import android.content.Intent
import android.net.Uri

class GenericLinkHandler {

    companion object {
        fun goToLink(context: Context, link: String) {
            val uri = Uri.parse(link)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        }
    }

}