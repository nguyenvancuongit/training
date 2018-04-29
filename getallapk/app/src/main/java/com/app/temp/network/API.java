package com.app.temp.network;

import com.app.temp.network.model.PostsResponse;
import com.app.temp.network.model.RepositoriesResponse;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by nguyen_van_cuong on 08/11/2017.
 */

public interface API {
    @GET("/posts")
    Flowable<PostsResponse> getPosts();

    @GET("users/{username}/repos")
    Flowable<RepositoriesResponse> publicRepositories(@Path("username") String username);
}
