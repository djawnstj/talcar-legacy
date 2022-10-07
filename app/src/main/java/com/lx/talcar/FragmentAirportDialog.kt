package com.lx.talcar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lx.talcar.api.TalcarClient
import com.lx.talcar.databinding.FragmentAirportDialogBinding
import com.lx.talcar.response.CUDReponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentAirportDialog : DialogFragment() {

    private  var _binding: FragmentAirportDialogBinding? =null
    val binding get() = _binding!!
    lateinit var mainActivity:MainActivity

    lateinit var bundle:Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAirportDialogBinding.inflate(inflater, container, false)

        mainActivity = context as MainActivity

        showCarImage("/images/benz.jpeg")

        var apAddress = arguments?.getString("apAddress")
        var airReserveDate = arguments?.getString("airReserveDate")
        var airReserveTime = arguments?.getString("airReserveTime")
        var airReturnDate = arguments?.getString("airReturnDate")
        var airReturnTime = arguments?.getString("airReturnTime")
        var airModel = arguments?.getString("airModel")
        var airCarNumber = arguments?.getString("airCarNumber")
        var airMem = arguments?.getString("airMem")
        var airPrice = arguments?.getInt("airPrice")

        _binding?.apAddress?.text = "공항 : $apAddress"
        _binding?.apDate?.text = "기간 : \n${airReserveDate} ${airReserveTime} ~ ${airReturnDate} ${airReturnTime}"
        _binding?.apCar?.text = "차량 : ${airModel}(${airCarNumber}"
        _binding?.apMem?.text = "대여자 : $airMem"

        _binding?.apDialogCloseBtn?.setOnClickListener {
            dismiss()
        }

        _binding?.apChatButton?.setOnClickListener {
            dismiss()
            bundle = Bundle()
            var fragmentChat=FragmentChat()
            bundle.putString("shMem", airMem)
            fragmentChat.arguments = bundle
            mainActivity.supportFragmentManager.beginTransaction().add(R.id.fragmentView, fragmentChat).addToBackStack(null).commit()
        }

        _binding?.apReserveButton?.setOnClickListener {
            setReservation(airMem, airPrice, airModel, airCarNumber, airReserveDate, airReturnDate, airReserveTime, airReturnTime)
        }

        return binding.root
    }

    fun setReservation(airMem:String?, airPrice:Int?, airModel:String?, airCarNumber:String?, reserveDate:String?, returnDate:String?, reserveTime:String?, returnTime:String?) {
        TalcarClient.api.setReservation(
            reShMem = airMem,
            reserveDate = reserveDate,
            returnDate = returnDate,
            reserveTime = reserveTime,
            returnTime = returnTime,
            rePrice = airPrice,
            reModel = airModel,
            reCarNumber = airCarNumber
        ).enqueue(object: Callback<CUDReponse> {
            override fun onResponse(call: Call<CUDReponse>, response: Response<CUDReponse>) {
                dismiss()
            }
            override fun onFailure(call: Call<CUDReponse>, t: Throwable) {
                Log.d("오류", "$t")
            }
        })
    }

    fun showCarImage(path:String?) {
        var url = "http://14.55.65.168/image.do?image=${path}"
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .dontAnimate()
            .into(_binding?.apImage!!)
    }

}