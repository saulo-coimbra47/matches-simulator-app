package me.dio.simulator.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import me.dio.simulator.R
import me.dio.simulator.data.MatchesAPI
import me.dio.simulator.databinding.ActivityMainBinding
import me.dio.simulator.domain.Match
import me.dio.simulator.ui.adapter.MatchesAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var matchesApi: MatchesAPI
    private lateinit var binding: ActivityMainBinding
    private var matchesAdapter: MatchesAdapter = MatchesAdapter(Collections.emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupHttpClient()
        setupMatchesList()
        setupMatchesRefresh()
        setupFloatingActionButton()

    }

    private fun setupHttpClient() {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://digitalinnovationone.github.io/matches-simulator-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        matchesApi = retrofit.create(MatchesAPI::class.java)
    }

    private fun setupMatchesList() {
        binding.rvMatches.setHasFixedSize(true)
        LinearLayoutManager(this).also { binding.rvMatches.layoutManager = it }
        binding.rvMatches.adapter = this.matchesAdapter
        findMatchesFromApi()
    }

    private fun showErrorMessage() {
        Snackbar.make(binding.fabSimulate, R.string.error_api, Snackbar.LENGTH_LONG).show()
    }

    private fun setupMatchesRefresh() {
        binding.srlMatches.setOnRefreshListener { run { this.findMatchesFromApi() } }
    }

    private fun setupFloatingActionButton() {
        binding.fabSimulate.setOnClickListener { view -> run {
            view.animate().rotationBy(360F).setDuration(500).setListener(object: AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    val random = Random
                    for (i in 0 until matchesAdapter.itemCount){
                        val match: Match = matchesAdapter.getMatches()[i]
                        match.homeTeam.score = random.nextInt(match.homeTeam.stars + 1)
                        match.awayTeam.score = random.nextInt(match.awayTeam.stars + 1)
                        matchesAdapter.notifyItemChanged(i)
                    }
                }
            })
        } }
    }

    private fun findMatchesFromApi() {
        binding.srlMatches.isRefreshing = true
        matchesApi.getMatches().enqueue(object : Callback<List<Match>>{
            override fun onResponse(call: Call<List<Match>>, response: Response<List<Match>>) {
                if(response.isSuccessful) {
                    val matches : List<Match>? = response.body()
                    matchesAdapter = matches?.let { MatchesAdapter(it) }!!
                    binding.rvMatches.adapter = matchesAdapter
                }
                else {
                    showErrorMessage()
                }
                binding.srlMatches.isRefreshing = false
            }
            override fun onFailure(call: Call<List<Match>>, t: Throwable) {
                showErrorMessage()
                binding.srlMatches.isRefreshing = false
            }
        })
    }


}