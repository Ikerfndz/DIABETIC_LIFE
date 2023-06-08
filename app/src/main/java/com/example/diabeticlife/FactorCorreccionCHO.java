package com.example.diabeticlife;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class FactorCorreccionCHO extends AppCompatActivity {


    private DatabaseReference marksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factor_correccion_cho);

        EditText uniDiaria = (EditText) findViewById(R.id.unidadesChoTxt);
        EditText gCHO = (EditText) findViewById(R.id.gCHOChoTxt);
        Button calcular = (Button) findViewById(R.id.CalculoCHOButton);
        TextView result = (TextView)  findViewById(R.id.coreccionChoText);
        Button insertar = (Button) findViewById(R.id.InsertarDosisCHOButton);
        Button atras = (Button) findViewById(R.id.atrasCHOButton);
        EditText zona = (EditText) findViewById(R.id.zonaChoTxt);

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FactorCorreccionCHO.this, CalcularInsulina.class);
                startActivity(intent);
            }
        });

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



                        double dosisDiariaFija = dosisDiaria.getDosisDiariaFija();
                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                        decimalFormat.setRoundingMode(RoundingMode.DOWN);
                        String resultado = decimalFormat.format(dosisDiariaFija);

                        uniDiaria.setText(resultado);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar el error en caso de que la consulta sea cancelada
            }
        });

        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gramostxt= gCHO.getText().toString();
                String unidadestxt = uniDiaria.getText().toString();

                if(esConvertibleADouble(gramostxt) == false || esConvertibleADouble(unidadestxt) == false){
                    Toast.makeText(FactorCorreccionCHO.this, "Por favor, completa correctamente el campo. (Decimal con .)", Toast.LENGTH_SHORT).show();
                }else{
                    Double gramos = Double.parseDouble(gramostxt);

                    Double unidades =  Double.parseDouble(unidadestxt);
                    Double relaccion = 500 / unidades;

                    Double resultado = gramos/relaccion ;
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    decimalFormat.setRoundingMode(RoundingMode.DOWN);
                    String s = decimalFormat.format(resultado);

                    result.setText(s);


                }
            }
        });


        insertar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String zonaStr = zona.getText().toString();
                String unidad = result.getText().toString();
                if (zonaStr.isEmpty() || unidad.isEmpty()){
                    Toast.makeText(FactorCorreccionCHO.this, "Por favor, rellena los campos", Toast.LENGTH_SHORT).show();

                }else{

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String userId = user.getUid();
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Dosis");
                    marksRef = userRef.child("Id Usuario: " + userId);

                    LocalDate fecha = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String fechaString = fecha.format(formatter);
                    String tipo = "Rapida";
                    String obsStr = "Cobertura CHO";
                    Dosis d = new Dosis(unidad,tipo, fechaString, zonaStr, obsStr);
                    marksRef.push().setValue(d);
                    Toast.makeText(FactorCorreccionCHO.this, "Dosis registrada", Toast.LENGTH_SHORT).show();



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
                                double uF = Double.parseDouble(unidad);
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

                    // Limpia los campos de texto
                    gCHO.setText("");
                    zona.setText("");
                    result.setText("");

                }



            }
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