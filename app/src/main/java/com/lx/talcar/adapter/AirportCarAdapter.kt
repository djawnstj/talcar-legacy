package com.lx.talcar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lx.talcar.databinding.AirportCarItemBinding
import com.lx.talcar.response.TmapSearchResponse
import com.lx.talcar.databinding.ViewholderSearchResultItemBinding
import com.lx.talcar.response.AirportListResponse
import com.lx.talcar.response.TalcarListResponse
import java.security.AccessController.getContext

class AirportCarAdapter : RecyclerView.Adapter<AirportCarAdapter.ViewHolder>() {

    var items = ArrayList<AirportListResponse.Data>()

    interface OnItemClickListener{
        fun onItemClick(v: View, data: AirportListResponse.Data, pos : Int)
    }
    private var listener : AirportCarAdapter.OnItemClickListener? = null
    fun setOnItemClickListener(listener : AirportCarAdapter.OnItemClickListener) {
        this.listener = listener
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirportCarAdapter.ViewHolder {
        val binding = AirportCarItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AirportCarAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }

    inner class ViewHolder(val binding:AirportCarItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun setItem(item:AirportListResponse.Data) {
            binding.apItemCar.text = "${item.airModel}(${item.airCarNumber})"
            binding.apItemDate.text = "${item.airReserveDate} ${item.airReserveTime} ~ ${item.airReturnDate} ${item.airReturnTime}"
            binding.apItemLocation.text = item.airport
            var url = "http://14.55.65.168/image.do?image=/images/benz.jpeg"
            Glide.with(itemView)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .dontAnimate()
                .into(binding?.imageView8!!)

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

//
//    private var searchResultList: List<SearchResultEntity> = listOf()
//    var currentPage = 1
//    var currentSearchString = ""
//
//    private lateinit var searchResultClickListener: (SearchResultEntity) -> Unit
//
//    inner class SearchResultViewHolder(
//        private val binding: ViewholderSearchResultItemBinding,
//        private val searchResultClickListener: (SearchResultEntity) -> Unit
//    ) : RecyclerView.ViewHolder(binding.root) {
//        fun bindData(data: SearchResultEntity) = with(binding) {
//            titleTextView.text = data.name
//            subtitleTextView.text = data.fullAddress
//        }
//
//        fun bindViews(data: SearchResultEntity) {
//            binding.root.setOnClickListener {
//                searchResultClickListener(data)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
//        val binding = ViewholderSearchResultItemBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent,
//            false
//        )
//        return SearchResultViewHolder(binding, searchResultClickListener)
//    }
//
//    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
//        holder.bindData(searchResultList[position])
//        holder.bindViews(searchResultList[position])
//    }
//
//    override fun getItemCount(): Int {
//        return searchResultList.size
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun setSearchResultList(
//        searchResultList: List<SearchResultEntity>,
//        searchResultClickListener: (SearchResultEntity) -> Unit
//    ) {
//        this.searchResultList = this.searchResultList + searchResultList
//        this.searchResultClickListener = searchResultClickListener
//        notifyDataSetChanged()
//    }
//
//    fun clearList() {
//        searchResultList = listOf()
//    }
//}