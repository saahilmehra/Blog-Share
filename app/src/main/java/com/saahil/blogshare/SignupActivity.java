package com.saahil.blogshare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {
    EditText etEmail, etFirstName, etLastName, etContact, etPassword, etRePassword;
    Button btnSignup;
    TextView tvLogin;
    private ProgressDialog progressDialog;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.29.214:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);

        progressDialog=new ProgressDialog(this);

        etEmail=findViewById(R.id.etEmail);
        etFirstName=findViewById(R.id.etFirstName);
        etLastName=findViewById(R.id.etLastName);
        etContact=findViewById(R.id.etContact);
        etPassword=findViewById(R.id.etPassword);
        etRePassword=findViewById(R.id.etRePassword);
        btnSignup=findViewById(R.id.btnSignup);
        tvLogin=findViewById(R.id.tvLogin);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidations();
            }
        });
    }
    private void checkValidations(){
        String email=etEmail.getText().toString().trim();
        String first_name=etFirstName.getText().toString().trim();
        String last_name=etLastName.getText().toString().trim();
        String contact=etContact.getText().toString().trim();
        String password=etPassword.getText().toString().trim();
        String re_password=etRePassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            etEmail.setError("Required Field..");
            return;
        }
        if(TextUtils.isEmpty(first_name)){
            etFirstName.setError("Required Field..");
            return;
        }
        if(TextUtils.isEmpty(last_name)){
            etLastName.setError("Required Field..");
            return;
        }
        if(TextUtils.isEmpty(contact)){
            etContact.setError("Required Field..");
            return;
        }
        if(TextUtils.isEmpty(password)){
            etPassword.setError("Required Field..");
            return;
        }
        if(TextUtils.isEmpty(re_password)){
            etRePassword.setError("Required Field..");
            return;
        }
        if(!password.equals(re_password)){
            Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Processing..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Log.e("a",contact);
        int con=Integer.parseInt(contact);
        Log.e("b",contact);
        createUser(email, first_name, last_name, con, password);
        progressDialog.dismiss();
    }
    private void createUser(final String email, final String first_name, final String last_name, final int contact, final String password){
        User user=new User(email, first_name, last_name, contact, password);
        Call<User> call=jsonPlaceHolderApi.createUser(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code()==400){
                    Toast.makeText(SignupActivity.this, "This email already exists!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!response.isSuccessful()){
                    Toast.makeText(SignupActivity.this, "Code:" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                User userResponse=response.body();
                Intent intent=new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "failure: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
