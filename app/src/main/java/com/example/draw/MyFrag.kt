package com.example.draw

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment


class MyFrag :Fragment() {
    private lateinit var web:WebView
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val frame= inflater.inflate(R.layout.left_frag,container,false)
        web=frame.findViewById(R.id.web)
        val webChromeClient = WebChromeClient()
        web.webChromeClient = webChromeClient
        web.settings.javaScriptEnabled=true
        web.webViewClient= WebViewClient()
        web.loadUrl("file:///android_asset/helloMinda.html")
        return frame
    }
}