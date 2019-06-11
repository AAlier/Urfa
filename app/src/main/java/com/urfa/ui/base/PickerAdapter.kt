package com.urfa.ui.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.urfa.R
import com.urfa.model.SelectionEntity

class PickerAdapter(context: Context, list: ArrayList<SelectionEntity>) : ArrayAdapter<SelectionEntity>(context, 0, list) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_selection, parent, false)
        }
        val item = getItem(position)
        val textView = view?.findViewById<TextView>(R.id.textView)
        textView?.text = item.name
        textView?.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, item.image), null, null, null)
        return view!!
    }
}