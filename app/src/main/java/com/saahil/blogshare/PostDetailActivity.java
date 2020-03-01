package com.saahil.blogshare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostDetailActivity extends AppCompatActivity {
    TextView tvTitle, tvPublish, tvBody;
    private int id;
    JsonPlaceHolderApi jsonPlaceHolderApi;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.29.214:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);

        tvTitle=findViewById(R.id.tvTitle);
        tvPublish=findViewById(R.id.tvPublish);
        tvBody=findViewById(R.id.tvBody);

        id=getIntent().getIntExtra("id", -1);

        getPost();
    }

    private void getPost() {
        Call<Posts> call=jsonPlaceHolderApi.getPost(id);
        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(PostDetailActivity.this, "Code: "+ response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                Posts post=response.body();
                tvTitle.setText(post.getTitle());
                tvPublish.setText(post.getPublished());
                tvBody.setText(post.getBody());
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                Log.e("Error",t.getMessage());
                Toast.makeText(PostDetailActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }
}
