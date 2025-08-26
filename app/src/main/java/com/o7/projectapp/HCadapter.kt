package com.o7.projectapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HCadapter(var list: ArrayList<HCdataclass>,
                var clickInterface:OnClick?,
                private val currentUserType: String?="user"): RecyclerView.Adapter<HCadapter.Viewholder>() {
    class Viewholder(var view: View) : RecyclerView.ViewHolder(view) {
        var Username = view.findViewById<TextView>(R.id.textclass)
        var password = view.findViewById<TextView>(R.id.nm)
        var Email = view.findViewById<TextView>(R.id.desc)
        var update = view.findViewById<Button>(R.id.btnupdate)
        var delete = view.findViewById<Button>(R.id.btndelete)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HCadapter.Viewholder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.hc_itemslist, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: HCadapter.Viewholder, position: Int) {
        val item = list[position]
        holder.Username.text = item.Username
        holder.Email.text = item.Email

        val isUser = currentUserType == "user"
        val hasClick = clickInterface != null

        if (isUser) {
            holder.update.visibility = View.GONE
            holder.delete.visibility = View.GONE
        } else if (hasClick) {
            holder.update.visibility = View.VISIBLE
            holder.delete.visibility = View.VISIBLE


            holder.itemView.setOnClickListener {
                clickInterface?.onItemClick(position)
            }
            holder.update.setOnClickListener {
                clickInterface?.update(position)
            }
            holder.delete.setOnClickListener {
                clickInterface?.delete(position)
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



