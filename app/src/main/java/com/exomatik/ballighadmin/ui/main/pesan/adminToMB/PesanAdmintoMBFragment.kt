package com.exomatik.ballighadmin.ui.main.pesan.adminToMB

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import com.exomatik.ballighadmin.R
import com.exomatik.ballighadmin.base.BaseFragmentBind
import com.exomatik.ballighadmin.databinding.FragmentPesanAdminToMbBinding
import com.exomatik.ballighadmin.utils.Constant.codeRequestFoto
import com.exomatik.ballighadmin.utils.FileUtil
import com.exomatik.ballighadmin.utils.FirebaseUtils
import androidx.navigation.fragment.findNavController
import com.exomatik.ballighadmin.model.ModelUser
import com.exomatik.ballighadmin.utils.Constant.admin
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class PesanAdmintoMBFragment : BaseFragmentBind<FragmentPesanAdminToMbBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_pesan_admin_to_mb
    private lateinit var viewModel: PesanAdmintoMBViewModel

    override fun myCodeHere() {
        init()
        onClick()
    }

    private fun init(){
        bind.lifecycleOwner = this

        viewModel = PesanAdmintoMBViewModel(bind.rcChat, context, activity, findNavController())
        bind.viewModel = viewModel
        bind.swipeRefresh.isRefreshing = false
        val dataUserMuballigh = this.arguments?.getParcelable<ModelUser>("dataUserMuballigh")
        val idChat= this.arguments?.getString("idChat")
        val username= this.arguments?.getString("username")
        viewModel.setData()
        viewModel.initAdapter()

        if (dataUserMuballigh != null){
            viewModel.dataUserMuballigh.value = dataUserMuballigh
            viewModel.idChat = "${dataUserMuballigh.username}___$admin"
            viewModel.refreshDataUser(dataUserMuballigh.username)
            viewModel.refreshDataChat()
        }
        else if (!idChat.isNullOrEmpty() && !username.isNullOrEmpty()){
            viewModel.idChat = idChat
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
