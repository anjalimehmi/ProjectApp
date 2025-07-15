package com.o7.projectapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HCadapter(var list: ArrayList<HCdataclass>,var clickInterface:OnClick): RecyclerView.Adapter<HCadapter.Viewholder>() {
    class Viewholder(var view: View) : RecyclerView.ViewHolder(view) {
        var name = view.findViewById<TextView>(R.id.nm)
        var update = view.findViewById<Button>(R.id.btnupdate)
        var delete = view.findViewById<Button>(R.id.btndelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HCadapter.Viewholder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.hc_itemslist, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: HCadapter.Viewholder, position: Int) {
        holder.apply {
            name.setText(list[position].title)
            update.setOnClickListener {
                clickInterface.update(position)
            }
            delete.setOnClickListener {
                clickInterface.delete(position)
            }
            view.setOnClickListener {
                clickInterface.onItemClick(position)
            }
        }
    }
        override fun getItemCount(): Int {
            return list.size
        }

        interface OnClick{
            fun update(position: Int)
            fun delete(position: Int)
            fun onItemClick(position: Int)
        }
    }



