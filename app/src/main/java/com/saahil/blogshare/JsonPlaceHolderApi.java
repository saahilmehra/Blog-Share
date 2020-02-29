package com.saahil.blogshare;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {

    @POST("account/api/token/")
    Call<User> loginUser(@Body User user);
}
