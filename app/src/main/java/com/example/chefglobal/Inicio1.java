package com.example.chefglobal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;

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
                        HomeFragment h = new HomeFragment();
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
                        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,c).commit();
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
                int id = item.getItemId();

                if (id == R.id.mChat) {
                    // Cargar el fragmento de chat
                    Chat c = new Chat();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, c).commit();
                } else if (id == R.id.recetas) {
                    // Cargar el fragmento de recetas
                    Recetas r = new Recetas();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, r).commit();
                } else if (id == R.id.notificacion) {
                    // Iniciar la actividad de notificaciones
                    startActivity(new Intent(Inicio1.this, Notificaciones.class));
                } else if (id == R.id. invitaAmigos) {
                    // Implementar lógica para compartir la app
                    // Puedes usar un Intent para compartir el enlace de descarga de la app
                } else if (id == R.id.ajustes) {
                    // Iniciar la actividad de ajustes
                    startActivity(new Intent(Inicio1.this, Ajustes.class));
                } else if (id == R.id.ayuda) {
                    // Iniciar la actividad de ayuda
                    startActivity(new Intent(Inicio1.this, Ayuda.class));
                } else if (id == R.id.cerrarsesion) {
                    FirebaseAuth.getInstance().signOut(); // Cierra la sesión de Firebase
                    // También puedes eliminar la información de preferencias compartidas si lo deseas
                    String preferenciaNombre = "MisPreferencias";
                    SharedPreferences misPreferencias = getSharedPreferences(preferenciaNombre, MODE_PRIVATE);
                    SharedPreferences.Editor editor = misPreferencias.edit();
                    editor.remove("correo");
                    editor.apply();
                    finish(); // Finaliza la actividad actual
                }

                // Cerrar el cajón de navegación después de la selección
                DrawerLayout dl = findViewById(R.id.inicio);
                dl.closeDrawer(GravityCompat.START);
                return true;
            }
            // Método para cargar un fragmento en el contenedor
            private void openFragment(Fragment fragment) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

                // Cerrar el cajón de navegación después de la selección
                DrawerLayout dl = findViewById(R.id.inicio);
                dl.closeDrawer(GravityCompat.START);
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