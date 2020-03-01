package com.saahil.blogshare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddPostActivity extends AppCompatActivity {
    EditText etTitle, etSlug, etBody;
    RadioButton rbDraft, rbPublished;
    Button btnAdd;
    String status="draft";
    private ProgressDialog progressDialog;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.29.214:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);

        progressDialog=new ProgressDialog(this);

        etTitle=findViewById(R.id.etTitle);
        etSlug=findViewById(R.id.etSlug);
        etBody=findViewById(R.id.etBody);
        rbDraft=findViewById(R.id.rbDraft);
        rbPublished=findViewById(R.id.rbPublished);
        btnAdd=findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkStatus();
                checkValidations();
            }
        });
    }

    private void checkValidations() {
        String title=etTitle.getText().toString().trim();
        String slug=etSlug.getText().toString().trim();
        String body=etBody.getText().toString().trim();

        if(TextUtils.isEmpty(title)){
            etTitle.setError("Required Field..");
            return;
        }
        if(TextUtils.isEmpty(slug)){
            etSlug.setError("Required Field..");
            return;
        }
        if(TextUtils.isEmpty(body)){
            etBody.setError("Required Field..");
            return;
        }

        progressDialog.setMessage("Processing..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        addPost(title, slug, body);
    }

    private void addPost(final String title, final String slug, final String body) {
        Posts post=new Posts(title, body, slug, status);
        Call<Posts> call=jsonPlaceHolderApi.addPost(post);

        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                if(!response.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(AddPostActivity.this, "Code:" + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Posts postResponse=response.body();
                Toast.makeText(AddPostActivity.this, "Post added successfully!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Intent intent=new Intent(AddPostActivity.this, HomeActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                Toast.makeText(AddPostActivity.this, "failure: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void checkStatus() {
        if(rbDraft.isChecked()){
            status="draft";
        }
        if(rbPublished.isChecked()){
            status="published";
        }
    }
}
