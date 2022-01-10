package id.exomatik.news.ui.news.listNews

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import id.exomatik.news.R
import id.exomatik.news.model.ModelNews
import kotlinx.android.synthetic.main.item_news.view.*

class AdapterNews(
    private val listKelas: ArrayList<ModelNews>,
    private val onClickSource: (ModelNews) -> Unit,
    private val onClickItem: (ModelNews) -> Unit
) : RecyclerView.Adapter<AdapterNews.AfiliasiHolder>(){

    inner class AfiliasiHolder(private val viewItem : View) : RecyclerView.ViewHolder(viewItem){
        @SuppressLint("SetTextI18n")
        fun bindAfiliasi(item: ModelNews){
            viewItem.textNama.text = item.title
            viewItem.textDeskripsi.text = item.description
            viewItem.btnSource.text = item.source?.name
            viewItem.textTanggal.text = item.publishedAt.take(10)

            viewItem.setOnClickListener {
                onClickItem(item)
            }

            viewItem.btnSource.setOnClickListener {
                onClickSource(item)
            }

            viewItem.imgFoto.load(item.urlToImage) {
                crossfade(true)
                placeholder(R.drawable.ic_image_gray)
                error(R.drawable.ic_image_gray)
                fallback(R.drawable.ic_image_gray)
                memoryCachePolicy(CachePolicy.ENABLED)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AfiliasiHolder {
        return AfiliasiHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false))
    }
    override fun getItemCount(): Int = listKelas.size
    override fun onBindViewHolder(holder: AfiliasiHolder, position: Int) {
        holder.bindAfiliasi(listKelas[position])
    }
}
