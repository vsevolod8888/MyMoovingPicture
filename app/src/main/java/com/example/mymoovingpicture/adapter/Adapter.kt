package com.example.mymoovingpicture.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymoovingpicture.databinding.CoordHolderBinding
import com.example.mymoovingpicture.databinding.RouteHolderNewBinding
import com.example.mymoovingpicture.domain.CoordinatesDomain
import com.example.mymoovingpicture.domain.RouteDomain

class Adapter(val clickListener: RouteListener) :
    ListAdapter<RouteDomain, Adapter.RouteHolder>(SleepNightDiffCallback()) {
    var selectedItemPosition:Int = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteHolder {
        return RouteHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RouteHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {

            clickListener.onClick(item )
            selectItemPosition(position)
        }
        holder.bind(
            item
        )
    }
    fun selectItemPosition(itemPos:Int){
        selectedItemPosition = itemPos
        notifyDataSetChanged()
    }


    class RouteHolder(val binding: RouteHolderNewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //var fff:ConstraintLayout

        // @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun bind(
            item: RouteDomain

        ) {
            binding.route = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RouteHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RouteHolderNewBinding.inflate(layoutInflater, parent, false)
                return RouteHolder(binding)
            }
        }

    }
}


class SleepNightDiffCallback : DiffUtil.ItemCallback<RouteDomain>() {
    override fun areItemsTheSame(
        oldItem: RouteDomain,
        newItem: RouteDomain
    ): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: RouteDomain,
        newItem: RouteDomain
    ): Boolean {
        return oldItem == newItem
    }
}
class RouteListener(val clickListener: (itemDetail: RouteDomain) -> Unit) {        // ???????????
    fun onClick(itemDetail: RouteDomain) = clickListener(itemDetail)
}