package com.example.diabeticlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CalcularInsulina extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcular_insulina);

        Button diaria = (Button)  findViewById(R.id.botonDosisDiaria);
        Button cho = (Button)  findViewById(R.id.botonCalculoCHO);
        Button crr= (Button) findViewById(R.id.botonCalculoCorreccion);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_calculate);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bottom_home:
                    startActivity(new Intent(this,PrincipalApp.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    return true;

                case R.id.bottom_search:
                    startActivity(new Intent(this, BuscarDosis.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;

                case R.id.bottom_dosis:
                    startActivity(new Intent(this, RegistrarDosis.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;

                case R.id.bottom_account:
                    startActivity(new Intent(this, Perfil.class));

                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();

                    return true;
            }
            return false;
        });


        diaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalcularInsulina.this, CalculoDosisDiaria.class);
                startActivity(intent);
            }
        });

        cho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalcularInsulina.this, FactorCorreccionCHO.class);
                startActivity(intent);
            }
        });


        crr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalcularInsulina.this, FactorCorreccion.class);
                startActivity(intent);
            }
        });



    }
}