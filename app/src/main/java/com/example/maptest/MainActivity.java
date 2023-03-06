package com.example.maptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//Készítette: Némethy Viktor
public class MainActivity extends AppCompatActivity {

    private EditText myUsername, myPassword;
    private Button btnSignIn;

    private final String realUser = "user", realPass = "12345";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        myUsername = (EditText) findViewById(R.id.username);
        myPassword = (EditText) findViewById(R.id.password);
        btnSignIn = (Button) findViewById(R.id.sign_in_button);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = myUsername.getText().toString();
                String pass = myPassword.getText().toString();

                if(!username.equals("") && !pass.equals("")){

                    if(username.equals(realUser) && pass.equals(realPass))
                    {
                        toastMessage("Bejelentkezett a: " + username + " felhasználónévvel");
                        Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
                        startActivity(intent);
                    }
                    else
                        toastMessage("Hibás felhasználói adatok");
                }
                else
                {
                    toastMessage("Hiányos felhasználói adatok.");
                }
            }
        });
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}