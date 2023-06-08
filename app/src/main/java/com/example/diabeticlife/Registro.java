package com.example.diabeticlife;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Registro extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference marksRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        EditText nombre = (EditText) findViewById(R.id.nombreregistrotxt);
        EditText telefono = (EditText) findViewById(R.id.telefonoregistro);
        EditText mail = (EditText) findViewById(R.id.mailregistro);
        EditText password = (EditText) findViewById(R.id.passwordregistro);
        EditText passwordRepe = (EditText) findViewById(R.id.passwordRepetidaregistro);
        EditText apellidos = (EditText) findViewById(R.id.apellidosregistrotxt);

        Button registro = (Button) findViewById(R.id.RegistroButton);


        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String mailStr = mail.getText().toString();
                String pass = password.getText().toString();
                String passRepe = passwordRepe.getText().toString();
                String nombreStr = nombre.getText().toString();
                String apellidosStr = apellidos.getText().toString();
                String telefonoStr = telefono.getText().toString();

                if (mailStr.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(Registro.this, "Registro fallido, algun campo vacio",
                            Toast.LENGTH_SHORT).show();
                } else if (nombreStr.isEmpty() || apellidosStr.isEmpty()) {
                    Toast.makeText(Registro.this, "Registro fallido, algun campo vacio",
                            Toast.LENGTH_SHORT).show();
                } else if (telefonoStr.isEmpty() || !telefonoStr.matches("\\d{9}")) {
                    Toast.makeText(Registro.this, "Registro fallido, campo telefono no valido",
                            Toast.LENGTH_SHORT).show();
                } else if (pass.equals(passRepe)) {


                    registrar(mailStr, pass, nombreStr, apellidosStr, telefonoStr);


                } else {
                    Toast.makeText(Registro.this, "Registro fallido, contraseñas no coinciden",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });


    }


    private void registrar(String mail, String pass, String nombre, String apellidos, String telefono) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(Registro.this, "Registro Completado",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            login(mail, pass);
                            FirebaseUser userr = FirebaseAuth.getInstance().getCurrentUser();
                            String userId = userr.getUid();

                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Usuarios");
                            marksRef = userRef.child("Usuario id: " + userId);
                            LocalDate fecha = LocalDate.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            String fechaString = fecha.format(formatter);

                            Usuario u = new Usuario(nombre, apellidos, telefono, mail, fechaString);
                            marksRef.push().setValue(u);


                            Intent intent = new Intent(Registro.this, Login.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Registro.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    private void login(String mail, String pass) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {


                        }
                    }
                });
    }


}