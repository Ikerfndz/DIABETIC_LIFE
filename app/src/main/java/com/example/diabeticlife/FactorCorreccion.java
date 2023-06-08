package com.example.diabeticlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class FactorCorreccion extends AppCompatActivity {

    private DatabaseReference marksRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factor_correccion);

        Button atras = (Button) findViewById(R.id.atrasCrrButton);
        TextView glucemia = (TextView) findViewById(R.id.textviewcantidad);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("DosisDiaria");
        marksRef = userRef.child("Id Usuario: " + userId);

        marksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    DosisDiaria dosisDiaria = ds.getValue(DosisDiaria.class);
                    if (dosisDiaria != null) {


                        double dosisDiariaCalculo = dosisDiaria.getDosisDiariaMostrar();
                        double resultado = 1800 / dosisDiariaCalculo;

                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                        decimalFormat.setRoundingMode(RoundingMode.DOWN);
                        String resultadoStr = decimalFormat.format(resultado);


                        glucemia.setText(resultadoStr + " mg/dl");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar el error en caso de que la consulta sea cancelada
            }
        });




        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FactorCorreccion.this, CalcularInsulina.class);
                startActivity(intent);
            }
        });

    }
}