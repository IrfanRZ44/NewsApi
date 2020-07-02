package com.exomatik.ballighadmin.ui.main.pesan.adminToMJ

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseFragmentBind
import com.exomatik.ballighadmin.databinding.FragmentPesanAdminToMjBinding
import com.exomatik.ballighadmin.utils.Constant.codeRequestFoto
import com.exomatik.ballighadmin.utils.FileUtil
import com.exomatik.ballighadmin.utils.FirebaseUtils
import androidx.navigation.fragment.findNavController
import com.exomatik.ballighadmin.model.ModelDataMasjid
import com.exomatik.ballighadmin.model.ModelUser
import com.exomatik.ballighadmin.utils.Constant.admin
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class PesanAdmintoMJFragment : BaseFragmentBind<FragmentPesanAdminToMjBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_pesan_admin_to_mj
    private lateinit var viewModel: PesanAdmintoMJViewModel

    override fun myCodeHere() {
        init()
        onClick()
    }

    private fun init(){
        bind.lifecycleOwner = this

        viewModel = PesanAdmintoMJViewModel(bind.rcChat, context, activity, findNavController())
        bind.viewModel = viewModel
        bind.swipeRefresh.isRefreshing = false
        val dataMasjid = this.arguments?.getParcelable<ModelDataMasjid>("dataMasjid")
        val dataPengurus = this.arguments?.getParcelable<ModelUser>("dataPengurus")
        val username= this.arguments?.getString("username")
        viewModel.setData()
        viewModel.initAdapter()

        if (dataMasjid != null && dataPengurus != null){
            viewModel.dataMasjid.value = dataMasjid
            viewModel.dataPengurus.value = dataPengurus
            viewModel.idChat = "${dataPengurus.username}___$admin"
            viewModel.refreshDataUser(dataPengurus.username)
            viewModel.refreshDataChat()
        }
        else if (dataMasjid == null && dataPengurus != null){
            viewModel.dataPengurus.value = dataPengurus
            viewModel.getDataMasjid(dataPengurus.username)
            viewModel.idChat = "${dataPengurus.username}___$admin"
            viewModel.refreshDataUser(dataPengurus.username)
            viewModel.refreshDataChat()
        }
        else if (!username.isNullOrEmpty()){
            viewModel.idChat = "${username}___$admin"
            viewModel.refreshDataUser(username)
            viewModel.refreshDataChat()
        }
    }

    private fun pickFoto(){
        startActivityForResult(
            Intent("android.intent.action.PICK",
            MediaStore.Images.Media.INTERNAL_CONTENT_URI), codeRequestFoto)
    }

    private fun onClick() {
        bind.swipeRefresh.setOnRefreshListener {
            viewModel.listChat.clear()
            viewModel.refreshDataChat()
            bind.swipeRefresh.isRefreshing = false
        }

        bind.btnImg.setOnClickListener {
            pickFoto()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseUtils.stopRefreshDataUserChat()
        FirebaseUtils.stopRefreshDataChat()
    }

    override fun onPrimaryNavigationFragmentChanged(isPrimaryNavigationFragment: Boolean) {
        activity?.customeToolbar?.visibility = View.GONE
        super.onPrimaryNavigationFragmentChanged(isPrimaryNavigationFragment)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == -1 && requestCode == codeRequestFoto) {
            try {
                val actualImage = FileUtil.from(activity?:throw Exception("Afwan, mohon muat ulang aplikasi"), data?.data)
                val compressedImage = Compressor(activity).compressToFile(actualImage)
                val imageUri = Uri.fromFile(compressedImage)

                viewModel.isShowFoto.value = true
                viewModel.urlFoto.value = imageUri
            } catch (e: IOException) {
                var message = e.message
                if (message != null) {
                    if (message == "prefix must be at least 3 characters") {
                        message = "Afwan, nama foto harus lebih dari 3 karakter"
                    }
                }
                viewModel.isShowLoading.value = false
                viewModel.message.value = message
            } catch (e: Exception) {
                var message = e.message
                if (message != null) {
                    if (message == "prefix must be at least 3 characters") {
                        message = "Afwan, nama foto harus lebih dari 3 karakter"
                    }
                }
                viewModel.isShowLoading.value = false
                viewModel.message.value = message
            }
        }
    }
}
