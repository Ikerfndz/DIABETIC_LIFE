package com.example.diabeticlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RegistrarDosis extends AppCompatActivity {

    private DatabaseReference marksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_dosis);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Dosis");
        marksRef = userRef.child("Id Usuario: " + userId);


        EditText unidadTxt = (EditText) findViewById(R.id.unidadesDosisTxt);
        EditText zonaTxt = (EditText) findViewById(R.id.lugarDosisTxt);
        EditText obsTxt = (EditText) findViewById(R.id.observacionesDosisTxt);
        Button registrarDosis = (Button) findViewById(R.id.RegistroButton);
        Spinner tipoTxt = (Spinner) findViewById(R.id.spinnerDosis);



        registrarDosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String udidadStr = unidadTxt.getText().toString();
                String zonaStr = zonaTxt.getText().toString();
                String tipoStr = tipoTxt.getSelectedItem().toString();
                String obsStr = obsTxt.getText().toString();
                LocalDate fecha  = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String fechaString = fecha.format(formatter);

                if(udidadStr.isEmpty() || zonaStr.isEmpty() || tipoStr.isEmpty()){
                    // Si algún campo está vacío, muestra un mensaje de error
                    Toast.makeText(RegistrarDosis.this,
                            "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();

                }else if(esConvertibleADouble(udidadStr)== false){
                    // Si en unidad introduce algún caracter que no sea numerico, muestra un mensaje de error
                    Toast.makeText(RegistrarDosis.this,
                            "Por favor, introduce valores correctos en unidades (0.0)", Toast.LENGTH_SHORT).show();

                }else {

                    // Si todos los campos están completos y
                    // la unidad es  válida, crea la dosis y lo agrega a la base de datos
                    Dosis d = new Dosis(udidadStr,tipoStr, fechaString, zonaStr, obsStr);
                    marksRef.push().setValue(d);
                    Toast.makeText(RegistrarDosis.this, "Dosis registrada", Toast.LENGTH_SHORT).show();
                    // Limpia los campos de texto
                    unidadTxt.setText("");
                    zonaTxt.setText("");
                    obsTxt.setText("");


                    FirebaseUser user2 = FirebaseAuth.getInstance().getCurrentUser();
                    String userId2 = user2.getUid();
                    DatabaseReference userRef2 = FirebaseDatabase.getInstance().getReference("DosisDiaria");
                    marksRef = userRef2.child("Id Usuario: " + userId2);

                    marksRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            DosisDiaria dd = null;
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                dd = ds.getValue(DosisDiaria.class);
                                // Obtener los valores del snapshot y asignarlos a la instancia existente
                                double dosisDiariaMostrar = dd.getDosisDiariaMostrar();
                                double uF = Double.parseDouble(udidadStr);
                                double resultado = dosisDiariaMostrar - uF;
                                dd.setDosisDiariaMostrar(resultado);
                                // Actualizar el campo 'dosisDiariaMostrar' en la base de datos
                                ds.getRef().child("dosisDiariaMostrar").setValue(resultado);
                            }
                            // Verificar si se encontró un objeto DosisDiaria válido
                            if (dd != null) {
                                double dosisDiariaMostrar = dd.getDosisDiariaMostrar();

                                dd.setDosisDiariaMostrar(dosisDiariaMostrar);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Manejar el error en caso de que la consulta sea cancelada
                        }
                    });
                }


            }
        });






















        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_dosis);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(this, PrincipalApp.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    return true;

                case R.id.bottom_search:
                    startActivity(new Intent(this, BuscarDosis.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;

                case R.id.bottom_dosis:

                    return true;

                case R.id.bottom_calculate:
                    startActivity(new Intent(this, CalcularInsulina.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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

    public boolean esConvertibleADouble(String str) {
        try {
            Double.parseDouble(str);
            return true;  // El String se puede convertir a un double
        } catch (NumberFormatException e) {
            return false;  // El String no se puede convertir a un double
        }
    }
}