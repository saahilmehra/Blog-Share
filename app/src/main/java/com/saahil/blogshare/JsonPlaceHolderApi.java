package com.saahil.blogshare;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {

    @POST("account/api/token/")
    Call<User> loginUser(@Body User user);

    @POST("account/users/")
    Call<User> createUser(@Body User user);


    @Headers("Authorization:Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNTgzODU1MDAxLCJqdGkiOiIwMjg5MTdlZDE3OGI0OGI3OTBkYzM5ZjQ2ZmI4MWZlNCIsInVzZXJfaWQiOjF9.tNUH7YDWQpP6QfWwozzm6AALDlQZPzn1GrDcO6EO7N4")
    @GET("blog/api/")
    Call<ArrayList<Posts>>getPosts();
}
