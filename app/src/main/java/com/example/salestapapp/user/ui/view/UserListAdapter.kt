package com.example.salestapapp.user.ui.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.salestapapp.R
import com.example.salestapapp.login.data.model.UsersModel

class UserListAdapter(
    private var list: List<UsersModel>,
    val onItemRemove:(UsersModel) -> Unit, val onItemGoEdit: (UsersModel) -> Unit
): RecyclerView.Adapter<UserVH>() {

    private lateinit var context: Context

    fun updateList(newList: List<UsersModel>) {
        val userDiff = UserDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(userDiff)
        list = newList
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH {
        context = parent.context
        return UserVH(LayoutInflater.from(parent.context).inflate(R.layout.user_list_cardview, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        holder.render(list[position], context, onItemRemove, onItemGoEdit)
    }
}

class UserVH(view: View): RecyclerView.ViewHolder(view){

    private val ivImage = view.findViewById<ImageView>(R.id.ivImageUserCardview)
    private val tvName = view.findViewById<TextView>(R.id.tvNameUserCardiView)
    private val tvDated = view.findViewById<TextView>(R.id.tvDatedUserCardiView)
    private val tvEmail = view.findViewById<TextView>(R.id.tvEmailUserCardView)
    private val btnEdit = view.findViewById<Button>(R.id.btnEditUserCardView)
    private val btnDelete = view.findViewById<Button>(R.id.btnDeleteUserCardView)

    fun render (
        userModel: UsersModel, context: Context,
        onItemRemove: (UsersModel) -> Unit,
        onItemGoEdit: (UsersModel) -> Unit
    ){
        /*if (userModel.imageUser.isNotEmpty()) {
            ivImage.setImageBitmap(convertBase64ToBitmap(userModel.imageUser))
        }else{
            ivImage.setImageResource(R.drawable.gallery)
        }*/

        tvName.text = "${userModel.name} ${userModel.lastName}"
        tvDated.text = userModel.createDate
        tvEmail.text = userModel.userName
        btnEdit.setOnClickListener {
            onItemGoEdit(userModel)
        }
        btnDelete.setOnClickListener {
            onItemRemove(userModel)
        }

    }

    private fun convertBase64ToBitmap(base64String: String): Bitmap? {
        val decodedByteArray: ByteArray = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
    }

}