package com.dam.notes;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.dam.notes.RecordatorioDataSource.RecuperarRecordatorioCallback;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{



    private FloatingActionButton agregar;
    private RecyclerView recyclerView;
    private RecordatorioAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<RecordatorioModel> listaRecordatorios;
    private LinearLayout cartel;
    private DrawerLayout drawer;
    public static String RECORDATORIO = "com.example.tp3.RECORDATORIO";
    private static String NAME = "nevilleSettings";
    private RecordatorioRepository repositorioR;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        agregar= findViewById(R.id.agregar);
        cartel=findViewById(R.id.cartelito);
        recyclerView = findViewById(R.id.listado);


        String user="";
        String pass="";
        repositorioR = new RecordatorioRepository(new RecordatorioAPIRestDataSource(user,pass));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        drawer = (DrawerLayout) findViewById(R.id.drawer_lay);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("21496", name, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        listaRecordatorios = new ArrayList<RecordatorioModel>();



        mAdapter = new RecordatorioAdapter(listaRecordatorios);
        agregar.setOnClickListener(this);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);



        repositorioR.traerRecordatorios(new RecuperarRecordatorioCallback() {
            @Override
            public void resultado(boolean exito, List<RecordatorioModel> recordatorios) {
                if(recordatorios!=null && listaRecordatorios.isEmpty()){
                    if(!exito){
                        Toast.makeText(getApplicationContext(),getString(R.string.toast_error), Toast.LENGTH_LONG).show();
                    }
                    else {
                        mAdapter.setLista(recordatorios);
                        mAdapter.notifyDataSetChanged();
                        if (!recordatorios.isEmpty()) {
                            ocultarCartel();
                        }
                    }
                }
            }
        });
    }

    public void ocultarCartel(){
        if(cartel.getVisibility()== View.VISIBLE){
            cartel.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null && resultCode == Activity.RESULT_OK){

            ocultarCartel();

            RecordatorioModel n = data.getExtras().getParcelable("r_parcel");


            repositorioR.guardarRecordatorio(new RecordatorioDataSource.GuardarRecordatorioCallback() {
                @Override
                public void resultado(boolean exito){
                    if(!exito){
                        Toast.makeText(getApplicationContext(),getString(R.string.toast_ok), Toast.LENGTH_LONG).show();
                    }
                }
            }, n);


            mAdapter.add(n);
            mAdapter.notifyDataSetChanged();


            SharedPreferences sharedPreferences = getSharedPreferences(NAME,0);
            if(sharedPreferences.getBoolean("notif",true)){
                int song = sharedPreferences.getInt("song",0);
                notificar(n,song);
            }
        }
    }
    private void notificar(RecordatorioModel n,int song){
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, RecordatorioReceiver.class);



        intent.putExtra("not_texto",n.getMensaje());
        intent.putExtra("song",song);
        intent.setAction(RECORDATORIO);
        PendingIntent intped= PendingIntent.getBroadcast(MainActivity.this,n.hashCode(),intent,PendingIntent.FLAG_UPDATE_CURRENT);



        Calendar t = Calendar.getInstance();
        t.setTime(n.getFecha());
        t.set(Calendar.SECOND,0);
        alarm.set(AlarmManager.RTC_WAKEUP,t.getTimeInMillis(), intped);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (drawer.isDrawerOpen(GravityCompat.START)==false || drawer.getVisibility()==View.INVISIBLE) {
                    drawer.setVisibility(View.VISIBLE);
                    drawer.openDrawer(GravityCompat.START);
                }
                else{
                    drawer.closeDrawer(GravityCompat.START);
                    drawer.setVisibility(View.INVISIBLE);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.agregar:
                Intent sig = new Intent(MainActivity.this, CrearRecordatorio.class);
                startActivityForResult(sig,0);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.itemSettings:
                drawer.closeDrawer(GravityCompat.START);
                drawer.setVisibility(View.INVISIBLE);
                Intent sett = new Intent(MainActivity.this, Ajustes.class);
                startActivityForResult(sett,36);
                break;
        }
        return false;
    }

}