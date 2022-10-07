package com.lx.talcar

import android.R
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.lx.talcar.adapter.AirportCarAdapter
import com.lx.talcar.adapter.RequestShareAdapter
import com.lx.talcar.adapter.RequestSubRecyclerAdapter
import com.lx.talcar.adapter.TalcarRecyclerAdapter
import com.lx.talcar.api.TalcarClient
import com.lx.talcar.databinding.FragmentAirportListBinding
import com.lx.talcar.databinding.FragmentHomeBinding
import com.lx.talcar.response.AirportListResponse
import com.lx.talcar.response.ReservationResponse
import com.lx.talcar.response.SubscriptionsResponse
import com.lx.talcar.response.TalcarListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class FragmentAirportList : Fragment() {

    private  var _binding: FragmentAirportListBinding? =null
    val binding get() = _binding!!
    lateinit var mainActivity:MainActivity

    var airportCarAdapter: AirportCarAdapter? = null
    lateinit var bundle:Bundle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAirportListBinding.inflate(inflater, container, false)

        mainActivity = context as MainActivity

        initRecyclerView()

        val airportCategory = resources.getStringArray(com.lx.talcar.R.array.airport_category)
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, airportCategory)
        _binding?.airportSpinner?.adapter = adapter

        _binding?.airportSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("선택해주세요")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                when(position) {
                    0 -> {
                        initRecyclerView()
                    }
                    else -> {
                        var airport: String = _binding?.airportSpinner?.selectedItem.toString()
                        TalcarClient.api.getAirportReserveList(
                            airport = airport
                        ).enqueue(object: Callback<AirportListResponse> {
                            override fun onResponse(call: Call<AirportListResponse>, response: Response<AirportListResponse>) {
                                var data = response.body()?.data
                                if(data?.size!!>0) {
                                    airportCarAdapter?.items = data as ArrayList<AirportListResponse.Data>
                                    airportCarAdapter?.notifyDataSetChanged()
                                }
                            }
                            override fun onFailure(call: Call<AirportListResponse>, t: Throwable) {
                                Log.d("오류", "$t")
                            }
                        })
                    }
                }

                airportCarAdapter?.setOnItemClickListener(object: AirportCarAdapter.OnItemClickListener {
                    override fun onItemClick(v: View, data: AirportListResponse.Data, pos: Int) {
                        bundle = Bundle()
                        bundle.putString("apAddress", data.airport)
                        bundle.putString("airReserveDate", data.airReserveDate)
                        bundle.putString("airReserveTime", data.airReserveTime)
                        bundle.putString("airReturnDate", data.airReturnDate)
                        bundle.putString("airReturnTime", data.airReturnTime)
                        bundle.putString("airModel", data.airCarNumber)
                        bundle.putString("airCarNumber", data.airCarNumber)
                        bundle.putString("airMem", data.airMem)
                        bundle.putInt("airPrice", data.airPrice)
                        val dialog = FragmentAirportDialog()
                        dialog.setArguments(bundle)
                        dialog.show(childFragmentManager, "FragmentAirportDialog")
                    }

                })
            }

        }



        return binding.root
    }

    fun initRecyclerView() {
        val layoutManager = LinearLayoutManager((activity as MainActivity), LinearLayoutManager.VERTICAL, false)
        binding.airportCarList.layoutManager = layoutManager

        airportCarAdapter = AirportCarAdapter()

        _binding?.airportCarList?.adapter = airportCarAdapter
    }

}