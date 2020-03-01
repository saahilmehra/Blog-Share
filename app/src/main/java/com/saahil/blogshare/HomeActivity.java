package com.saahil.blogshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity implements PostAdapter.ItemClicked {
    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Posts> arrayList;
    JsonPlaceHolderApi jsonPlaceHolderApi;
    ProgressDialog progressDialog;
    Button btnAddPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.29.214:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);

        recyclerView=findViewById(R.id.rvList);
        recyclerView.setHasFixedSize(true);
        arrayList=new ArrayList<>();

        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getPosts();

        btnAddPost=findViewById(R.id.btnAddPost);
        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this, AddPostActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClicked(int index) {
        Toast.makeText(this, "surname: "+arrayList.get(index).getId(), Toast.LENGTH_SHORT).show();
        Intent i=new Intent(HomeActivity.this, PostDetailActivity.class);
        i.putExtra("id", arrayList.get(index).getId());
        startActivity(i);
    }

    private void getPosts(){
        Call<ArrayList<Posts>> call=jsonPlaceHolderApi.getPosts();
        call.enqueue(new Callback<ArrayList<Posts>>() {
            @Override
            public void onResponse(Call<ArrayList<Posts>> call, Response<ArrayList<Posts>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(HomeActivity.this, "Code: "+ response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                arrayList=response.body();
                myAdapter=new PostAdapter(HomeActivity.this, arrayList);
                recyclerView.setAdapter(myAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<Posts>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }
}
