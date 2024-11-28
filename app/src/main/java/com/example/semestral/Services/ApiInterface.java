package com.example.semestral.Services;

import com.example.semestral.Entidades.Paises;
import com.example.semestral.Entidades.Ranking;
import com.example.semestral.Entidades.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("api/country/quiz-pais")
    Call<List<Paises>> quizPaises();

    @GET("api/country/maps-continents")
    Call<List<Paises>> quizMapas();

    @POST("api/auth/login")
    Call<User> login(@Body User user);

    @POST("api/auth/signup")
    Call<User> signup(@Body User user);

    @GET("api/ranking/flags")
    Call<List<Ranking>> paises();

    @GET("api/ranking/map-continents")
    Call<List<Ranking>> mapas();

    @POST("api/ranking/create")
    Call<Ranking> createRanking(@Body Ranking ranking);

    @GET("api/country")
    Call<List<Paises>> getPaises();

    @PUT("api/country/{id}")
    Call<Paises>editCountry(@Path("id") String id, @Body Paises pais);

    @POST("api/country/")
    Call<Paises>createCountry(@Body Paises pais);

    @DELETE("api/country/{id}")
    Call<Paises>deleteCountry(@Path("id") String id);

}
