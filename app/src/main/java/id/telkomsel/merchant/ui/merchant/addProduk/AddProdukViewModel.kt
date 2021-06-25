package id.telkomsel.merchant.ui.merchant.addProduk

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.android.material.textfield.TextInputLayout
import faranjit.currency.edittext.CurrencyEditText
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelKategori
import id.telkomsel.merchant.model.ModelMerchant
import id.telkomsel.merchant.model.response.ModelResponse
import id.telkomsel.merchant.model.response.ModelResponseDaftarKategori
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.SpinnerKategoriAdapter
import id.telkomsel.merchant.utils.adapter.dismissKeyboard
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("StaticFieldLeak")
class AddProdukViewModel(
    private val activity: Activity?,
    private val context: Context?,
    private val navController: NavController,
    private val spinnerKategori: AppCompatSpinner,
    private val editNamaMerchant: TextInputLayout,
    private val editNamaProduk: TextInputLayout,
    private val editTglKadaluarsa: TextInputLayout,
    private val editStok: TextInputLayout,
    private val editDesc: TextInputLayout,
    private val editHarga: CurrencyEditText
) : BaseViewModel() {
    val etNamaMerchant = MutableLiveData<String>()
    val etNamaProduk = MutableLiveData<String>()
    val etTglKadaluarsa = MutableLiveData<String>()
    val etFotoProduk = MutableLiveData<Uri>()
    val etStok = MutableLiveData<String>()
    val etHarga = MutableLiveData<String>()
    val etDeskripsi = MutableLiveData<String>()
    val listKategori = ArrayList<ModelKategori>()
    lateinit var adapterKategori : SpinnerKategoriAdapter
    var dataMerchant: ModelMerchant? = null

    fun setAdapterKategori() {
        listKategori.clear()

        adapterKategori = SpinnerKategoriAdapter(
            activity,
            listKategori, true
        )
        spinnerKategori.adapter = adapterKategori
    }

    fun getDateTglKadaluarsa() {
        activity?.let { dismissKeyboard(it) }
        val datePickerDialog: DatePickerDialog
        val localCalendar = Calendar.getInstance()

        try {
            datePickerDialog = DatePickerDialog(
                activity ?: throw Exception("Error, mohon mulai ulang aplikasi"),
                { _, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3 ->
                    val dateSelected = Calendar.getInstance()
                    dateSelected[paramAnonymousInt1, paramAnonymousInt2] = paramAnonymousInt3
                    val dateFormatter = SimpleDateFormat(Constant.dateFormat1, Locale.US)
                    etTglKadaluarsa.value = dateFormatter.format(dateSelected.time)
                },
                localCalendar[Calendar.YEAR],
                localCalendar[Calendar.MONTH],
                localCalendar[Calendar.DATE]
            )

            datePickerDialog.show()
        } catch (e: java.lang.Exception) {
            message.value = e.message
        }
    }

    fun getDaftarKategori(){
        isShowLoading.value = true

        RetrofitUtils.getDaftarKategori(
            object : Callback<ModelResponseDaftarKategori> {
                override fun onResponse(
                    call: Call<ModelResponseDaftarKategori>,
                    response: Response<ModelResponseDaftarKategori>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess) {
                        val data = result.data
                        listKategori.clear()
                        listKategori.add(ModelKategori(0, 0, Constant.pilihKategori))

                        for (i in data.indices) {
                            listKategori.add(data[i])
                        }
                        adapterKategori.notifyDataSetChanged()

                        val idKategori = dataMerchant?.kategori_id
                        if (dataMerchant != null) {
                            for (i in listKategori.indices) {
                                if (listKategori[i].id == idKategori) {
                                    spinnerKategori.setSelection(i)
                                }
                            }
                        }
                    } else {
                        listKategori.clear()
                        listKategori.add(ModelKategori(0, 0, Constant.noDataKategori))
                        adapterKategori.notifyDataSetChanged()
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseDaftarKategori>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    listKategori.clear()
                    listKategori.add(ModelKategori(0, 0, Constant.noDataKategori))
                    adapterKategori.notifyDataSetChanged()
                }
            })
    }

    private fun setNullError(){
        editNamaMerchant.error = null
        editNamaProduk.error = null
        editDesc.error = null
        editStok.error = null
        editTglKadaluarsa.error = null
        editHarga.error = null
    }

    private fun setTextError(msg: String, editText: TextInputLayout){
        message.value = msg
        editText.error = msg
        editText.requestFocus()
        editText.findFocus()
    }

    fun onClickAddProduk(){
        setNullError()
        activity?.let { dismissKeyboard(it) }

        val namaMerchant = etNamaMerchant.value
        val namaProduk = etNamaProduk.value
        val kategori = listKategori[spinnerKategori.selectedItemPosition].id
        val tglKadaluarsa = etTglKadaluarsa.value
        val stok = etStok.value
        val harga = editHarga.currencyText.toString()
        val desc = etDeskripsi.value
        val fotoProduk = etFotoProduk.value?.path

        if (!namaMerchant.isNullOrEmpty() && !namaProduk.isNullOrEmpty()
            && !tglKadaluarsa.isNullOrEmpty() && !tglKadaluarsa.isNullOrEmpty()
            && (kategori != 0) && harga.isNotEmpty() && !desc.isNullOrEmpty()
            && !stok.isNullOrEmpty() && !fotoProduk.isNullOrEmpty()
        ) {
            val hargaReplaced = harga.replace(".", "")

            val fileProduk = File(fotoProduk)
            val urlFoto = MultipartBody.Part.createFormData("url_foto", fileProduk.name, RequestBody.create(
                MediaType.get("image/*"), fileProduk))
            val merchantId = RequestBody.create(MediaType.get("text/plain"), 60.toString())
            val kategoriId = RequestBody.create(MediaType.get("text/plain"), 1.toString())
            val subKategoriId = RequestBody.create(MediaType.get("text/plain"), 1.toString())
            val tglHabis = RequestBody.create(MediaType.get("text/plain"), tglKadaluarsa)
            val stokProduk = RequestBody.create(MediaType.get("text/plain"), stok)
            val hargaProduk = RequestBody.create(MediaType.get("text/plain"), hargaReplaced)
            val nama = RequestBody.create(MediaType.get("text/plain"), namaProduk)
            val deskripsi = RequestBody.create(MediaType.get("text/plain"), desc)

            createProduk(merchantId, kategoriId, subKategoriId, tglHabis,
                stokProduk, nama, hargaProduk, deskripsi, urlFoto)
        }
        else{
            when {
                fotoProduk.isNullOrEmpty() -> {
                    message.value = "Mohon upload foto produk"
                }
                namaMerchant.isNullOrEmpty() -> {
                    setTextError("Error, mohon masukkan nama merchant", editNamaMerchant)
                }
                kategori == 0 -> {
                    message.value = "Mohon memilih salah satu Kategori yang tersedia"
                }
                namaProduk.isNullOrEmpty() -> {
                    setTextError("Error, mohon masukkan nama produk", editNamaProduk)
                }
                tglKadaluarsa.isNullOrEmpty() -> {
                    message.value = "Error, Mohon pilih tanggal kadaluarsa"
                    editNamaMerchant.clearFocus()
                    editNamaProduk.clearFocus()
                    editTglKadaluarsa.requestFocus()
                    editTglKadaluarsa.findFocus()
                    editTglKadaluarsa.error = "Error, Mohon pilih tanggal kadaluarsa"
                }
                stok.isNullOrEmpty() -> {
                    setTextError("Error, mohon masukkan stok produk", editStok)
                }
                harga.isEmpty() -> {
                    message.value = "Error, mohon masukkan harga produk"
                    editHarga.error = "Error, mohon masukkan harga produk"
                    editHarga.requestFocus()
                    editHarga.findFocus()
                }
                desc.isNullOrEmpty() -> {
                    setTextError("Error, mohon masukkan deskripsi produk", editDesc)
                }
                else -> {
                    message.value = "Error, terjadi kesalahan yang tidak diketahui"
                }
            }
        }
    }

    private fun createProduk(merchant_id: RequestBody, kategori_id: RequestBody,
                             sub_kategori_id: RequestBody, tgl_kadaluarsa: RequestBody,
                             stok: RequestBody, nama: RequestBody, harga: RequestBody,
                             deskripsi: RequestBody, url_foto: MultipartBody.Part){
        isShowLoading.value = true

        RetrofitUtils.createProduk(merchant_id, kategori_id, sub_kategori_id, tgl_kadaluarsa,
            stok, nama, harga, deskripsi, url_foto,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccessRegister){
                        dialogSucces()
                        navController.popBackStack()
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
                    message.value = t.message
                }
            })
    }

    private fun dialogSucces() {
        if (activity != null){
            val alert = AlertDialog.Builder(activity)
            alert.setTitle(Constant.berhasil)
            alert.setMessage("Berhasil menambah produk, mohon tunggu proses verifikasi dalam waktu 1x24 jam")
            alert.setPositiveButton(
                Constant.iya
            ) { dialog, _ ->
                dialog.dismiss()
            }

            alert.show()
        }
        else{
            message.value = "Mohon mulai ulang aplikasi"
        }
    }
}