package com.lx.talcar.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lx.talcar.R
import com.lx.talcar.databinding.ReserveItemBinding
import com.lx.talcar.response.ReservationResponse
import com.lx.talcar.util.AppData

class UsingShareAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = ArrayList<ReservationResponse.Data>()

    interface OnItemClickListener{
        fun onItemClick(v: View, data: ReservationResponse.Data, pos: Int)
    }
    private var listener : UsingShareAdapter.OnItemClickListener? = null
    fun setOnItemClickListener(listener : UsingShareAdapter.OnItemClickListener) {
        this.listener = listener
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 1) {
            val binding = ReserveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return Holder(binding)
        }
        //getItemViewType 에서 뷰타입 2을 리턴받았다면 상대채팅레이아웃을 받은 Holder2를 리턴
        else if(viewType == 2){
            val binding = ReserveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return Holder2(binding)
        }
        else if(viewType==3){
            val binding = ReserveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return Holder3(binding)
        }
        else if(viewType==4){
            val binding = ReserveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return Holder4(binding)
        }
        else{
            val binding = ReserveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return Holder5(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //onCreateViewHolder에서 리턴받은 뷰홀더가 Holder라면 내채팅, item_my_chat의 뷰들을 초기화 해줌
        if (holder is com.lx.talcar.adapter.UsingShareAdapter.Holder) {
            val item = items[position]
            holder.setItem(item)
        }
        //onCreateViewHolder에서 리턴받은 뷰홀더가 Holder2라면 상대의 채팅, item_your_chat의 뷰들을 초기화 해줌
        else if(holder is com.lx.talcar.adapter.UsingShareAdapter.Holder2) {
            val item = items[position]
            holder.setItem(item)
        }
        else if(holder is com.lx.talcar.adapter.UsingShareAdapter.Holder3) {
            val item = items[position]
            holder.setItem(item)
        }
        else if(holder is com.lx.talcar.adapter.UsingShareAdapter.Holder4) {
            val item = items[position]
            holder.setItem(item)
        }
        else if(holder is com.lx.talcar.adapter.UsingShareAdapter.Holder5) {
            val item = items[position]
            holder.setItem(item)
        }
    }


    inner class Holder(val binding:ReserveItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(item: ReservationResponse.Data) {
            //반납 완료
            binding.reserveItemClassification.text = "반납 완료"
            binding.reserveItemDate.text = item.reReserveDate + " " + item.reReserveTime + " ~ " + item.reReturnDate + " " + item.reReturnTime
            binding.reserveItemCar.text = item.reModel + "(" + item.reCarNumber + ")"

            var url = "http://14.55.65.168/image.do?image=/images/benz.jpeg"
            Glide.with(itemView)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .dontAnimate()
                .into(binding?.imageView7!!)

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
    inner class Holder2(val binding:ReserveItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(item: ReservationResponse.Data) {
            // 이용 중
            binding.reserveItemLayout.setBackgroundResource(R.drawable.ic_item_using)
            binding.reserveItemClassification.apply {
                text = "이용 중"
                setTextColor(Color.parseColor("#6FEC71"))
            }
            binding.reserveItemDate.text = item.reReserveDate + " " + item.reReserveTime + " ~ " + item.reReturnDate + " " + item.reReturnTime
            binding.reserveItemCar.text = item.reModel + "(" + item.reCarNumber + ")"

            var url = "http://14.55.65.168/image.do?image=/images/benz.jpeg"
            Glide.with(itemView)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .dontAnimate()
                .into(binding?.imageView7!!)

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

    //상대가친 채팅 뷰홀더
    inner class Holder3(val binding:ReserveItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(item: ReservationResponse.Data) {
            // 요청 확인 됨
            binding.reserveItemClassification.text = "요청 확인"
            binding.reserveItemDate.text =
                item.reReserveTime + " " + item.reReserveDate + " ~ " + item.reReturnDate + " " + item.reReturnTime
            binding.reserveItemCar.text = item.reModel + "(" + item.reCarNumber + ")"

            var url = "http://14.55.65.168/image.do?image=/images/benz.jpeg"
            Glide.with(itemView)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .dontAnimate()
                .into(binding?.imageView7!!)

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, item, pos)
                }
            }
        }
    }

    inner class Holder4(val binding:ReserveItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(item: ReservationResponse.Data) {
            binding.reserveItemLayout.setBackgroundResource(R.drawable.ic_item_return)
            binding.reserveItemClassification.apply {
                text = "요청 거절됨"
                setTextColor(Color.parseColor("#F64F4F"))
            }
            binding.reserveItemDate.text =
                item.reReserveTime + " " + item.reReserveDate + " ~ " + item.reReturnDate + " " + item.reReturnTime
            binding.reserveItemCar.text = item.reModel + "(" + item.reCarNumber + ")"

            var url = "http://14.55.65.168/image.do?image=/images/benz.jpeg"
            Glide.with(itemView)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .dontAnimate()
                .into(binding?.imageView7!!)

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, item, pos)
                }
            }
        }
    }

    inner class Holder5(val binding:ReserveItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(item: ReservationResponse.Data) {
            binding.reserveItemClassification.text = "요청 중"
            binding.reserveItemDate.text =
                item.reReserveTime + " " + item.reReserveDate + " ~ " + item.reReturnDate + " " + item.reReturnTime
            binding.reserveItemCar.text = item.reModel + "(" + item.reCarNumber + ")"

            var url = "http://14.55.65.168/image.do?image=/images/benz.jpeg"
            Glide.with(itemView)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .dontAnimate()
                .into(binding?.imageView7!!)

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, item, pos)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {//여기서 뷰타입을 1, 2로 바꿔서 지정해줘야 내채팅 너채팅을 바꾸면서 쌓을 수 있음
        //내 아이디와 arraylist의 name이 같다면 내꺼 아니면 상대꺼
        return if (items.get(position).reReturn.equals("o")) {
            // 반납 완료
            1
        } else if(items.get(position).reCheck.equals("o")) {
            // 이용 중
            2
        } else if(items.get(position).reConfirm.equals("o")){
            // 요청 확인
            3
        } else if(items.get(position).reConfirm.equals("x")) {
            // 요청 거절
            4
        } else {
            // 요청 중
            5
        }
    }
}