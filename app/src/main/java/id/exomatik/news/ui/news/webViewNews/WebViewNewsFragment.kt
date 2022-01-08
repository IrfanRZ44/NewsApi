package id.exomatik.news.ui.news.webViewNews

import android.annotation.SuppressLint
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import id.exomatik.news.R
import id.exomatik.news.base.BaseFragmentBind
import id.exomatik.news.databinding.FragmentWebViewNewsBinding
import id.exomatik.news.utils.Constant


class WebViewNewsFragment : BaseFragmentBind<FragmentWebViewNewsBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_web_view_news
    lateinit var viewModel: WebViewNewsViewModel

    override fun myCodeHere() {
        supportActionBar?.title = Constant.menuDetail
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        init()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun init() {
        bind.lifecycleOwner = this
        viewModel = WebViewNewsViewModel()
        bind.viewModel = viewModel
        try {
            bind.webView.clearCache(true)
            bind.webView.clearHistory()
            bind.webView.webChromeClient = WebChromeClient()
            bind.webView.webViewClient = WebViewClient()
            bind.webView.settings.loadsImagesAutomatically = true
            bind.webView.settings.javaScriptEnabled = true
            bind.webView.settings.javaScriptCanOpenWindowsAutomatically = true
            bind.webView.settings.domStorageEnabled = true
            bind.webView.settings.setSupportZoom(false)
            bind.webView.settings.builtInZoomControls = false
            bind.webView.settings.displayZoomControls = false
            bind.webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            bind.webView.loadUrl(this.arguments?.getString(Constant.reffUrl)?:throw Exception("Error, terjadi kesalahan database"))
        }catch (e: Exception){
            viewModel.message.value = e.message
        }
    }
}