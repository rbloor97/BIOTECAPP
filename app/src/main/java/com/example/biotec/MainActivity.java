package com.example.biotec;

import android.content.ClipData;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.example.biotec.ui.gallery.GalleryFragment;
import com.example.biotec.ui.home.HomeFragment;
import com.example.biotec.ui.slideshow.SlideshowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import kotlin.contracts.Returns;

import com.example.biotec.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    SQLiteDatabase myDB;

    public static int navItemIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        crearBase();
        
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Agregar Registro", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, RegistrarGanado.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)//, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if(savedInstanceState == null){
            navItemIndex = 0;
            loadHomeFragment();

        }
    }

    private void crearBase(){
        myDB = openOrCreateDatabase(BuildConfig.Nombre_Base, MODE_PRIVATE, null);
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS "+BuildConfig.Nombre_Tabla+" (codigo VARCHAR(20) not null, especie VARCHAR(100) , imagen varchar(100), raza VARCHAR(100), fecha_nacimiento varchar(8), genero char(1), edad varchar(20), peso REAL, tamanio REAL, estado varchar(20), proposito varchar(50), lote varchar(50), cod_madre varchar(20), cod_padre varchar(20), descripcion varchar(100), color varchar(20), ficha varchar(200), codigoMAGAD carchar(20) )"
        );
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS "+BuildConfig.Tabla_Reproductivos+" (id INTEGER PRIMARY KEY AUTOINCREMENT, codigo_ganado varchar(20), fecha_monta varchar(8), fecha_parto varchar(8), fecha_parto_real varchar(8), observacion varchar(100), num_servicios INTEGER, num_partos INTEGER, genero_cria char(1), codigo_cria varchar(10), estado_parto varchar(10),  inseminacion varchar(10), codPareja varchar(20), fecha_celo varchar(8))"
        );
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS "+BuildConfig.Tabla_Parto+" (id INTEGER PRIMARY KEY AUTOINCREMENT, codigo_ganado varchar(20), fecha_monta varchar(8), fecha_parto varchar(8), fecha_parto_real varchar(8),genero_cria char(1), estado_parto varchar(10), observacion varchar(100), num_partos INTEGER, num_servicios INTEGER)"
        );
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS "+BuildConfig.Tabla_fotos+" (id INTEGER PRIMARY KEY AUTOINCREMENT, codigo_ganado varchar(20), imagen varchar(200), fecha varchar(8), descripcion varchar(100))"
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void loadHomeFragment(){
        Runnable mPendingRunnable = () -> {
            Fragment fragment = new GalleryFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.nav_host_fragment_content_main, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        };

    }

    private Fragment getHomeFragment() {
        switch (navItemIndex){
            case 0:
                return new HomeFragment();
            case 1:
                return new GalleryFragment();
            case 2:
                return new SlideshowFragment();
            default:
                return new HomeFragment();
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                navItemIndex = 0;
                break;
            case R.id.nav_gallery:
                navItemIndex = 1;
                break;
            case R.id.nav_slideshow:
                navItemIndex = 2;
                break;

        }

        return true;

    }

}