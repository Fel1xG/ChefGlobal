package com.example.chefglobal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class Inicio1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio1);
        //Referencia al Toolbar
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);





        TabLayout tl = (TabLayout) findViewById(R.id.tablayout);
        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //codificar cosas a ejecutar cuando le das tap a un tab
                int position = tab.getPosition();
                switch (position){
                    case 0:
                        //Llamar al fragmento home
                        Home h = new Home();
                        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,h).commit();
                        break;
                    case 1:
                        //Llamar al fragmento buscar
                        Buscar b = new Buscar();
                        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,b).commit();
                        break;
                    case 2:
                        //Llamar al fragmento agregar
                        Agregar a = new Agregar();
                        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,a).commit();
                        break;
                    case 3:
                        //Llamar al fragmento recetas
                        Recetas r = new Recetas();
                        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,r).commit();
                        break;
                    case 4:
                        //Llamar al fragmento chat
                        Chat c = new Chat();
                        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,c
                        ).commit();
                        break;

                }





            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Codificar cosas a ejecutar cuando un tab deja de estar seleccionado
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Codificar cosas a ejecutar cuando un tab se vuelve a seleccionar
            }
        });
        //incorporamos el menu lateral
        NavigationView nav = (NavigationView) findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //recuperar la opcion del menu
                int id = item.getItemId();
                if(id==R.id.mChat){
                    Toast.makeText(Inicio1.this, "JAAAAAAAAAA", Toast.LENGTH_SHORT).show();
                    finish();
                }
                Toast.makeText(getApplicationContext(), "alooo", Toast.LENGTH_SHORT).show();
                if(id==R.id.cerrarsesion){
                    SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = datos.edit();
                    editor.putString("correo","");
                    editor.apply();
                    Toast.makeText(Inicio1.this, "CHAO", Toast.LENGTH_SHORT).show();
                    System.out.println("CHAO");
                    finish();
                }
                return false;
            }
        });
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.inicio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                dl,
                R.string.abrir_drawer,
                R.string.cerrar_drawer
        );
        dl.addDrawerListener(toggle);
        toggle.syncState();

        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dl.isDrawerOpen(GravityCompat.START)){
                    dl.closeDrawer(GravityCompat.START);
                }
                else{
                    dl.openDrawer((int) Gravity.START);
                }
            }
        });
    }
}