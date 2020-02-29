package com.saahil.blogshare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.PointerIcon;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvSignup;
    private ProgressDialog progressDialog;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.29.214:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);

        progressDialog=new ProgressDialog(this);

        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        btnLogin=findViewById(R.id.btnLogin);
        tvSignup=findViewById(R.id.tvSignup);

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidations();
            }
        });
    }

    private void checkValidations() {
        String email=etEmail.getText().toString().trim();
        String pass=etPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            etEmail.setError("Required Field..");
            return;
        }
        if(TextUtils.isEmpty(pass)){
            etPassword.setError("Required Field..");
            return;
        }

        progressDialog.setMessage("Processing..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        loginUser(email, pass);
        progressDialog.dismiss();

    }

    private void loginUser(final String email, final String pass){
        User user=new User(email, pass);
        Call<User> call=jsonPlaceHolderApi.loginUser(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code()==401){
                    Toast.makeText(LoginActivity.this, "Account not found!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!response.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Code:" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                User userResponse=response.body();
                String token=userResponse.getAccess();
                Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "failure: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
