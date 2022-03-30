package com.example.biotec;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.biotec.clases.DBHandler;
import com.example.biotec.clases.ElementoGanado;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class GanadoDetalle extends AppCompatActivity {
    private FloatingActionButton fab_datosReproductivos, fab_datosAdicionales, fab_datosInformativos;
    private CardView cv_reproductivo;
    private Button btn_eliminar;
    private SQLiteDatabase myDB;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganado_detalle);
        getSupportActionBar().setTitle("Detalles del Ganado");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        String codigo_ganado = getIntent().getStringExtra("codigo_ganado");

        myDB = openOrCreateDatabase(BuildConfig.Nombre_Base, MODE_PRIVATE, null);

        fab_datosReproductivos = findViewById(R.id.fab_datosReproductivos);
        fab_datosAdicionales = findViewById(R.id.fab_datosAdicionales);
        fab_datosInformativos = findViewById(R.id.fab_datosGenerales);
        btn_eliminar = findViewById(R.id.btn_eliminar);
        cv_reproductivo = findViewById(R.id.cv_reproductivos);

        SQLiteDatabase myDB = openOrCreateDatabase(BuildConfig.Nombre_Base, MODE_PRIVATE, null);
        ElementoGanado ganado = new DBHandler(myDB).obtenerGanado(codigo_ganado);

        if(ganado.getGenero().equals("M")) cv_reproductivo.setVisibility(View.GONE);
        fab_datosInformativos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GanadoDetalle.this, DatosInformativos.class);
                intent.putExtra("codigo_ganado",codigo_ganado);
                startActivity(intent);
            }
        });
        fab_datosReproductivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GanadoDetalle.this, DatosReproductivos.class);
                intent.putExtra("codigo_ganado",codigo_ganado);
                startActivity(intent);
            }
        });

        fab_datosAdicionales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(GanadoDetalle.this, DatosAdicionales.class);
                intent.putExtra("codigo_ganado",codigo_ganado);
                startActivity(intent);
            }
        });

        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(GanadoDetalle.this)
                        .setTitle("Advertencia")
                        .setMessage("Esta seguro de eliminar el registro")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new DBHandler(myDB).eliminarGanado(codigo_ganado);
                                finish();
                                Snackbar.make(v,"Ganado ELiminado",Snackbar.LENGTH_LONG)
                                        .setAction("Accion",null).show();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //nothing
                            }
                        })
                        .create().show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
