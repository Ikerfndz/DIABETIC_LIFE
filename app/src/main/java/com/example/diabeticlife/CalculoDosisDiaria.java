package com.example.diabeticlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CalculoDosisDiaria extends AppCompatActivity {

    private DatabaseReference marksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo_dosis_diaria);

        Button atras = (Button) findViewById(R.id.atrasDoDiariaButton);
        EditText kgTxt = (EditText) findViewById(R.id.kgETxt);
        Button calcular = (Button) findViewById(R.id.CalculoDoDiariaButton);
        TextView texto = (TextView) findViewById(R.id.DiariaText);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("DosisDiaria");
        marksRef = userRef.child("Id Usuario: " + userId);


        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalculoDosisDiaria.this, CalcularInsulina.class);
                startActivity(intent);
            }
        });



        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kilos = kgTxt.getText().toString();

                if (kilos.isEmpty()) {
                    Toast.makeText(CalculoDosisDiaria.this, "Por favor, completa el campo", Toast.LENGTH_SHORT).show();
                } else if (!esConvertibleADouble(kilos)) {
                    Toast.makeText(CalculoDosisDiaria.this, "Por favor, completa correctamente el campo. (Decimal con .)", Toast.LENGTH_SHORT).show();
                } else {
                    double kgD = Double.parseDouble(kilos);
                    double i = 0.55;

                    Query query = marksRef.orderByChild("userId").equalTo(userId);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            double dosisDiaria = kgD * i;
                            DecimalFormat decimalFormat = new DecimalFormat("#.##");
                            decimalFormat.setRoundingMode(RoundingMode.DOWN);
                            String resultado = decimalFormat.format(dosisDiaria);
                            String mostrarDosis = "Tu dosis diaria es de: " + resultado + " unidades";
                            texto.setText(mostrarDosis);

                            boolean registroExistente = false;

                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                DosisDiaria dosisDiariaObj = childSnapshot.getValue(DosisDiaria.class);
                                if (dosisDiariaObj != null && dosisDiariaObj.getUserId().equals(userId)) {
                                    // El usuario ya tiene un registro, actualizar el campo deseado
                                    dosisDiariaObj.setDosisDiariaFija(dosisDiaria);
                                    dosisDiariaObj.setDosisDiariaMostrar(dosisDiaria);
                                    dosisDiariaObj.setFechaDiaDiaria(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                                    marksRef.child(childSnapshot.getKey()).setValue(dosisDiariaObj);
                                    registroExistente = true;
                                    break;
                                }
                            }

                            if (!registroExistente) {
                                String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                String id = userId.toString();
                                DosisDiaria dd = new DosisDiaria(dosisDiaria, dosisDiaria, fecha, id);
                                marksRef.push().setValue(dd);
                            } else {
                                Toast.makeText(CalculoDosisDiaria.this, "ACTUALIZADO", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Manejar el error en caso de que la consulta sea cancelada
                        }
                    });
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