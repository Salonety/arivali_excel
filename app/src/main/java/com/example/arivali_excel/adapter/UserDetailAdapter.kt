package com.example.arivali_excel.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arivali_excel.R
import com.example.arivali_excel.database.Student

import com.example.arivali_excel.databinding.UserDetailItemBinding

class UserDetailAdapter : RecyclerView.Adapter<UserDetailAdapter.MyViewHolder>() {

    private var listener: ((Student) -> Unit)? = null
    var list = mutableListOf<Student>()
    private var userType: String? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setContentList(list: MutableList<Student>, userType: String) {
        this.list = list
        this.userType = userType
        notifyDataSetChanged()
    }

    class MyViewHolder(val binding: UserDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding =
            UserDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    fun itemClickListener(l: (Student) -> Unit) {
        listener = l
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.binding.student= this.list[position]

        holder.binding.tvStatus.isEnabled = false
        if (userType.equals("user")) {
            holder.binding.tvStatus.isEnabled = true
            with(holder) {
                binding.tvStatus.setBackgroundColor(
                        binding.tvStatus.resources.getColor(R.color.bg_color)
                    )
            }
        }

        holder.binding.tvStatus.setOnClickListener {
            listener?.let {
                it(this.list[position])
            }
        }

    }

    override fun getItemCount(): Int {
        return this.list.size

    }

}