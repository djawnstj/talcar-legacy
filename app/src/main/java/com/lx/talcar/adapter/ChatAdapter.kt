package com.lx.talcar.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lx.talcar.R
import com.lx.talcar.databinding.ChatItemBinding
import com.lx.talcar.databinding.ItemMyChatBinding
import com.lx.talcar.databinding.ViewholderSearchResultItemBinding
import com.lx.talcar.response.ChatResponse
import com.lx.talcar.response.TalcarListResponse
import com.lx.talcar.util.AppData

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = ArrayList<ChatResponse.Data>()

    interface OnItemClickListener{
        fun onItemClick(v: View, data: ChatResponse.Data, pos: Int)
    }
    private var listener : ChatAdapter.OnItemClickListener? = null
    fun setOnItemClickListener(listener : ChatAdapter.OnItemClickListener) {
        this.listener = listener
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 1) {
            val binding = ItemMyChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return Holder(binding)
        }
        //getItemViewType 에서 뷰타입 2을 리턴받았다면 상대채팅레이아웃을 받은 Holder2를 리턴
        else{
            val binding = ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return Holder2(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //onCreateViewHolder에서 리턴받은 뷰홀더가 Holder라면 내채팅, item_my_chat의 뷰들을 초기화 해줌
        if (holder is com.lx.talcar.adapter.ChatAdapter.Holder) {
            val item = items[position]
            holder.setItem(item)
        }
        //onCreateViewHolder에서 리턴받은 뷰홀더가 Holder2라면 상대의 채팅, item_your_chat의 뷰들을 초기화 해줌
        else if(holder is com.lx.talcar.adapter.ChatAdapter.Holder2) {
            val item = items[position]
            holder.setItem(item)
        }
    }

    //내가친 채팅 뷰홀더
    inner class Holder(val binding:ItemMyChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(item: ChatResponse.Data) {
            var year = item.chTime.substring(0, 4)
            var month = item.chTime.substring(5, 7)
            var day = item.chTime.substring(8, 10)
            var hour = item.chTime.substring(11, 13)
            var minute = item.chTime.substring(14, 16)
            binding.chatText.text = item.chat
            binding.chatTime.text = year + "년 " + month + "월 " + day + "일 " + hour + ":" + minute
            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION)
            {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView,item,pos)
                }
            }
        }
//        //친구목록 모델의 변수들 정의하는부분
//        val chat_Text = binding.chatText
//        val chat_Time = binding.chatTime
    }

    //상대가친 채팅 뷰홀더
    inner class Holder2(val binding:ChatItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(item: ChatResponse.Data) {
            var year = item.chTime.substring(0, 4)
            var month = item.chTime.substring(5, 7)
            var day = item.chTime.substring(8, 10)
            var hour = item.chTime.substring(11, 13)
            var minute = item.chTime.substring(14, 16)
            binding.chatYouName.text = item.chMem
            binding.chatText.text = item.chat
            binding.chatTime.text = year + "년 " + month + "월 " + day + "일 " + hour + ":" + minute
            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION)
            {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView,item,pos)
                }
            }
        }

        //친구목록 모델의 변수들 정의하는부분
//        val chat_You_Image = binding.chatYouImage
//        val chat_You_Name = binding.chatYouName
//        val chat_Text = binding.chatText
//        val chat_Time = binding.chatTime


    }

    override fun getItemViewType(position: Int): Int {//여기서 뷰타입을 1, 2로 바꿔서 지정해줘야 내채팅 너채팅을 바꾸면서 쌓을 수 있음
        var memId = AppData.memId

        //내 아이디와 arraylist의 name이 같다면 내꺼 아니면 상대꺼
        return if (items.get(position).chMem == memId) {
            1
        } else {
            2
        }
    }
}