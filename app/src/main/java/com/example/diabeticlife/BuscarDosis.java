package com.example.diabeticlife;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

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
import java.util.ArrayList;

public class BuscarDosis extends AppCompatActivity {

    CalendarView fechaHoraCalendar;

    private DatabaseReference marksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_dosis);

        fechaHoraCalendar = (CalendarView) findViewById(R.id.calendarView);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Dosis");
        marksRef = userRef.child("Id Usuario: " + userId);



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_search);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bottom_home:
                    startActivity(new Intent(this, PrincipalApp.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    return true;

                case R.id.bottom_search:

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
                    startActivity(new Intent(this, Perfil.class));

                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();

                    return true;

            }
            return false;
        });

/*
        marksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Dosis d;
                ArrayAdapter<Dosis> adaptador;
                ArrayList<Dosis> listado = new ArrayList<Dosis>();


                for (DataSnapshot ds: snapshot.getChildren()){

                     d = new Dosis();


                        //d.setFecha(fechaDosis);
                        d.setUnidades(ds.child("unidades").getValue(String.class));
                        d.setLugar(ds.child("lugar").getValue(String.class));
                        d.setTipo(ds.child("tipo").getValue(String.class));
                        listado.add(d);

                }
                adaptador = new ArrayAdapter<Dosis>(BuscarDosis.this, android.R.layout.simple_list_item_1, listado );
                listaDosis.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/
        // No funciona todavia
        /*
        fechaHoraCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                marksRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ListView listaDosis = (ListView) findViewById(R.id.lista);
                        Dosis d;
                        ArrayAdapter<Dosis> adaptador;
                        ArrayList<Dosis> listado = new ArrayList<Dosis>();

                        // Obtenemos la fecha seleccionada del CalendarView
                        long fechaSeleccionadaMillis = fechaHoraCalendar.getDate();
                        Instant instant = Instant.ofEpochMilli(fechaSeleccionadaMillis);
                        LocalDate fechaSeleccionada = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                        listado.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            d = new Dosis();

                            String fechaString = ds.child("fecha").getValue(String.class);
                            LocalDate fechaDosis = LocalDate.parse(fechaString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            if (fechaDosis.isEqual(fechaSeleccionada)) {
                                d.setUnidades(ds.child("unidades").getValue(String.class));
                                d.setLugar(ds.child("lugar").getValue(String.class));
                                d.setTipo(ds.child("tipo").getValue(String.class));
                                listado.add(d);
                            }
                        }

                        adaptador = new ArrayAdapter<Dosis>(BuscarDosis.this, android.R.layout.simple_list_item_1, listado );
                        listaDosis.setAdapter(adaptador);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });*/

        fechaHoraCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                marksRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ListView listaDosis = (ListView) findViewById(R.id.lista);
                        Dosis d;
                        ArrayAdapter<Dosis> adaptador;
                        ArrayList<Dosis> listado = new ArrayList<Dosis>();

                        // Obtenemos la fecha seleccionada del CalendarView
                        LocalDate fechaSeleccionada = LocalDate.of(i, i1+1, i2); // Los meses van de 0 a 11, por eso sumamos 1 a i1
                        listado.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            d = new Dosis();

                            String fechaString = ds.child("fecha").getValue(String.class);
                            LocalDate fechaDosis = LocalDate.parse(fechaString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            if (fechaDosis.isEqual(fechaSeleccionada)) {
                                d.setUnidades(ds.child("unidades").getValue(String.class));
                                d.setLugar(ds.child("lugar").getValue(String.class));
                                d.setTipo(ds.child("tipo").getValue(String.class));
                                listado.add(d);
                            }
                        }

                        adaptador = new ArrayAdapter<Dosis>(BuscarDosis.this, android.R.layout.simple_list_item_1, listado );
                        listaDosis.setAdapter(adaptador);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });



    }
}