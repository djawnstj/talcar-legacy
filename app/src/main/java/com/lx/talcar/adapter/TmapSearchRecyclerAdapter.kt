package com.lx.exproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lx.talcar.response.TmapSearchResponse
import com.lx.talcar.databinding.ViewholderSearchResultItemBinding

class TmapSearchRecyclerAdapter : RecyclerView.Adapter<TmapSearchRecyclerAdapter.ViewHolder>() {

    var items = ArrayList<TmapSearchResponse.SearchPoiInfo.Pois.Poi>()

    interface OnItemClickListener{
        fun onItemClick(v: View, data: TmapSearchResponse.SearchPoiInfo.Pois.Poi, pos : Int)
    }
    private var listener : TmapSearchRecyclerAdapter.OnItemClickListener? = null
    fun setOnItemClickListener(listener : TmapSearchRecyclerAdapter.OnItemClickListener) {
        this.listener = listener
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TmapSearchRecyclerAdapter.ViewHolder {
        val binding = ViewholderSearchResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TmapSearchRecyclerAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }

    inner class ViewHolder(val binding:ViewholderSearchResultItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun setItem(item:TmapSearchResponse.SearchPoiInfo.Pois.Poi) {


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