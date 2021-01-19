package net.memish.durakcepte.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import net.memish.durakcepte.R
import net.memish.durakcepte.databinding.LayoutSavedMarkLocationBinding
import net.memish.durakcepte.model.MarkLocation
import java.io.File

class SavedMarkLocationsAdapter(
    private var savedMarkLocationsClickListener: SavedMarkLocationsClickListener
): RecyclerView.Adapter<SavedMarkLocationsAdapter.MarkedLocationsHolder>() {

    private val items = arrayListOf<MarkLocation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkedLocationsHolder = MarkedLocationsHolder(
        LayoutSavedMarkLocationBinding.bind(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_saved_mark_location, parent, false)
        )
    )

    override fun onBindViewHolder(holder: MarkedLocationsHolder, position: Int) {
        val item = items[holder.adapterPosition]
        val title = item.title
        val description = item.description
        val path = item.path
        val address = item.address

        holder.bind(title, description, path, address)
        holder.itemView.setOnClickListener { savedMarkLocationsClickListener.onClick(item) }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun update(items: List<MarkLocation>) {
        val diffCallback = SavedMarkLocationsDiffCallback(this.items, items)
        val result = DiffUtil.calculateDiff(diffCallback)
        this.items.clear()
        this.items.addAll(items)
        result.dispatchUpdatesTo(this)
    }

    class MarkedLocationsHolder(var binding: LayoutSavedMarkLocationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String?, description: String?, path: String?, address: String?) {
            title?.let {
                binding.savedMarkLocationTitle.text = it
            }

            description?.let {
                binding.savedMarkLocationDescription.text = it
            }

            address?.let {
                binding.savedMarkLocationAddress.text = it
            }

            if (path != null) {
                binding.savedMarkLocationImage.load(File(path))
            } else {
                binding.savedMarkLocationImage.load(R.drawable.image)
            }
        }
    }

    interface SavedMarkLocationsClickListener {
        fun onClick(markLocation: MarkLocation)
    }
}