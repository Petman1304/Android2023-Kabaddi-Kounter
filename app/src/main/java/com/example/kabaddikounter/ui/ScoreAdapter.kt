package com.example.kabaddikounter.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kabaddikounter.data.entities.Score
import com.example.kabaddikounter.databinding.ScoreLayoutBinding

class ScoreAdapter(var context: Context) : ListAdapter<Score, ScoreAdapter.ViewHolder>(ScoreDiffCallback()){

    inner class ViewHolder(var binding: ScoreLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ScoreLayoutBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(binding)
    }

//    override fun getItemCount(): Int {
//        return list.size
//    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val score = getItem(position)
        val view = holder.binding

        view.layoutScoreA.text = score.teamAScore.toString()
        view.layoutScoreB.text = score.teamBScore.toString()
        view.layoutTeamA.text = score.teamAName.toString()
        view.layoutTeamB.text = score.teamBName.toString()
    }

    class ScoreDiffCallback : DiffUtil.ItemCallback<Score>(){
        override fun areContentsTheSame(oldItem: Score, newItem: Score): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Score, newItem: Score): Boolean {
            return oldItem.teamId == newItem.teamId
        }
    }

}