package com.lx.talcar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.lx.talcar.api.TalcarClient
import com.lx.talcar.databinding.FragmentLoginBinding
import com.lx.talcar.response.MemResponse
import com.lx.talcar.response.ReservationResponse
import com.lx.talcar.util.AppData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class LoginFragment : Fragment() {

    private  var _binding: FragmentLoginBinding? =null
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        showTitleImage()
        _binding?.loginButton?.setOnClickListener {
            var memId = _binding?.loginIdInput?.getText().toString()
            var memPw = _binding?.loginPwInput?.getText().toString()
            TalcarClient.api.loginCheck(
                memId = memId,
                memPw = memPw
            ).enqueue(object: Callback<MemResponse> {
                override fun onResponse(call: Call<MemResponse>, response: Response<MemResponse>) {
                    var item = response.body()?.data
                    if(item?.get(0)?.count==1) {
                        getMemId(memId)
                        activity?.let{
                            val intent = Intent(context, MainActivity::class.java)
                            startActivity(intent)
                        }
                    } else if(item?.get(0)?.count==0) {
                        _binding?.loginIdInput?.setText("")
                        _binding?.loginPwInput?.setText("")
                        Toast.makeText(getContext(), "ID 또는 비밀번호를 잘못 입력하셨습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<MemResponse>, t: Throwable) {
                    Log.d("오류", "$t")
                }
            })
        }

        return binding.root
    }

    fun getMemId(memId:String) {
        AppData.memId = memId
    }

    fun showTitleImage() {
        Glide.with(this)
            .load(R.raw.talcar_title)
            .into(_binding?.imageView10!!)
    }

}