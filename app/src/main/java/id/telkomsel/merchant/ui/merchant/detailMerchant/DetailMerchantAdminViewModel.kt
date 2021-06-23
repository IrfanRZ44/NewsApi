package id.telkomsel.merchant.ui.merchant.detailMerchant

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
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelMerchant
import id.telkomsel.merchant.model.response.ModelResponse
import id.telkomsel.merchant.model.response.ModelResponseDataKategori
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.onClickFoto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class DetailMerchantAdminViewModel(
    private val context: Context?,
    private val navController: NavController
) : BaseViewModel() {
    val dataMerchant = MutableLiveData<ModelMerchant>()
    val clusterBranch = MutableLiveData<String>()
    val kategori = MutableLiveData<String>()

    fun clickFotoProfil(){
        dataMerchant.value?.foto_profil?.let { onClickFoto(it, navController) }
    }

    fun onClickAccept(){
        dataMerchant.value?.id?.let { updateStatus(it, Constant.statusActive, "") }
    }

    fun onClickDecline(){
        dataMerchant.value?.id?.let {
            context?.let {
                    it1 -> showDialogTolak(it1, it)
            }
        }
    }

    fun onClickMaps(){
        val gmmIntentUri = Uri.parse("google.navigation:q=${dataMerchant.value?.latitude},${dataMerchant.value?.longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context?.startActivity(mapIntent)
    }

    private fun showDialogTolak(context: Context, id: Int){
        val alert = AlertDialog.Builder(context)
        alert.setMessage("Mohon masukkan alasan merchant ini ditolak :")

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

    fun getDataKategori(kategori_id: Int){
        isShowLoading.value = true

        RetrofitUtils.getDataKategori(kategori_id,
            object : Callback<ModelResponseDataKategori> {
                override fun onResponse(
                    call: Call<ModelResponseDataKategori>,
                    response: Response<ModelResponseDataKategori>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        kategori.value = result.data?.nama
                    }
                    else{
                        kategori.value = "-"
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseDataKategori>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    kategori.value = "-"
                }
            })
    }

    private fun updateStatus(id: Int, status: String, comment: String){
        isShowLoading.value = true

        RetrofitUtils.updateStatusMerchant(id, status, comment,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        if (status == Constant.statusActive){
                            message.value = "Berhasil konfirmasi merchant"
                            Toast.makeText(context, "Berhasil konfirmasi merchant", Toast.LENGTH_LONG).show()
                        }
                        else{
                            message.value = "Berhasil menolak permintaan merchant"
                            Toast.makeText(context, "Berhasil menolak permintaan merchant", Toast.LENGTH_LONG).show()
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