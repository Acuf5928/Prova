package com.acuf5928.marvelcharacters.ui.recycler

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.acuf5928.marvelcharacters.databinding.ItemHomeElementBinding
import com.acuf5928.marvelcharacters.model.local.MainElementModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop

class ElementHomeAdapter(
    private var list: List<MainElementModel>,
    private val onClick: ((MainElementModel) -> Unit)? = null
) : RecyclerView.Adapter<ElementHomeAdapter.VH>() {
    private var key = ""

    private var filteredList = list.filter { key.isBlank() || it.title.contains(key, ignoreCase = true) || it.description.contains(key, ignoreCase = true)}

    class VH(val binding: ItemHomeElementBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemHomeElementBinding.inflate((parent.context as FragmentActivity).layoutInflater, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        with(holder) {
            val context = binding.root.context
            val item = filteredList[position]

            Glide
                .with(context)
                .load(item.imgLink)
                .transform(CircleCrop())
                .into(binding.icon)

            binding.title.text = item.title
            binding.content.text = item.description

            onClick?.let { click ->
                binding.root.setOnClickListener {
                    click.invoke(item)
                }
            }
        }
    }

    override fun getItemCount() = filteredList.size

    fun updateList(list: List<MainElementModel>) {
        this.list = list
        filteredList = list.filter { key.isBlank() || it.title.contains(key, ignoreCase = true) || it.description.contains(key, ignoreCase = true)}
        notifyDataSetChanged()
    }

    fun setFilter(key: String) {
        this.key = key
        filteredList = list.filter { key.isBlank() || it.title.contains(key, ignoreCase = true) || it.description.contains(key, ignoreCase = true)}
        notifyDataSetChanged()
    }
}