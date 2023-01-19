package dev.manuel.timetable.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.manuel.timetable.R
import dev.manuel.timetable.databinding.ClassLayoutBinding
import dev.manuel.timetable.format
import dev.manuel.timetable.room.entities.OccurrenceWithClass

class ClassAdapter(
    val onClassClick: (OccurrenceWithClass) -> Unit = {}
) :
    ListAdapter<OccurrenceWithClass, ClassAdapter.ClassViewHolder>(UserDiffCallBack()) {

    class ClassViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val binding = ClassLayoutBinding.bind(itemView)

        fun onBind(owClass: OccurrenceWithClass) {
            binding.starts.text = owClass.occurrence.starts.format()
            binding.ends.text = owClass.occurrence.ends.format()
            binding.textSubject.text = owClass.mClass.subject
            binding.textClassroom.text = owClass.mClass.classroom
            binding.textTeacher.text = owClass.mClass.teacher
        }
    }

    private class UserDiffCallBack : DiffUtil.ItemCallback<OccurrenceWithClass>() {
        override fun areItemsTheSame(oldItem: OccurrenceWithClass, newItem: OccurrenceWithClass): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: OccurrenceWithClass, newItem: OccurrenceWithClass): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.class_layout, parent, false)

        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        holder.itemView.setOnClickListener { onClassClick(getItem(position)) }
        holder.onBind(getItem(position))
    }
}