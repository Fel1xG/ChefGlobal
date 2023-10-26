package com.example.chefglobal;

import androidx.annotation.NonNull;
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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Inicio1 extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio1);

        // Inicializa Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Referencia al Toolbar
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        TabLayout tl = (TabLayout) findViewById(R.id.tablayout);
        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        Home h = new Home();
                        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, h).commit();
                        break;
                    case 1:
                        Buscar b = new Buscar();
                        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, b).commit();
                        break;
                    case 2:
                        Agregar a = new Agregar();
                        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, a).commit();
                        break;
                    case 3:
                        Recetas r = new Recetas();
                        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, r).commit();
                        break;
                    case 4:
                        Chat c = new Chat();
                        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, c).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Código cuando un tab deja de estar seleccionado
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Código cuando un tab se vuelve a seleccionar
            }
        });

        NavigationView nav = (NavigationView) findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.mChat) {
                    Chat c = new Chat();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, c).commit();
                } else if (id == R.id.recetas) {
                    Recetas r = new Recetas();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, r).commit();
                } else if (id == R.id.notificacion) {
                    // Inicia la actividad Notificaciones y pasa el nombre de usuario
                    String nombreUsuario = getUsernameFromEmail(mAuth.getCurrentUser().getEmail());
                    Intent intent = new Intent(Inicio1.this, Notificaciones.class);
                    intent.putExtra("nombreUsuario", nombreUsuario);
                    startActivity(intent);
                } else if (id == R.id.invitaAmigos) {
                    // Implementa lógica para compartir la app
                    // Puedes usar un Intent para compartir el enlace de descarga de la app
                } else if (id == R.id.ajustes) {
                    startActivity(new Intent(Inicio1.this, Ajustes.class));
                } else if (id == R.id.ayuda) {
                    startActivity(new Intent(Inicio1.this, Ayuda.class));
                } else if (id == R.id.cerrarsesion) {
                    FirebaseAuth.getInstance().signOut(); // Cierra la sesión de Firebase
                    String preferenciaNombre = "MisPreferencias";
                    SharedPreferences misPreferencias = getSharedPreferences(preferenciaNombre, MODE_PRIVATE);
                    SharedPreferences.Editor editor = misPreferencias.edit();
                    editor.remove("correo");
                    editor.apply();
                    finish();
                }

                DrawerLayout dl = findViewById(R.id.inicio);
                dl.closeDrawer(GravityCompat.START);
                return true;
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
                if (dl.isDrawerOpen(GravityCompat.START)) {
                    dl.closeDrawer(GravityCompat.START);
                } else {
                    dl.openDrawer((int) Gravity.START);
                }
            }
        });

        // Actualizar el encabezado con el nombre de usuario y el correo
        actualizarEncabezado();
    }

    private void actualizarEncabezado() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // El usuario ha iniciado sesión
            String email = user.getEmail();
            String username = getUsernameFromEmail(email);

            NavigationView navigationView = findViewById(R.id.nav);
            View headerView = navigationView.getHeaderView(0);
            TextView tvNombreUsuario = headerView.findViewById(R.id.tvNombreUsuario);
            TextView tvCorreoUsuario = headerView.findViewById(R.id.tvCorreoUsuario);

            tvNombreUsuario.setText(username);
            tvCorreoUsuario.setText(email);
        }
    }

    private String getUsernameFromEmail(String email) {
        if (email != null) {
            return email.substring(0, 7); // Cambia esto si deseas un nombre diferente
        } else {
            return "Usuario"; // Valor predeterminado en caso de que el correo sea nulo
        }
    }
}