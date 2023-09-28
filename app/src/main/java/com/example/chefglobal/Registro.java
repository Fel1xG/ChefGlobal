package com.example.chefglobal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Registro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }

    public void register2(View v){
        EditText campo1 = this.findViewById(R.id.correo2);
        String correo2 = campo1.getText().toString();
        EditText campo2 = this.findViewById(R.id.contrasenia2);
        String contrasenia2 = campo2.getText().toString();
        EditText campo3 = this.findViewById(R.id.contrasenia3);
        String contrasenia3 = campo3.getText().toString();

        if (isValidRegistration(correo2, contrasenia2, contrasenia3)) {
            Intent i = new Intent(this,Confirmacion1.class);
            startActivity(i);
        }
    }

    private boolean isValidRegistration(String correo2, String contrasenia2, String contrasenia3) {
        if (correo2.equals("felix@gmail.com") && contrasenia2.equals("123") && contrasenia3.equals("123")) {
            return true;
        }
        return false;
    }


}