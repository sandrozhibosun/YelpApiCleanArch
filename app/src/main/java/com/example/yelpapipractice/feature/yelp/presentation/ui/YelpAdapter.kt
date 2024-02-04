package com.example.yelpapipractice.feature.yelp.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.yelpapipractice.R
import com.example.yelpapipractice.databinding.ItemBusinessBinding
import com.example.yelpapipractice.feature.yelp.data.model.domain.Business
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class YelpAdapter @Inject constructor(
    @ActivityContext private val context: Context
) : RecyclerView.Adapter<YelpAdapter.BusinessItemViewHolder>() {

    private var mBusinessList = emptyList<Business>()
    private lateinit var itemBusinessBinding: ItemBusinessBinding
    private var onItemClick: ((Business) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setBusinessList(
        businessList: List<Business>,
    ) {
        this.mBusinessList = businessList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setOnItemClick(onItemClick: ((Business) -> Unit)? = null) {
        this.onItemClick = onItemClick
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessItemViewHolder {
        itemBusinessBinding =
            ItemBusinessBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BusinessItemViewHolder(itemBusinessBinding, context)
    }

    override fun onBindViewHolder(holder: BusinessItemViewHolder, position: Int) {
        holder.bind(mBusinessList[position], onItemClick)
    }

    override fun getItemCount(): Int {
        return mBusinessList.size
    }

    inner class BusinessItemViewHolder(
        private val binding: ItemBusinessBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(business: Business, onItemClick: ((Business) -> Unit)? = null) {
            with(binding) {
                name.text = business.name
                description.text = business.ratings.toString()
                Glide.with(context).load(business.imageUrl).apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                ).into(image)
                binding.root.setOnClickListener { onItemClick?.invoke(business) }
            }
        }
    }
}