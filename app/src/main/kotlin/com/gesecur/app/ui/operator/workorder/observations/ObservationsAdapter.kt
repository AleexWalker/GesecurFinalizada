package com.gesecur.app.ui.operator.workorder.observations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gesecur.app.R

/**class ObservationsAdapter: RecyclerView.Adapter<ObservationsAdapter.MyViewHolder>() {
    class MyViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        var observationId = String()
        val description: TextView = itemView.findViewById(R.id.tv_text)
        var planificationId = String()
        var observationTypeId = String()
        var observationCodDescription = String()
        var observationCodCode = String()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.observation_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = observationList[position]
        holder.description.text = currentItem.description
        holder.observationId = currentItem.observationId.toString()
        holder.planificationId = currentItem.planificationId.toString()
        holder.observationTypeId = currentItem.observationTypeId.toString()
        holder.observationCodDescription = currentItem.observationCodDescription
        holder.observationCodCode = currentItem.observationCodCode
    }

    override fun getItemCount(): Int {
        return observationList.size
    }
}*/