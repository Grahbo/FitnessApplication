package com.bignerdranch.android.FitnessApplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.FitnessApplication.data.WorkOutEntity
import com.bignerdranch.android.FitnessApplication.databinding.ListItemBinding

class WorkOutListAdapter(private val workoutsList: List<WorkOutEntity>,
                         private val listener: ListItemListener
) :

    RecyclerView.Adapter<WorkOutListAdapter.ViewHolder>() {

    val selectedworkouts = arrayListOf<WorkOutEntity>()

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val binding = ListItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = workoutsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workout = workoutsList[position]
        with(holder.binding) {
            workOutText.text = workout.text
            root.setOnClickListener{
                listener.editWorkOut(workout.id)
            }

            fab.setOnClickListener {
                if(selectedworkouts.contains(workout)){
                    selectedworkouts.remove(workout)
                    fab.setImageResource(R.drawable.ic_note)
                }else{
                    selectedworkouts.add(workout)
                    fab.setImageResource(R.drawable.ic_check)
                }
                listener.onItemSelectionChanged()
            }

            fab.setImageResource(
                if (selectedworkouts.contains(workout)){
                    R.drawable.ic_check
                }else{
                    R.drawable.ic_note
                }
            )
        }
    }

    interface ListItemListener{
        fun editWorkOut(workOutId: Int)
        fun onItemSelectionChanged()
    }
}