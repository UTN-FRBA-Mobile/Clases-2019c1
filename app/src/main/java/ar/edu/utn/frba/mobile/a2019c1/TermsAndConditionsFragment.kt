package ar.edu.utn.frba.mobile.a2019c1

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment

/**
 * Created by emanuel on 17/4/17.
 */

class TermsAndConditionsFragment : Fragment() {
    private var webView: WebView? = null
    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_termsandconditions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView = view.findViewById(R.id.webView) as WebView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainFragment.OnFragmentInteractionListener) {
            mListener = context as OnFragmentInteractionListener
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onStart() {
        super.onStart()
        loadPage()
    }

    private fun loadPage() {
        webView!!.setBackgroundColor(Color.TRANSPARENT)
        webView!!.settings.javaScriptEnabled = true
        val url = "file:///android_asset/termsAndConditions.html"
        webView!!.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                view.loadUrl("javascript:setText(\"¡Hola JavaScript!\")")
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                return shouldOverrideUrlLoading(request.url)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return shouldOverrideUrlLoading(Uri.parse(url))
            }

            private fun shouldOverrideUrlLoading(url: Uri): Boolean {
                return if (APP_SCHEME == url.scheme) {
                    performCommand(url.host!!)
                } else false
            }
        }
        webView!!.loadUrl(url)
    }

    private fun performCommand(command: String): Boolean {
        when (command) {
            "accept" -> {
                acceptTermsAndConditions()
                return true
            }
            "doNotAccept" -> {
                doNotAcceptTermsAndConditions()
                return true
            }
        }
        return false
    }

    private fun acceptTermsAndConditions() {
        Toast.makeText(getContext(), R.string.accept, Toast.LENGTH_LONG).show()
        mListener!!.goBack()
    }

    private fun doNotAcceptTermsAndConditions() {
        Toast.makeText(getContext(), R.string.dontAccept, Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.add(R.string.acceptMenu).setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener {
            acceptTermsAndConditions()
            true
        })
        menu.add(R.string.dontAcceptMenu).setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener {
            doNotAcceptTermsAndConditions()
            true
        })
    }

    interface OnFragmentInteractionListener {

        fun goBack()
    }

    companion object {

        private val APP_SCHEME = "app"

        fun newInstance(): TermsAndConditionsFragment {
            return TermsAndConditionsFragment()
        }
    }
}
