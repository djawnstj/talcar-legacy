package com.lx.talcar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lx.talcar.databinding.ViewholderSearchResultItemBinding
import com.lx.talcar.response.TmapAddressInfoResponse

class TmapAddressInfoRecyclerAdapter : RecyclerView.Adapter<TmapAddressInfoRecyclerAdapter.ViewHolder>() {

    var items = ArrayList<TmapAddressInfoResponse.AddressInfo>()

    interface OnItemClickListener{
        fun onItemClick(v: View, data: TmapAddressInfoResponse.AddressInfo, pos : Int)
    }
    private var listener : TmapAddressInfoRecyclerAdapter.OnItemClickListener? = null
    fun setOnItemClickListener(listener : TmapAddressInfoRecyclerAdapter.OnItemClickListener) {
        this.listener = listener
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TmapAddressInfoRecyclerAdapter.ViewHolder {
        val binding = ViewholderSearchResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TmapAddressInfoRecyclerAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }

    inner class ViewHolder(val binding:ViewholderSearchResultItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun setItem(item:TmapAddressInfoResponse.AddressInfo) {


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