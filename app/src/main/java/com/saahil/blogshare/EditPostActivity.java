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

public class EditPostActivity extends AppCompatActivity {
    EditText etTitle, etSlug, etBody;
    RadioButton rbDraft, rbPublished;
    Button btnEditPost;
    String status="draft";
    private int id;
    private ProgressDialog progressDialog, progressDialog2;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.29.214:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        progressDialog2=new ProgressDialog(this);

        etTitle=findViewById(R.id.etTitle);
        etSlug=findViewById(R.id.etSlug);
        etBody=findViewById(R.id.etBody);
        rbDraft=findViewById(R.id.rbDraft);
        rbPublished=findViewById(R.id.rbPublished);
        btnEditPost=findViewById(R.id.btnEditPost);

        id=getIntent().getIntExtra("id", -1);

        setContentText();

        btnEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkStatus();
                checkValidations();
            }
        });
    }

    private void setContentText() {
        Call<Posts> call=jsonPlaceHolderApi.getPost(id);
        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(EditPostActivity.this, "Code: "+ response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                Posts post=response.body();
                etTitle.setText(post.getTitle());
                etSlug.setText(post.getSlug());
                etBody.setText(post.getBody());

                etTitle.setFocusableInTouchMode(true);
                etSlug.setFocusableInTouchMode(true);
                etBody.setFocusableInTouchMode(true);

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                Toast.makeText(EditPostActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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
        
        progressDialog2.setMessage("Loading...");
        progressDialog2.setCanceledOnTouchOutside(false);
        progressDialog2.show();

        editPost(title, slug, body);
    }

    private void editPost(final String title, final String slug, final String body) {
        Posts post=new Posts(title, body, slug, status);
        Call<Posts> call=jsonPlaceHolderApi.editPost(id, post);

        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                if(!response.isSuccessful()){
                    progressDialog2.dismiss();
                    Toast.makeText(EditPostActivity.this, "Code:" + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Posts postResponse=response.body();

                Toast.makeText(EditPostActivity.this, "Post edited successfully!", Toast.LENGTH_SHORT).show();

                if(status.equals("draft")){
                    progressDialog2.dismiss();
                    Intent intent=new Intent(EditPostActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
                else{
                    progressDialog2.dismiss();
                    Intent intent=new Intent(EditPostActivity.this, PostDetailActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                progressDialog2.dismiss();
                Toast.makeText(EditPostActivity.this, "failure: "+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
