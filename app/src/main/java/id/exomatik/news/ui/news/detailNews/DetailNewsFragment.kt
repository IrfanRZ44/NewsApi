package id.exomatik.news.ui.news.detailNews

import androidx.navigation.fragment.findNavController
import id.exomatik.news.R
import id.exomatik.news.databinding.FragmentDetailNewsBinding
import id.exomatik.news.base.BaseFragmentBind
import id.exomatik.news.utils.Constant

class DetailNewsFragment : BaseFragmentBind<FragmentDetailNewsBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_detail_news
    private lateinit var viewModel: DetailNewsViewModel

    override fun myCodeHere() {
        supportActionBar?.title = Constant.menuDetail
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        init()
    }

    private fun init() {
        bind.lifecycleOwner = this
        viewModel = DetailNewsViewModel(findNavController())
        bind.viewModel = viewModel
        try {
            viewModel.dataNews.value = this.arguments?.getParcelable(Constant.reffNews)?:throw Exception("Error, terjadi kesalahan database")
        }catch (e: Exception){
            viewModel.message.value = e.message
        }
    }
}