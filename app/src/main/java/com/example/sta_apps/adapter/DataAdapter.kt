package com.example.sta_apps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sta_apps.R
import com.example.sta_apps.data.DataEntity
import com.example.sta_apps.databinding.UserItemBinding

class DataAdapter(private var listUser:List<DataEntity>) : RecyclerView.Adapter<DataAdapter.AdapterViewHolder>() {
    private lateinit var onItemClickCallback: DataAdapter.OnItemClickCallback

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item,parent,false)
        return AdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataAdapter.AdapterViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = this.listUser.size

    interface OnItemClickCallback{
        fun onItemClicked(selectedUser: DataEntity?,position: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
    fun updateData(newList: List<DataEntity>) {
        listUser= newList
        notifyDataSetChanged()  // or use DiffUtil for better performance
    }

    inner class AdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = UserItemBinding.bind(itemView)
        fun bind(data: DataEntity){
            binding.tvIdShow.text=data.idKaryawan
            binding.tvNameShow.text=data.nmKaryawan
            binding.tvDateShow.text=data.tglMasukKerja.toString()
            binding.tvUsiaShow.text=data.usia.toString()

            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(data,adapterPosition)
            }
        }
    }

}