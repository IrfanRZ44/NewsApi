package id.telkomsel.merchant.ui.merchant.addProduk

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import faranjit.currency.edittext.CurrencyEditText
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelKategori
import id.telkomsel.merchant.model.ModelMerchant
import id.telkomsel.merchant.model.response.ModelResponse
import id.telkomsel.merchant.model.response.ModelResponseDaftarMerchant
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
    val etDataMerchant = MutableLiveData<ModelMerchant>()
    val etNamaProduk = MutableLiveData<String>()
    val etTglKadaluarsa = MutableLiveData<String>()
    val etFotoProduk = MutableLiveData<Uri>()
    val etStok = MutableLiveData<String>()
    val etHarga = MutableLiveData<String>()
    val etPromo = MutableLiveData<String>()
    val etPoin = MutableLiveData<String>()
    val etDeskripsi = MutableLiveData<String>()
    private val listKategori = ArrayList<ModelKategori>()
    private lateinit var adapterKategori : SpinnerKategoriAdapter
    val listMerchant = ArrayList<ModelMerchant>()
    lateinit var adapterPickMerchant: AdapterPickMerchant
    lateinit var btmSheet: BottomSheetDialog
    var startPage = 0

    fun initAdapter(rcMerchant: RecyclerView?) {
        rcMerchant?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapterPickMerchant = AdapterPickMerchant(
            listMerchant
        ) { item: ModelMerchant -> onClickPickMerchant(item) }
        rcMerchant?.adapter = adapterPickMerchant
    }

    fun getDataMerchant(search: String?, cluster: String, userRequest: String) {
        isShowLoading.value = true
        adapterPickMerchant.notifyDataSetChanged()
        val textStatus = btmSheet.findViewById<AppCompatTextView>(R.id.textStatus)

        RetrofitUtils.getPickMerchant(cluster, userRequest, startPage, search,
            object : Callback<ModelResponseDaftarMerchant> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<ModelResponseDaftarMerchant>,
                    response: Response<ModelResponseDaftarMerchant>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        listMerchant.addAll(result.dataMerchant)
                        adapterPickMerchant.notifyDataSetChanged()

                        startPage += 25
                        if (listMerchant.size == 0){
                            if (search.isNullOrEmpty()){
                                textStatus?.text = Constant.noMerchant
                            }
                            else{
                                textStatus?.text = "Maaf, belum ada data merchant dengan nama $search"
                            }
                        }
                        else{
                            if (startPage > 0 && result.dataMerchant.isEmpty()){
                                Toast.makeText(context, "Maaf, sudah tidak ada lagi data", Toast.LENGTH_LONG).show()
                                textStatus?.text = ""
                            }
                        }
                    }
                    else{
                        textStatus?.text = result?.message
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseDaftarMerchant>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    textStatus?.text = t.message
                }
            })
    }

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

    private fun onClickPickMerchant(item: ModelMerchant){
        listKategori.clear()
        etDataMerchant.value = item
        listKategori.add(ModelKategori(0, 0, Constant.pilihKategori, false))
        listKategori.addAll(item.sub_kategori)
        adapterKategori.notifyDataSetChanged()
        btmSheet.dismiss()
    }

    fun onClickAddProduk(){
        setNullError()
        activity?.let { dismissKeyboard(it) }

        val dataMerchant = etDataMerchant.value
        val namaProduk = etNamaProduk.value
        val subKategori = listKategori[spinnerKategori.selectedItemPosition].id
        val kategori = listKategori[spinnerKategori.selectedItemPosition].kategori_id
        val tglKadaluarsa = etTglKadaluarsa.value
        val stok = etStok.value
        val harga = editHarga.currencyText.toString()
        val desc = etDeskripsi.value
        val fotoProduk = etFotoProduk.value?.path

        if (dataMerchant != null && !namaProduk.isNullOrEmpty()
            && !tglKadaluarsa.isNullOrEmpty()
            && (subKategori != 0) && (kategori != 0) && harga.isNotEmpty() && !desc.isNullOrEmpty()
            && !stok.isNullOrEmpty() && !fotoProduk.isNullOrEmpty()
        ) {
            val hargaReplaced = harga.replace(".", "")

            val fileProduk = File(fotoProduk)
            val urlFoto = MultipartBody.Part.createFormData("url_foto", fileProduk.name, RequestBody.create(
                MediaType.get("image/*"), fileProduk))
            val merchantId = RequestBody.create(MediaType.get("text/plain"), dataMerchant.id.toString())
            val regional = RequestBody.create(MediaType.get("text/plain"), dataMerchant.regional)
            val branch = RequestBody.create(MediaType.get("text/plain"), dataMerchant.branch)
            val cluster = RequestBody.create(MediaType.get("text/plain"), dataMerchant.cluster)
            val kategoriId = RequestBody.create(MediaType.get("text/plain"), kategori.toString())
            val subKategoriId = RequestBody.create(MediaType.get("text/plain"), subKategori.toString())
            val tglHabis = RequestBody.create(MediaType.get("text/plain"), tglKadaluarsa)
            val stokProduk = RequestBody.create(MediaType.get("text/plain"), stok)
            val hargaProduk = RequestBody.create(MediaType.get("text/plain"), hargaReplaced)
            val nama = RequestBody.create(MediaType.get("text/plain"), namaProduk)
            val deskripsi = RequestBody.create(MediaType.get("text/plain"), desc)

            createProduk(merchantId, kategoriId, subKategoriId, tglHabis,
                stokProduk, nama, hargaProduk, deskripsi, regional, branch, cluster, urlFoto)
        }
        else{
            when {
                fotoProduk.isNullOrEmpty() -> {
                    message.value = "Mohon upload foto produk"
                }
                dataMerchant == null -> {
                    message.value = "Error, terjadi kesalahan database saat mengambil data merchant"
                    setTextError("Error, terjadi kesalahan database saat mengambil data merchant", editNamaMerchant)
                }
                kategori == 0 -> {
                    message.value = "Mohon memilih salah satu Kategori yang tersedia"
                }
                subKategori == 0 -> {
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
                             deskripsi: RequestBody, regional: RequestBody,
                             branch: RequestBody, cluster: RequestBody,
                             url_foto: MultipartBody.Part){
        isShowLoading.value = true

        RetrofitUtils.createProduk(merchant_id, kategori_id, sub_kategori_id, tgl_kadaluarsa,
            stok, nama, harga, deskripsi, regional, branch, cluster, url_foto,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        message.value = "Berhasil menambah produk, mohon tunggu proses verifikasi dalam waktu 1x24 jam"
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
}