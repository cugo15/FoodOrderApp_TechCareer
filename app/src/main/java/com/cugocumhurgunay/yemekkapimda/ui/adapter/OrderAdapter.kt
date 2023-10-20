package com.cugocumhurgunay.yemekkapimda.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cugocumhurgunay.yemekkapimda.data.entity.Orders
import com.cugocumhurgunay.yemekkapimda.databinding.OrderRowBinding
import com.cugocumhurgunay.yemekkapimda.ui.viewmodel.main.OrderViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderAdapter(var mContext: Context,
                   var orderList:List<Orders>,
                   var viewModel: OrderViewModel
)
    : RecyclerView.Adapter<OrderAdapter.OrderHolder>(){

    inner class OrderHolder(var binding: OrderRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val binding = OrderRowBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return OrderHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        val order = orderList[position]
        val t = holder.binding
        t.textViewOrderRowAdress.text = "Teslimat Adresi : ${order.order_adress}"
        t.textViewOrderRowDetails.text = order.order_details
        t.textViewOrderRowTime.text = "Sipariş Tarihi : ${timeConvert(order.order_time)}"
        t.textViewOrderRowTotalPrice.text = "Toplam : ${order.order_total} TL"
        t.imageViewDown.setOnClickListener {
            t.imageViewDown.visibility = View.INVISIBLE
            t.imageViewUp.visibility = View.VISIBLE
            t.textViewOrderRowDetails.visibility = View.VISIBLE
            t.textViewOrderRowAdress.visibility = View.VISIBLE
            t.line2.visibility = View.VISIBLE
            t.line1.visibility = View.VISIBLE

        }
        t.imageViewUp.setOnClickListener {
            t.imageViewDown.visibility = View.VISIBLE
            t.imageViewUp.visibility = View.INVISIBLE
            t.textViewOrderRowDetails.visibility = View.GONE
            t.textViewOrderRowAdress.visibility = View.GONE
            t.line2.visibility = View.GONE
            t.line1.visibility = View.GONE
        }

    }

    private fun timeConvert(timestamp: Timestamp): String {
        val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
        val sdf = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault())
        val netDate = Date(milliseconds)
        return sdf.format(netDate).toString()
    }
}