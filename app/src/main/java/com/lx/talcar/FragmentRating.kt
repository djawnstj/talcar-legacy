package com.lx.talcar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.lx.talcar.api.TalcarClient
import com.lx.talcar.databinding.FragmentRatingBinding
import com.lx.talcar.response.CUDReponse
import com.lx.talcar.response.RegiCarResponse
import com.lx.talcar.util.AppData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentRating : DialogFragment() {

    private  var _binding: FragmentRatingBinding? =null
    val binding get() = _binding!!
    lateinit var mainActivity:MainActivity

    lateinit var bundle:Bundle
    private var rtReId:Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRatingBinding.inflate(inflater, container, false)

        var rtdMem = arguments?.getString("reShMem")
//        var reReturnAddress = arguments?.getString("reReturnAddress")
        var reModel = arguments?.getString("reModel")
        var reCarNumber = arguments?.getString("reCarNumber")

        TalcarClient.api.getRegiCarId(
            regiCar = reModel,
            regiCarNumber = reCarNumber
        ).enqueue(object : Callback<RegiCarResponse> {
            override fun onResponse(call: Call<RegiCarResponse>, response: Response<RegiCarResponse> ) {
                response.body()?.data?.get(0)?.let { setRtReId(it.regiId) }
            }
            override fun onFailure(call: Call<RegiCarResponse>, t: Throwable) {
                Log.d("오류", "$t")
            }
        })

        _binding?.ratingAddress?.text = "반납 위치 : LX공간정보아카데미"
        _binding?.ratingText?.text = "만족스러우셨나요? 후기를 작성해주세요."

        mainActivity = context as MainActivity

        _binding?.ratingCloseBtn?.setOnClickListener {
            activity?.let{
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
            }
        }

        _binding?.ratingCloseButton?.setOnClickListener {
            var rating:Float? = _binding?.ratingBar?.getRating()
            var rtComment:String = _binding?.ratingInput?.getText().toString()
            TalcarClient.api.setRating(
                rtMem = AppData.memId,
                rtdMem = rtdMem,
                rating = rating,
                rtReId = rtReId,
                rtComment = rtComment
            ).enqueue(object : Callback<CUDReponse> {
                override fun onResponse(call: Call<CUDReponse>, response: Response<CUDReponse> ) {
                    dismiss()
                    mainActivity.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentView, FragmentHome()).commit()
                }
                override fun onFailure(call: Call<CUDReponse>, t: Throwable) {
                    Log.d("오류", "$t")
                }
            })
        }

        return binding.root
    }

    fun setRtReId(regiId:Int) {
        this.rtReId = regiId
    }

}