package com.example.salestapapp.menu.data.model.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.salestapapp.R
import com.example.salestapapp.menu.data.model.MenuItemsModel

class MenuItemsAdapter(private var menuList: List<MenuItemsModel>):RecyclerView.Adapter<MenuItemsVH>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemsVH {
        context = parent.context
        return MenuItemsVH(LayoutInflater.from(context).inflate(R.layout.menu_list_cardview,
            parent, false))
    }

    override fun onBindViewHolder(holder: MenuItemsVH, position: Int){
        holder.render(menuList[position], context)
    }

    override fun getItemCount() = menuList.size

}

class  MenuItemsVH(view: View): RecyclerView.ViewHolder(view){

    private val ivImage = view.findViewById<ImageView>(R.id.ivImageMenu)
    private val nameModule = view.findViewById<TextView>(R.id.tvMenuTitle)
    private val btnMenu = view.findViewById<CardView>(R.id.btnMenuOpcion)

    fun render(menuItemModel: MenuItemsModel, context: Context) {

        if (menuItemModel.icon > 0) {
            val icon: Int = menuItemModel.icon
            ivImage.setImageResource(icon)
        }else{
            ivImage.setImageResource(R.drawable.ic_launcher_background)
        }
        nameModule.text = menuItemModel.name.toString()

        btnMenu.setOnClickListener {
            val intent = Intent(context, menuItemModel.activity)
            context.startActivity(intent)
        }

    }

}
