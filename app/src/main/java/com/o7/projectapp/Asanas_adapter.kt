package com.o7.projectapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Asanas_adapter(var list: ArrayList<Asanas_dataclass>,
                     var clickInterface:OnClick,
                     private var currentUserType: String): RecyclerView.Adapter<Asanas_adapter.Viewholder>(){
                           class Viewholder (var view: View) : RecyclerView.ViewHolder(view){
        var name = view.findViewById<TextView>(R.id.typename)
                               val descText: TextView = itemView.findViewById(R.id.typename2)
        var edit = view.findViewById<Button>(R.id.editbtn)
        var delete = view.findViewById<Button>(R.id.delbtn)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.asanas_listitem, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: Asanas_adapter.Viewholder, position: Int) {
        val item=list[position]
        holder.name.text=item.name
        holder.descText.text = item.description

        if (currentUserType == "user") {
            holder.edit.visibility = View.GONE
            holder.delete.visibility = View.GONE
        } else {
            holder.edit.visibility = View.VISIBLE
            holder.delete.visibility = View.VISIBLE
        }


        holder.edit.setOnClickListener {
            clickInterface.Edit(position)
        }
        holder.delete.setOnClickListener {
            clickInterface.Delete(position)
        }
        holder.itemView.setOnClickListener {
            clickInterface.onItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface OnClick{
        fun Edit(position: Int)
        fun Delete(position: Int)
        fun onItemClick(position: Int)

    }



}