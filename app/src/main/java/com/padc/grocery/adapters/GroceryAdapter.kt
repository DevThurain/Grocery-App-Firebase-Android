package com.padc.grocery.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.padc.grocery.R
import com.padc.grocery.data.vos.GroceryVO
import com.padc.grocery.delegates.GroceryViewItemActionDelegate
import com.padc.grocery.viewholders.GroceryViewHolder
const val VIEW_TYPE_LIST = 0
const val VIEW_TYPE_GRID = 1
class GroceryAdapter(private val mDelegate: GroceryViewItemActionDelegate, private val selectedViewType: Int) : BaseRecyclerAdapter<GroceryViewHolder, GroceryVO>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        var view: View? = null

        when(selectedViewType){
            VIEW_TYPE_LIST -> view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_grocery_item,parent,false)
            VIEW_TYPE_GRID -> view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_grocery_item_grid,parent,false)
            else -> view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_grocery_item,parent,false)
        }

        return GroceryViewHolder(view, mDelegate)
    }
}