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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Perfil extends AppCompatActivity {

    private DatabaseReference marksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        TextView nombreTxt = (TextView) findViewById(R.id.TextviewNombrePerfil);
        TextView apellidoTxt = (TextView) findViewById(R.id.TextviewApellidosPerfil);
        TextView telefonoTxt = (TextView) findViewById(R.id.TextviewTelefonoPerfil);
        TextView mailTxt = (TextView) findViewById(R.id.TextviewMailPerfil);
        TextView unidadesTxt = (TextView) findViewById(R.id.TextviewDUnidadesPerfil);
        TextView tipoTxt = (TextView) findViewById(R.id.TextviewDTipoPerfil);
        TextView fechaDosisTxt = (TextView) findViewById(R.id.TextviewDFechaPerfil);
        TextView lugarTxt = (TextView) findViewById(R.id.TextviewDLugarPerfil);

        TextView fechaAltaUsuTxt = (TextView) findViewById(R.id.TextviewUFechaAltaPerfil);


        rellenarDatosUsuario(nombreTxt, apellidoTxt, telefonoTxt, mailTxt, fechaAltaUsuTxt);
        rellenarDatosDosis(unidadesTxt, tipoTxt, fechaDosisTxt, lugarTxt);


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
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    return true;

                case R.id.bottom_dosis:

                    startActivity(new Intent(this, RegistrarDosis.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    return true;


                case R.id.bottom_calculate:
                    startActivity(new Intent(this, CalcularInsulina.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    return true;

                case R.id.bottom_account:

                    return true;
            }
            return false;
        });

    }


    public void rellenarDatosUsuario(TextView nombre, TextView apellido, TextView telefono, TextView mail, TextView fechaAlta) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Usuarios");
        DatabaseReference marksRef = userRef.child("Usuario id: " + userId); // Cambia "Id Usuario" por "Usuario id"

        marksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) { // Verifica si el snapshot tiene datos
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    Usuario usuario = userSnapshot.getValue(Usuario.class);
                    if (usuario != null) {
                        String nombreUsuario = usuario.getNombre();
                        String apellidosUsuario = usuario.getApellidos();
                        String telefonoUsuario = usuario.getTelefono();
                        String mailUsuario = usuario.getMail();
                        String fechaAltaUsuario = usuario.getFechaAlta();

                        // Asigna los valores a los TextView
                        nombre.setText(nombreUsuario);
                        apellido.setText(apellidosUsuario);
                        telefono.setText(telefonoUsuario);
                        mail.setText(mailUsuario);
                        fechaAlta.setText(fechaAltaUsuario);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Maneja el error en caso de que la consulta sea cancelada
            }
        });
    }

    public void rellenarDatosDosis(TextView unidades, TextView tipo, TextView fecha, TextView lugar) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Dosis").child("Id Usuario: " + userId);

        Query query = userRef.orderByKey().limitToLast(1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Dosis d = ds.getValue(Dosis.class);
                        if (d != null) {
                            String unidadesDosis = d.getUnidades();
                            String tipoDosis = d.getTipo();
                            String fechaDosis = d.getFecha();
                            String lugarDosis = d.getLugar();

                            unidades.setText(unidadesDosis);
                            tipo.setText(tipoDosis);
                            fecha.setText(fechaDosis);
                            lugar.setText(lugarDosis);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar el error en caso de que la consulta sea cancelada
            }
        });
    }

}