package com.example.kabaddikounter.ui

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kabaddikounter.data.entities.Score
import com.example.kabaddikounter.databinding.LiveMatchRvLayoutBinding
import com.example.kabaddikounter.databinding.ScoreLayoutBinding
import com.example.kabaddikounter.ui.ScoreAdapter.ScoreDiffCallback

class LiveMatchAdapter (var context: Context, private val onSubsButtonClick: (Score) -> Unit) : ListAdapter<Score, LiveMatchAdapter.ViewHolder>(ScoreDiffCallback()){
    inner class ViewHolder(var binding: LiveMatchRvLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LiveMatchRvLayoutBinding.inflate(LayoutInflater.from(context), parent, false)

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
        view.status.text = score.status.toString()
        view.status.setTextColor(
            if(view.status.text == "LIVE"){
                Color.GREEN
            }else if(view.status.text == "END"){
                Color.RED
            }else{
                Color.GRAY
            }
        )

        view.subscribeBtn.setOnClickListener {
            onSubsButtonClick(score)
        }
    }


}