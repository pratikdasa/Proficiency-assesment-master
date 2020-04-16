package com.proficiencyassesment.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.proficiencyassesment.BR
import com.proficiencyassesment.R
import com.proficiencyassesment.databinding.RecylerviewRowLayoutBinding
import com.proficiencyassesment.model.CountryInformation


class RecyclerViewAdapter (private val listitemrows: List<CountryInformation>?) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding : RecylerviewRowLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recylerview_row_layout, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listitemrows?.size!!

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = this!!.listitemrows?.get(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(private val recylerviewItemLayoutBinding: RecylerviewRowLayoutBinding) : RecyclerView.ViewHolder(recylerviewItemLayoutBinding.root) {
        fun bind(data:Any){
            recylerviewItemLayoutBinding.setVariable(BR.dataitem, data)
            recylerviewItemLayoutBinding.executePendingBindings()
        }
    }

}