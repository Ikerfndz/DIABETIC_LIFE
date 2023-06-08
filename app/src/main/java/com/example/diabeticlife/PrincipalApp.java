package com.example.diabeticlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PrincipalApp extends AppCompatActivity {

    private DatabaseReference marksRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_app);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        TextView txt = (TextView) findViewById(R.id.textViewDosisDiaria);
        TextView fechatxt = (TextView) findViewById(R.id.Textfecha);

        LocalDate fecha  = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaString = fecha.format(formatter);
        fechatxt.setText("*" + fechaString + "*");


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
                        String fechaRegistrada = dosisDiaria.getFechaDiaDiaria();
                        String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                        if (!fechaRegistrada.equals(fechaActual)) {
                            double dosisDiariaFija = dosisDiaria.getDosisDiariaFija();
                            dosisDiaria.setDosisDiariaMostrar(dosisDiariaFija);
                            dosisDiaria.setFechaDiaDiaria(fechaActual);

                            ds.getRef().setValue(dosisDiaria);
                        }

                        double dosisDiariaMostrar = dosisDiaria.getDosisDiariaMostrar();
                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                        decimalFormat.setRoundingMode(RoundingMode.DOWN);
                        String resultado = decimalFormat.format(dosisDiariaMostrar);
                        if (dosisDiariaMostrar <= 0.15) {
                            resultado = "COMPLETADO";
                        }
                        txt.setText(resultado);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar el error en caso de que la consulta sea cancelada
            }
        });









        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bottom_home:
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

                case R.id.bottom_calculate:
                    startActivity(new Intent(this, CalcularInsulina.class));
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

    }
}