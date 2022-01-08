package id.exomatik.news.ui.news.detailNews

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import id.exomatik.news.R
import id.exomatik.news.base.BaseViewModel
import id.exomatik.news.model.ModelNews
import id.exomatik.news.ui.news.webViewNews.WebViewNewsFragment
import id.exomatik.news.utils.Constant
import id.exomatik.news.utils.adapter.onClickFoto

@SuppressLint("StaticFieldLeak")
class DetailNewsViewModel(private val navController: NavController) : BaseViewModel() {
    val dataNews = MutableLiveData<ModelNews>()

    fun clickFoto(){
        dataNews.value?.urlToImage?.let { onClickFoto(it, navController) }
    }

    fun clickLink(){
        val bundle = Bundle()
        val fragmentTujuan = WebViewNewsFragment()
        bundle.putString(Constant.reffUrl, dataNews.value?.url)
        fragmentTujuan.arguments = bundle
        navController.navigate(R.id.webViewNewsFragment, bundle)
    }
}