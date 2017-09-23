package cat.xojan.random1.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cat.xojan.random1.R
import cat.xojan.random1.domain.entities.Program
import cat.xojan.random1.domain.interactor.ProgramDataInteractor
import cat.xojan.random1.ui.activity.BrowseActivity
import com.squareup.picasso.Picasso


class ProgramListAdapter(private val interactor: ProgramDataInteractor)
    : RecyclerView.Adapter<ProgramListAdapter.ViewHolder>() {

    var programs: List<Program> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return programs.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(programs[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.program_item, parent, false)
        return ViewHolder(itemView, interactor)
    }

    class ViewHolder(itemView: View,
                     val interactor: ProgramDataInteractor) : RecyclerView
    .ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.programTitle)
        private var image: ImageView = itemView.findViewById(R.id.programImage)

        fun bind(item: Program) {
            itemView?.setOnClickListener {
                val isSection = interactor.isSectionSelected && item.sections.size > 1
                val intent = BrowseActivity.newIntent(itemView.context, item, isSection)
                itemView.context.startActivity(intent)
            }
            title.text = item.title
            Picasso.with(itemView.context)
                    .load(item.imageUrl())
                    .placeholder(R.drawable.default_rac1)
                    .into(image)
        }
    }


}