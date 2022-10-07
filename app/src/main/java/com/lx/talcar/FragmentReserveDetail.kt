package com.lx.talcar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lx.talcar.api.ChatClient
import com.lx.talcar.api.TalcarClient
import com.lx.talcar.databinding.FragmentReserveDetailBinding
import com.lx.talcar.response.CUDReponse
import com.lx.talcar.response.MemResponse
import com.lx.talcar.response.RatingResponse
import com.lx.talcar.response.ReservationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentReserveDetail : Fragment() {

    private  var _binding: FragmentReserveDetailBinding? =null
    val binding get() = _binding!!
    lateinit var mainActivity:MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentReserveDetailBinding.inflate(inflater, container, false)

        mainActivity = context as MainActivity

        var reId = arguments?.getInt("reId")

        TalcarClient.api.checkReReturn(
            reId = reId
        ).enqueue(object: Callback<ReservationResponse> {
            override fun onResponse(call: Call<ReservationResponse>, response: Response<ReservationResponse>) {
                var item = response.body()?.data

                _binding?.apply {
                    detailIdOutput.text = item?.get(0)?.reId.toString()
                    detailCarOutput.text = item?.get(0)?.reModel + "(" + item?.get(0)?.reCarNumber + ")"
                    detailDateOutput.text = item?.get(0)?.reReserveDate + " " + item?.get(0)?.reReserveTime + " ~ " + item?.get(0)?.reReturnDate + " " + item?.get(0)?.reReturnTime
                }

                ChatClient.api.getRecMem(
                    shMem = item?.get(0)?.reMem
                ).enqueue(object: Callback<MemResponse> {
                    override fun onResponse(call: Call<MemResponse>, response: Response<MemResponse>) {
                        var item = response.body()?.data

                        _binding?.apply {
                            reserveDetailReMem.text = "이름 : " + item?.get(0)?.memId
                            reserveDetailReTel.text = "연락처 : " + item?.get(0)?.memTel
                        }

                    }
                    override fun onFailure(call: Call<MemResponse>, t: Throwable) {
                        Log.d("오류", "$t")
                    }
                })

                TalcarClient.api.getRating(
                    memId = item?.get(0)?.reMem
                ).enqueue(object: Callback<RatingResponse> {
                    override fun onResponse(call: Call<RatingResponse>, response: Response<RatingResponse>) {
                        var item = response.body()?.data
                        if(item?.size!! >0) {
                            _binding?.reserveDetailReRating?.text = "평점" + item?.get(0)?.rating.toString()
                        }
                    }
                    override fun onFailure(call: Call<RatingResponse>, t: Throwable) {
                        Log.d("오류", "$t")
                    }
                })
            }
            override fun onFailure(call: Call<ReservationResponse>, t: Throwable) {
                Log.d("오류", "$t")
            }
        })

        _binding?.detailReadyButton?.setOnClickListener {
            TalcarClient.api.confirmReserve(
                reId = reId,
                reVal = "o"
            ).enqueue(object: Callback<CUDReponse> {
                override fun onResponse(call: Call<CUDReponse>, response: Response<CUDReponse>) {
                    mainActivity.supportFragmentManager.beginTransaction().replace(R.id.fragmentView, FragmentReserveList()).commit()
                }
                override fun onFailure(call: Call<CUDReponse>, t: Throwable) {
                    Log.d("오류", "$t")
                }
            })
        }

        _binding?.detailCancelButton?.setOnClickListener {
            TalcarClient.api.confirmReserve(
                reId = reId,
                reVal = "x"
            ).enqueue(object: Callback<CUDReponse> {
                override fun onResponse(call: Call<CUDReponse>, response: Response<CUDReponse>) {
                    mainActivity.supportFragmentManager.beginTransaction().replace(R.id.fragmentView, FragmentReserveList()).commit()
                }
                override fun onFailure(call: Call<CUDReponse>, t: Throwable) {
                    Log.d("오류", "$t")
                }
            })
        }

        return binding.root
    }

}