package com.lx.talcar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lx.talcar.databinding.ReserveItemBinding
import com.lx.talcar.response.TmapSearchResponse
import com.lx.talcar.databinding.ViewholderSearchResultItemBinding
import com.lx.talcar.response.SubscriptionsResponse
import com.lx.talcar.response.TalcarListResponse

class RequestSubRecyclerAdapter : RecyclerView.Adapter<RequestSubRecyclerAdapter.ViewHolder>() {

    var items = ArrayList<SubscriptionsResponse.Data>()

    interface OnItemClickListener{
        fun onItemClick(v: View, data: SubscriptionsResponse.Data, pos : Int)
    }
    private var listener : RequestSubRecyclerAdapter.OnItemClickListener? = null
    fun setOnItemClickListener(listener : RequestSubRecyclerAdapter.OnItemClickListener) {
        this.listener = listener
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestSubRecyclerAdapter.ViewHolder {
        val binding = ReserveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestSubRecyclerAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }

    inner class ViewHolder(val binding: ReserveItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun setItem(item:SubscriptionsResponse.Data) {
            binding.reserveItemDate.text = "기간 : " + item.subReserveDate + " ~ " + item.subReturnDate
            binding.reserveItemCar.text = "시간 : " + item.subReserveTime + " ~ " + item.subReturnTime

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
    }
}
