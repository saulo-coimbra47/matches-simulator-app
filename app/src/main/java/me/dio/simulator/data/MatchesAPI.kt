package me.dio.simulator.data

import me.dio.simulator.domain.Match
import retrofit2.Call
import retrofit2.http.GET

interface MatchesAPI {

    @GET("matches.json")
    fun getMatches() : Call<List<Match>>

}