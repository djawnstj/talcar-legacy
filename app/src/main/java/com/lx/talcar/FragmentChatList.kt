package com.lx.talcar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.lx.talcar.adapter.ChatListAdapter
import com.lx.talcar.adapter.RequestShareAdapter
import com.lx.talcar.adapter.TalcarRecyclerAdapter
import com.lx.talcar.api.ChatClient
import com.lx.talcar.api.TalcarClient
import com.lx.talcar.databinding.FragmentChatListBinding
import com.lx.talcar.databinding.FragmentHomeBinding
import com.lx.talcar.response.ChatResponse
import com.lx.talcar.response.ReservationResponse
import com.lx.talcar.util.AppData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class FragmentChatList : Fragment() {

    private  var _binding: FragmentChatListBinding? =null
    val binding get() = _binding!!
    lateinit var mainActivity:MainActivity
    lateinit var bundle: Bundle

    var chatListAdapter: ChatListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChatListBinding.inflate(inflater, container, false)

        mainActivity = context as MainActivity
        initRecyclerView()
        getChatList()

        chatListAdapter?.setOnItemClickListener(object: ChatListAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: ChatResponse.Data, pos: Int) {
                bundle = Bundle()
                var fragmentChat=FragmentChat()
                var shMem:String = data.chCounterpart
                bundle.putString("shMem", shMem)
                fragmentChat.arguments = bundle
                mainActivity.supportFragmentManager.beginTransaction().add(R.id.fragmentView, fragmentChat).addToBackStack(null).commit()
            }

        })

        return binding.root
    }

    fun initRecyclerView() {
        val layoutManager = LinearLayoutManager((activity as MainActivity), LinearLayoutManager.VERTICAL, false)
        binding.chatList.layoutManager = layoutManager

        chatListAdapter = ChatListAdapter()

        _binding?.chatList?.adapter = chatListAdapter
    }

    fun getChatList() {
        ChatClient.api.getChatList(
            memId = AppData.memId
        ).enqueue(object: Callback<ChatResponse> {
            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                var items = response.body()?.data
                items?.apply {
                    chatListAdapter?.items = this as ArrayList<ChatResponse.Data>
                    chatListAdapter?.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                Log.d("오류", "$t")
            }
        })
    }
}