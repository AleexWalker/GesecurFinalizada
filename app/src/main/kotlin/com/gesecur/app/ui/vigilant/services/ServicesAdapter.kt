package com.gesecur.app.ui.vigilant.services

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gesecur.app.R

class ServicesAdapter (private val workList: ArrayList<ServicesCard>,
private val clickListener: (ServicesCard) -> Unit): RecyclerView.Adapter<ServicesAdapter.MyViewHolder>(){

    /**
     * Clase dedicada a cargar el adaptador en el RecyclerView desde los datos recibidos desde el GSON.
     * @param contrato: (GSON) -> contrato_servicio_id -> (TextView) tv_identifierx
     * @param servicio: (GSON) -> descripcion_contrato -> (TextView) tv_descripcionx
     * @param localizacion: (GSON) -> hora_ini y hora_fin -> (TextView) tv_hourx
     * @param horas: (GSON) -> descripcion_contrato_servicio -> (TextView) tv_locationx
     * @param fecha: (GSON) -> fecha_ini y fecha_fin -> (TextView) tv_datex
     * @param cuadrante: (GSON) -> cuadrante_id
     * @param vigilante_id: (GSON) -> vigilante_id
     */

    class MyViewHolder (itemView: View, clickAtPosition: (String) -> Unit): RecyclerView.ViewHolder(itemView){
        val contrato: TextView = itemView.findViewById(R.id.tv_identifierx)
        val servicio: TextView = itemView.findViewById(R.id.tv_descripcionx)
        val localizacion: TextView = itemView.findViewById(R.id.tv_locationx)
        val horas: TextView = itemView.findViewById(R.id.tv_hourx)
        val fechas: TextView = itemView.findViewById(R.id.tv_datex)
        var cuadrante = String()
        var vigilante_id = String()

        init {
            itemView.setOnClickListener {
                clickAtPosition(adapterPosition.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.work_order_item_experimental, parent, false)) { clickListener(workList[it.toInt()]) }
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = workList[position]
        holder.contrato.text = currentItem.contrato
        holder.servicio.text = currentItem.servicio
        holder.localizacion.text = currentItem.localizacion
        holder.horas.text = currentItem.horas
        holder.fechas.text = currentItem.fechaAbajo
        holder.cuadrante = currentItem.cuadrante
        holder.vigilante_id = currentItem.vigilante_id

        /**
         * Primer método: Permitir sólo hacer onClick() en el primer item del RecyclerView.
         * Segundo método (comentado): Permitir onClick() en cualquier posición, pero que al hacer onClick() entre al primer item siempre.
         * Tercer método (comentado): Permitido hacer onClick() en cualquier item del RecyclerView
         */

        if (position == 0) {
            holder.itemView.setBackgroundColor(Color.WHITE)
            holder.itemView.isClickable = true
            holder.itemView.isFocusable = true
        }

        holder.itemView.setOnClickListener {
            if (position == 0)
                clickListener(workList[0])
        }

        /**
         * holder.itemView.setOnClickListener {
         *      clickListener(workList[0])
         * }
         *
         * holder.itemView.setOnClickListener {
         *      clickListener(workList[position])
         * }
         */
    }

    override fun getItemCount(): Int {
        return workList.size
    }

}