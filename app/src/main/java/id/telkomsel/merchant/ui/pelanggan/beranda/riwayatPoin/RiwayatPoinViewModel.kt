package id.telkomsel.merchant.ui.pelanggan.beranda.riwayatPoin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.telkomsel.merchant.base.BaseViewModel
import id.telkomsel.merchant.model.ModelRiwayatPoin
import id.telkomsel.merchant.model.response.ModelResponseRiwayatPoin
import id.telkomsel.merchant.utils.Constant
import id.telkomsel.merchant.utils.DataSave
import id.telkomsel.merchant.utils.RetrofitUtils
import id.telkomsel.merchant.utils.adapter.dismissKeyboard
import id.telkomsel.merchant.utils.adapter.getDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("StaticFieldLeak")
class RiwayatPoinViewModel(
    private val activity: Activity?,
    private val rcRequest: RecyclerView,
    private val savedData: DataSave
) : BaseViewModel() {
    val listData = ArrayList<ModelRiwayatPoin>()
    lateinit var adapter: AdapterRiwayatPoin
    var isSearching = false
    var startPage = 0
    val etTglMulai = MutableLiveData<String>()
    val etTglSelesai = MutableLiveData<String>()

    fun initAdapter() {
        rcRequest.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapter = AdapterRiwayatPoin(listData)
        rcRequest.adapter = adapter
    }

    fun getDateTglMulai() {
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
                    etTglMulai.value = dateFormatter.format(dateSelected.time)

                    startPage = 0
                    listData.clear()
                    adapter.notifyDataSetChanged()
                    checkRangeDate()
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

    fun getDateTglSelesai() {
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
                    etTglSelesai.value = dateFormatter.format(dateSelected.time)

                    startPage = 0
                    listData.clear()
                    adapter.notifyDataSetChanged()
                    checkRangeDate()
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

    @SuppressLint("SimpleDateFormat")
    fun setDefaultDate(){
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -1)
        val result = cal.time
        etTglMulai.value = SimpleDateFormat(Constant.dateFormat1).format(result)
        etTglSelesai.value = getDate(Constant.dateFormat1)

        checkRangeDate()
    }

    fun checkRangeDate(){
        val etDateStart = etTglMulai.value?.split("-")
        val etDateEnd = etTglSelesai.value?.split("-")
        if (!etDateStart.isNullOrEmpty() && !etDateEnd.isNullOrEmpty()){
            getRiwayatPoin(etDateStart[0], etDateStart[1], etDateStart[2],
                etDateEnd[0], etDateEnd[1], etDateEnd[2])
        }
        else{
            status.value = "Error, tanggal mulai dan tanggal selesai tidak boleh kosong"
        }
    }

    private fun getRiwayatPoin(startTanggal: String, startBulan: String, startTahun: String,
                               endTanggal: String, endBulan: String, endTahun: String) {
        isShowLoading.value = true
        adapter.notifyDataSetChanged()

        RetrofitUtils.getRiwayatPoin(savedData.getDataPelanggan()?.nomor_mkios?:"", startPage,
            startTanggal, startBulan, startTahun, endTanggal, endBulan, endTahun,
            object : Callback<ModelResponseRiwayatPoin> {
                override fun onResponse(
                    call: Call<ModelResponseRiwayatPoin>,
                    response: Response<ModelResponseRiwayatPoin>
                ) {
                    isShowLoading.value = false
                    isSearching = false
                    val result = response.body()

                    if (result?.message == Constant.reffSuccess){
                        for (i in result.dataRiwayat.indices){
                            listData.add(result.dataRiwayat[i])
                            adapter.notifyDataSetChanged()
                        }

                        startPage += 25
                        message.value = ""
                    }
                    else{
                        if (startPage == 0){
                            message.value =
                                "Maaf, belum ada data riwayat poin pada range tanggal $startTanggal-$startBulan-$startTahun - $endTanggal-$endBulan-$endTahun"
                        }
                        else{
                            message.value = ""
                            status.value = result?.message
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ModelResponseRiwayatPoin>,
                    t: Throwable
                ) {
                    isSearching = false
                    isShowLoading.value = false
                    message.value = t.message
                }
            })
    }
}