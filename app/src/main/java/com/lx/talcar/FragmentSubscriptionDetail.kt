package com.lx.talcar

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.lx.talcar.adapter.RequestShareAdapter
import com.lx.talcar.adapter.RequestSubRecyclerAdapter
import com.lx.talcar.api.ChatClient
import com.lx.talcar.api.TalcarClient
import com.lx.talcar.databinding.FragmentReserveDetailBinding
import com.lx.talcar.databinding.FragmentSubscriptionDetailBinding
import com.lx.talcar.response.*
import com.lx.talcar.util.AppData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FragmentSubscriptionDetail : DialogFragment() {

    private  var _binding: FragmentSubscriptionDetailBinding? =null
    val binding get() = _binding!!
    lateinit var mainActivity:MainActivity

    lateinit var bundle: Bundle

    lateinit var reserveDate:String
    lateinit var reserveTime:String
    lateinit var returnDate:String
    lateinit var returnTime:String
    lateinit var reMem:String
    lateinit var reModel:String
    lateinit var reCarNumber:String
    var subCarCategory = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSubscriptionDetailBinding.inflate(inflater, container, false)

        mainActivity = context as MainActivity

        var subId = arguments?.getInt("subId")

        _binding?.subDialogCloseBtn?.setOnClickListener {
            dismiss()
        }

        TalcarClient.api.getSub(
            subId = subId
        ).enqueue(object: Callback<SubscriptionsResponse> {
            override fun onResponse(call: Call<SubscriptionsResponse>, response: Response<SubscriptionsResponse>) {
                var item = response.body()?.data

                _binding?.apply {
                    subDetailReMem.text = "아이디 : " + item?.get(0)?.subMem
                    subDate.text = "기간 : " + item?.get(0)?.subReserveDate + " ~ " + item?.get(0)?.subReturnDate
                    subTime.text = "시간 : " + item?.get(0)?.subReserveTime + " ~ " + item?.get(0)?.subReturnTime
                }
                item?.get(0)?.subMem?.let { setReMem(it) }
                item?.get(0)?.subReserveDate?.let { setReserveDate(it) }
                item?.get(0)?.subReserveTime?.let { setReserveTime(it) }
                item?.get(0)?.subReturnDate?.let { setReturnDate(it) }
                item?.get(0)?.subReturnTime?.let { setReturnTime(it) }

                ChatClient.api.getRecMem(
                    shMem = item?.get(0)?.subMem
                ).enqueue(object: Callback<MemResponse> {
                    override fun onResponse(call: Call<MemResponse>, response: Response<MemResponse>) {
                        var item = response.body()?.data

                        _binding?.apply {
                            subDetailReTel.text = "연락처 : " + item?.get(0)?.memTel
                        }

                    }
                    override fun onFailure(call: Call<MemResponse>, t: Throwable) {
                        Log.d("오류", "$t")
                    }
                })

                TalcarClient.api.getRating(
                    memId = item?.get(0)?.subMem
                ).enqueue(object: Callback<RatingResponse> {
                    override fun onResponse(call: Call<RatingResponse>, response: Response<RatingResponse>) {
                        var item = response.body()?.data
                        if(item?.size!! >0) {
                            _binding?.subDetailReRating?.text = "평점" + item?.get(0)?.rating.toString()
                        }
                    }
                    override fun onFailure(call: Call<RatingResponse>, t: Throwable) {
                        Log.d("오류", "$t")
                    }
                })
            }
            override fun onFailure(call: Call<SubscriptionsResponse>, t: Throwable) {
                Log.d("오류", "$t")
            }
        })

        TalcarClient.api.getRegiCar(
            regiMem = AppData.memId
        ).enqueue(object: Callback<RegiCarResponse> {
            override fun onResponse(call: Call<RegiCarResponse>, response: Response<RegiCarResponse>) {
                var item = response.body()?.data
                var array = ArrayList<String>()
                for(index in 0 .. item?.size!!-1) {
                    array.add("${item[index].regiCar}(${item[index].regiCarNumber})")
                }
                setCategoryArray(array)

                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, array)
                _binding?.subSpinner?.adapter = adapter
            }
            override fun onFailure(call: Call<RegiCarResponse>, t: Throwable) {
                Log.d("오류", "$t")
            }
        })

        _binding?.subSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("선택해주세요")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                //예약 등록, 요청, 이용 중
                when(position) {
                    1 -> {
                        var selectedCar: String = _binding?.subSpinner?.selectedItem.toString()
                        val arr = selectedCar.split("(")
                        setReModel(arr[0])
                        setReCarNumber(arr[1].substring(0, arr[1].length-1))
                    }
                    else -> {
                        var selectedCar: String = _binding?.subSpinner?.selectedItem.toString()
                        val arr = selectedCar.split("(")
                        setReModel(arr[0])
                        setReCarNumber(arr[1].substring(0, arr[1].length-1))
                    }
                }
            }

        }

        _binding?.subDetailReadyButton?.setOnClickListener {
            var dReserveDate = "$reserveDate 00:00:00"
            var dReturnDate = "$returnDate 00:00:00"
            var calReserveDate = SimpleDateFormat("yyyy-MM-dd 00:00:00").parse(dReserveDate)
            var calReturnDate = SimpleDateFormat("yyyy-MM-dd 00:00:00").parse(dReturnDate)
            var calcuDate = (calReturnDate.time - calReserveDate.time) / (60 * 60 * 24 * 1000)
            println("날짜 차이 : $calcuDate")
            for(index in 0..calcuDate) {
                var dateType = LocalDate.parse(reserveDate, DateTimeFormatter.ISO_DATE)
                var calculDate = dateType.plusDays(index)
                var date = calculDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                println("날짜 형변 : $calculDate, $reserveDate")
                TalcarClient.api.setReserve(
                    reMem = reMem,
                    reShMem = AppData.memId,
                    reReserveDate = date,
                    reReserveTime = reserveTime,
                    reReturnDate = date,
                    reReturnTime = returnTime,
                    rePrice = _binding?.subPriceInput?.getText().toString(),
                    reModel = reModel,
                    reCarNumber = reCarNumber
                ).enqueue(object : Callback<CUDReponse> {
                    override fun onResponse(
                        call: Call<CUDReponse>,
                        response: Response<CUDReponse>
                    ) {

                    }

                    override fun onFailure(call: Call<CUDReponse>, t: Throwable) {
                        Log.d("오류", "$t")
                    }
                })
            }
            activity?.let{
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
            }
        }

        _binding?.subDetailChatButton?.setOnClickListener {
            dismiss()
            bundle = Bundle()
            var fragmentChat=FragmentChat()
            bundle.putString("shMem", reMem)
            fragmentChat.arguments = bundle
            mainActivity.supportFragmentManager.beginTransaction().add(R.id.fragmentView, fragmentChat).addToBackStack(null).commit()
        }

        return binding.root
    }

    fun setCategoryArray(array:ArrayList<String>) {
        subCarCategory = array

        println("차량 리스트 : $subCarCategory")
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

    @JvmName("setReMem1")
    fun setReMem(memId:String) {
        reMem = memId
    }

    @JvmName("setReModel1")
    fun setReModel(model:String) {
        reModel = model
    }

    @JvmName("setReCarNumber1")
    fun setReCarNumber(carNumber:String) {
        reCarNumber = carNumber
    }
}