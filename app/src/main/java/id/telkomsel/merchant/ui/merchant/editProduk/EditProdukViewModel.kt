package id.telkomsel.merchant.ui.merchant.editProduk

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
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import faranjit.currency.edittext.CurrencyEditText
import id.telkomsel.merchant.R
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelKategori
import id.telkomsel.merchant.model.ModelMerchant
import id.telkomsel.merchant.model.ModelProduk
import id.telkomsel.merchant.model.response.ModelResponse
import id.telkomsel.merchant.model.response.ModelResponseDaftarKategori
import id.telkomsel.merchant.model.response.ModelResponseDaftarMerchant
import id.telkomsel.merchant.ui.merchant.addProduk.AdapterPickMerchant
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.SpinnerKategoriAdapter
import id.telkomsel.merchant.utils.adapter.dismissKeyboard
import id.telkomsel.merchant.utils.adapter.showLog
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("StaticFieldLeak")
class EditProdukViewModel(
    private val activity: Activity?,
    private val context: Context?,
    private val navController: NavController,
    private val savedData: DataSave,
    private val spinnerKategori: AppCompatSpinner,
    private val editNamaMerchant: TextInputLayout,
    private val editNamaProduk: TextInputLayout,
    private val editTglKadaluarsa: TextInputLayout,
    private val editStok: TextInputLayout,
    private val editDesc: TextInputLayout,
    private val editHarga: CurrencyEditText,
    private val editPromo: TextInputLayout,
    private val editPoin: CurrencyEditText
) : BaseViewModel() {
    val dataProduk = MutableLiveData<ModelProduk>()
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

    fun setAdapterKategori() {
        listKategori.clear()

        adapterKategori = SpinnerKategoriAdapter(
            activity,
            listKategori, savedData.getDataMerchant()?.level != Constant.levelMerchant
                )
        spinnerKategori.adapter = adapterKategori
    }

    fun setData(){
        etNamaProduk.value = dataProduk.value?.nama
        etTglKadaluarsa.value = dataProduk.value?.tgl_kadaluarsa
        etFotoProduk.value = Uri.parse(dataProduk.value?.url_foto)
        etStok.value = dataProduk.value?.stok.toString()
        etHarga.value = dataProduk.value?.harga.toString()
        etPromo.value = dataProduk.value?.promo
        etPoin.value = dataProduk.value?.jumlah_poin.toString()
        etDeskripsi.value = dataProduk.value?.deskripsi

        listKategori.clear()
        listKategori.add(ModelKategori(0, 0, Constant.pilihKategori, false))
        val list = etDataMerchant.value?.sub_kategori
        if (list != null){
            listKategori.addAll(list)
            adapterKategori.notifyDataSetChanged()
        }


        for (i in listKategori.indices){
            if (listKategori[i].id == dataProduk.value?.sub_kategori_id){
                spinnerKategori.setSelection(i)
            }
        }
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

    fun getDaftarSubKategoriByMerchant(merchant_id: Int) {
        isShowLoading.value = true

        RetrofitUtils.getDaftarSubKategoriByMerchant(merchant_id,
            object : Callback<ModelResponseDaftarKategori> {
                override fun onResponse(
                    call: Call<ModelResponseDaftarKategori>,
                    response: Response<ModelResponseDaftarKategori>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess) {
                        for (i in result.data.indices){
                            if (result.data[i].id == dataProduk.value?.sub_kategori_id){
                                listKategori.add(result.data[i])
                                adapterKategori.notifyDataSetChanged()
                            }
                        }

                        for (i in result.data.indices){
                            if (result.data[i].id != listKategori[0].id){
                                listKategori.add(result.data[i])
                                adapterKategori.notifyDataSetChanged()
                            }
                        }
                    } else {
                        message.value = result?.message
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseDaftarKategori>,
                    t: Throwable
                ) {
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
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
                    val dateFormatter = SimpleDateFormat(Constant.dateFormat4, Locale.US)
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
        editPromo.error = null
        editPoin.error = null
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
        val produkId = dataProduk.value?.id.toString()
        val namaProduk = etNamaProduk.value
        val subKategori = listKategori[spinnerKategori.selectedItemPosition].id
        val kategori = listKategori[spinnerKategori.selectedItemPosition].kategori_id
        val tglKadaluarsa = etTglKadaluarsa.value
        val stok = etStok.value
        val harga = editHarga.currencyText.toString()
        val promo = etPromo.value
        val poin = editPoin.currencyText.toString()
        val desc = etDeskripsi.value
        val fotoProduk = etFotoProduk.value?.path

        if (dataMerchant != null && produkId.isNotEmpty() && !namaProduk.isNullOrEmpty()
            && !tglKadaluarsa.isNullOrEmpty()
            && (subKategori != 0) && (kategori != 0) && harga.isNotEmpty() && !desc.isNullOrEmpty()
            && !stok.isNullOrEmpty() && !promo.isNullOrEmpty() && !fotoProduk.isNullOrEmpty() && poin.isNotEmpty()
        ) {
            val hargaReplaced = harga.replace(".", "")
            val poinReplaced = poin.replace(".", "")

            val status = if (savedData.getDataMerchant()?.level == Constant.levelCSO) Constant.statusActive
                else Constant.statusRequest

            editProduk(status, produkId, dataMerchant.id.toString(), kategori.toString(), subKategori.toString(), tglKadaluarsa,
                stok, namaProduk,
                hargaReplaced, promo, poinReplaced, desc, dataMerchant.regional, dataMerchant.branch, dataMerchant.cluster)
        }
        else{
            when {
                dataMerchant == null -> {
                    message.value = "Error, terjadi kesalahan database saat mengambil data merchant"
                    setTextError("Error, terjadi kesalahan database saat mengambil data merchant", editNamaMerchant)
                }
                produkId.isEmpty() -> {
                    message.value = "Error, terjadi kesalahan database saat mengambil data produk"
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
                promo.isNullOrEmpty() -> {
                    setTextError("Error, mohon masukkan promo", editPromo)
                }
                poin.isEmpty() -> {
                    message.value = "Error, mohon masukkan poin penukaran"
                    editHarga.error = "Error, mohon masukkan poin penukaran"
                    editPoin.requestFocus()
                    editPoin.findFocus()
                }
                desc.isNullOrEmpty() -> {
                    setTextError("Error, mohon masukkan deskripsi produk", editDesc)
                }
                fotoProduk.isNullOrEmpty() -> {
                    message.value = "Mohon upload foto produk"
                }
                else -> {
                    message.value = "Error, terjadi kesalahan yang tidak diketahui"
                }
            }
        }
    }

    private fun editProduk(status: String, produk_id: String, merchant_id: String, kategori_id: String,
                             sub_kategori_id: String, tgl_kadaluarsa: String,
                             stok: String, nama: String, harga: String,
                             promo: String, poin: String,
                             deskripsi: String, regional: String,
                             branch: String, cluster: String){
        isShowLoading.value = true

        RetrofitUtils.editProduk(status, produk_id, merchant_id, kategori_id, sub_kategori_id, tgl_kadaluarsa,
            stok, nama, harga, promo, poin, deskripsi, regional, branch, cluster,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        if (savedData.getDataMerchant()?.level == Constant.levelCSO)
                            message.value = "Berhasil mengedit produk"
                        else
                            message.value = "Berhasil mengedit produk, mohon tunggu proses verifikasi dalam waktu 1x24 jam"

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
                    message.value = t.message
                }
            })
    }

    fun editFotoProfilProduk(produk_id: RequestBody, level: RequestBody,
                         url_foto: MultipartBody.Part){
        isShowLoading.value = true
        showLog("edit")

        RetrofitUtils.editFotoProfilProduk(produk_id, level, url_foto,
            object : Callback<ModelResponse> {
                override fun onResponse(
                    call: Call<ModelResponse>,
                    response: Response<ModelResponse>
                ) {
                    isShowLoading.value = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        if (savedData.getDataMerchant()?.level == Constant.levelSBP){
                            message.value = "Berhasil mengupload foto produk, mohon tunggu proses verifikasi dalam waktu 1x24 jam"
                        }
                        else{
                            message.value = "Berhasil mengupload foto produk"
                        }
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