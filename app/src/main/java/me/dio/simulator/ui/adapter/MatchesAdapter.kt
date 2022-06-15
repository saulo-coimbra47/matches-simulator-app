package me.dio.simulator.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.dio.simulator.databinding.MatchItemBinding
import me.dio.simulator.domain.Match
import me.dio.simulator.ui.DetailActivity

class MatchesAdapter (private val matches: List<Match>) :
    RecyclerView.Adapter<MatchesAdapter.ViewHolder>(){

    fun getMatches(): List<Match> {
        return matches
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: MatchItemBinding = MatchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        val match: Match = matches[position]

        //Adapta os dados da partida (recuperada da API) para o nosso layout.
        Glide.with(context).load(match.homeTeam.image).circleCrop().into(holder.binding.ivHomeTeam)
        holder.binding.tvHomeTeamName.text = match.homeTeam.name
        if (match.homeTeam.score != null) holder.binding.tvHomeTeamScore.text = match.homeTeam.score.toString()
        Glide.with(context).load(match.awayTeam.image).circleCrop().into(holder.binding.ivAwayTeam)
        holder.binding.tvAwayTeamName.text = match.awayTeam.name
        if (match.awayTeam.score != null) holder.binding.tvAwayTeamScore.text = match.awayTeam.score.toString()

        holder.itemView.setOnClickListener {
            var intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.Extras.MATCH, match)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return matches.size
    }

    class ViewHolder(val binding: MatchItemBinding) : RecyclerView.ViewHolder(binding.root)

}