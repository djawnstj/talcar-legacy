package com.lx.talcar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.lx.talcar.api.TalcarClient
import com.lx.talcar.api.TmapClient
import com.lx.talcar.databinding.FragmentSubscriptionBinding
import com.lx.talcar.response.CUDReponse
import com.lx.talcar.response.TalcarListResponse
import com.lx.talcar.response.TmapSearchResponse
import com.lx.talcar.util.AppData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class FragmentSubscription : Fragment() {

    private  var _binding: FragmentSubscriptionBinding? =null
    val binding get() = _binding!!
    lateinit var mainActivity:MainActivity

    lateinit var bundle: Bundle

    lateinit var reserveDate:String
    lateinit var reserveTime:String
    lateinit var returnDate:String
    lateinit var returnTime:String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSubscriptionBinding.inflate(inflater, container, false)

        mainActivity = context as MainActivity

        val nowDate = LocalDateTime.now()
        val cal = Calendar.getInstance()

        _binding?.subscriptionReserveDate?.setOnClickListener {
            val listener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    var date:String = year.toString() + "-" + (monthOfYear.toInt()+1).toString() + "-" + dayOfMonth
                    _binding?.subscriptionReserveDate?.text = date
                    setReserveDate(date)
                }
            val dialog = DatePickerDialog(mainActivity, listener, nowDate.year, nowDate.monthValue-1, nowDate.dayOfMonth)
            dialog.show()
        }

        _binding?.subscriptionReturnDate?.setOnClickListener {
            val listener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    var date:String = year.toString() + "-" + (monthOfYear.toInt()+1).toString() + "-" + dayOfMonth
                    _binding?.subscriptionReturnDate?.text = date
                    setReturnDate(date)
                }
            val dialog = DatePickerDialog(mainActivity, listener, nowDate.year, nowDate.monthValue-1, nowDate.dayOfMonth)
            dialog.show()
        }

        _binding?.subscriptionReserveTime?.setOnClickListener {
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                var time:String? = null
                if(SimpleDateFormat("mm").format(cal.time).toInt()<30) {
                    time = SimpleDateFormat("HH").format(cal.time) + ":00"
                } else if(SimpleDateFormat("mm").format(cal.time).toInt()>=30) {
                    time = SimpleDateFormat("HH").format(cal.time) + ":30"
                }

                _binding?.subscriptionReserveTime?.setText(time)
                setReserveTime(time!!)
            }
            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        _binding?.subscriptionReturnTime?.setOnClickListener {
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                var time:String? = null
                if(SimpleDateFormat("mm").format(cal.time).toInt()<30) {
                    time = SimpleDateFormat("HH").format(cal.time) + ":00"
                } else if(SimpleDateFormat("mm").format(cal.time).toInt()>=30) {
                    time = SimpleDateFormat("HH").format(cal.time) + ":30"
                }

                _binding?.subscriptionReturnTime?.setText(time)
                setReturnTime(time!!)
            }
            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        _binding?.subscriptionButton?.setOnClickListener {
            setSub()
        }

        return binding.root
    }

    @JvmName("setReserveDate1")
    fun setReserveDate(reserveDate:String) {
        this.reserveDate = reserveDate
    }

    @JvmName("setReserveTime1")
    fun setReserveTime(reserveTime:String) {
        this.reserveTime = reserveTime
    }

    @JvmName("setReturnDate1")
    fun setReturnDate(returnDate:String) {
        this.returnDate = returnDate
    }

    @JvmName("setReturnTime1")
    fun setReturnTime(returnTime:String) {
        this.returnTime = returnTime
    }

    fun setSub() {
        TmapClient.api.getSearchLocation(
            keyword = _binding?.subscriptionAddressInput?.getText().toString(),
            currentLon = null,
            currentLat = null
        ).enqueue(object: Callback<TmapSearchResponse> {
            override fun onResponse(call: Call<TmapSearchResponse>, response: Response<TmapSearchResponse>) {
                var data = response.body()?.searchPoiInfo?.pois?.poi
                if(data!!.size>0) {
                    TalcarClient.api.setSub(
                        subMem = AppData.memId,
                        reserveDate = reserveDate,
                        reserveTime = reserveTime,
                        returnDate = returnDate,
                        returnTime = returnTime,
                        location = "POINT(${data.get(0).noorLon} ${data.get(0).noorLat})",
                        subAddress = "${data.get(0).upperAddrName}시 ${data.get(0).middleAddrName} ${data.get(0).lowerAddrName} ${data.get(0).roadName} ${data.get(0).firstBuildNo} ${data.get(0).name}"
                    ).enqueue(object : Callback<CUDReponse> {
                        override fun onResponse(
                            call: Call<CUDReponse>,
                            response: Response<CUDReponse>
                        ) {
                            activity?.let{
                                val intent = Intent(context, MainActivity::class.java)
                                startActivity(intent)
                            }
                        }

                        override fun onFailure(call: Call<CUDReponse>, t: Throwable) {
                            Log.d("오류", "$t")
                        }
                    })
                }

            }
            override fun onFailure(call: Call<TmapSearchResponse>, t: Throwable) {
                Log.d("오류", "$t")
            }
        })
    }

}