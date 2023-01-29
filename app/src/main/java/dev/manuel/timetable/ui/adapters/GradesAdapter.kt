package dev.manuel.timetable.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import dev.manuel.timetable.R
import dev.manuel.timetable.room.entities.Grade

class GradeAdapter(private val grades: List<Grade>) :
    ListAdapter<Grade, GradeAdapter.ViewHolder>(GradeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.grade_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskTextView: TextView = itemView.findViewById(R.id.task_grade_text)
        private val gradeTextView: TextView = itemView.findViewById(R.id.grade_text)
        private val gradeBar = itemView.findViewById<LinearProgressIndicator>(R.id.grade_bar)

        fun bind(grade: Grade) {
            taskTextView.text = grade.task
            gradeTextView.text = grade.grade.toString()
            gradeBar.progress = grade.grade
        }
    }

    class GradeDiffCallback : DiffUtil.ItemCallback<Grade>() {
        override fun areItemsTheSame(oldItem: Grade, newItem: Grade): Boolean {
            return oldItem.task == newItem.task
        }

        override fun areContentsTheSame(oldItem: Grade, newItem: Grade): Boolean {
            return oldItem == newItem
        }
    }
}
