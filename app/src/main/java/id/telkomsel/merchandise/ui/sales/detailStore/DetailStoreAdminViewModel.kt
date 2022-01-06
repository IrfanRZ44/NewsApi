package id.telkomsel.merchandise.ui.sales.detailStore

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import id.telkomsel.merchandise.R
import id.telkomsel.merchandise.base.BaseViewModel
import id.telkomsel.merchandise.model.ModelStore
import id.telkomsel.merchandise.model.response.ModelResponse
import id.telkomsel.merchandise.utils.Constant
import id.telkomsel.merchandise.utils.RetrofitUtils
import id.telkomsel.merchandise.utils.adapter.onClickFoto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class DetailStoreAdminViewModel(
    private val context: Context?,
    private val navController: NavController
) : BaseViewModel() {
    val dataStore = MutableLiveData<ModelStore>()

    fun clickFotoDepanAtas(){
        dataStore.value?.foto_depan_atas?.let { onClickFoto(it, navController) }
    }

    fun clickFotoDepanBawah(){
        dataStore.value?.foto_depan_bawah?.let { onClickFoto(it, navController) }
    }

    fun clickFotoEtalasePerdana(){
        dataStore.value?.foto_etalase_perdana?.let { onClickFoto(it, navController) }
    }

    fun clickFotoMejaPembayaran(){
        dataStore.value?.foto_meja_pembayaran?.let { onClickFoto(it, navController) }
    }

    fun clickFotoBelakangKasir(){
        dataStore.value?.foto_belakang_kasir?.let { onClickFoto(it, navController) }
    }

    fun onClickAccept(){
        dataStore.value?.id?.let { updateStatus(it, Constant.statusActive, "") }
    }

    fun onClickDecline(){
        dataStore.value?.id?.let {
            context?.let {
                    it1 -> showDialogTolak(it1, it)
            }
        }
    }

    fun onClickMaps(){
        val gmmIntentUri = Uri.parse("google.navigation:q=${dataStore.value?.latitude},${dataStore.value?.longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context?.startActivity(mapIntent)
    }

    private fun showDialogTolak(context: Context, id: Int){
        val alert = AlertDialog.Builder(context)
        alert.setMessage("Mohon masukkan alasan merchandise ini ditolak :")

        val editText = EditText(context)
        val linearLayout = LinearLayout(context)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        linearLayout.setPadding(20, 0, 20, 0)
        linearLayout.layoutParams = layoutParams
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(editText)
        alert.setView(linearLayout)

        alert.setPositiveButton(
            "Konfirmasi"
        ) { dialog, _ ->
            val comment = editText.text.toString()
            dialog.dismiss()
            if (comment.isEmpty()){
                message.value = "Error, mohon masukkan komentar"
            }
            else{
                updateStatus(id, Constant.statusDeclined, comment)
            }
        }
        alert.setNegativeButton(
            Constant.batal
        ) { dialog, _ ->
            dialog.dismiss()
        }

        alert.show()
    }

    private fun updateStatus(id: Int, status: String, comment: String){
        isShowLoading.value = true

        RetrofitUtils.updateStatusStore(id, status, comment,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        if (status == Constant.statusActive){
                            message.value = "Berhasil konfirmasi store"
                            Toast.makeText(context, "Berhasil konfirmasi", Toast.LENGTH_LONG).show()
                        }
                        else{
                            message.value = "Berhasil menolak store"
                            Toast.makeText(context, "Berhasil menolak permintaan", Toast.LENGTH_LONG).show()
                        }
                        val navOption = NavOptions.Builder().setPopUpTo(R.id.adminFragment, true).build()
                        navController.navigate(R.id.adminFragment, null, navOption)
                    }
                    else{
                        message.value = result?.message
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponse>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = "Error ${t.message}"
                }
            })
    }
}