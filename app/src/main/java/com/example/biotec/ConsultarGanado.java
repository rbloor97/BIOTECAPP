package com.example.biotec;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.biotec.adapter.AdpConsultarGanado;
import com.example.biotec.clases.ElementoGanado;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ConsultarGanado extends AppCompatActivity {
    private LinearLayout ll_busqueda;
    private Spinner sp_busqueda;
    private TextInputEditText tie_buscar;
    private TextInputLayout til_buscar;
    private ArrayList<ElementoGanado> listaGanado;
    private RecyclerView rv_ganado;
    private AdpConsultarGanado mAdapter;
    private TextView tv_titulo;
    private String param;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_consultar_ganado);

        param = getIntent().getStringExtra("param");
        tv_titulo = findViewById(R.id.tv_titulo);
        if(param.equals("G")) {
            getSupportActionBar().setTitle("Galeria");
            tv_titulo.setText("Control de Ganado por Fotos");
        }else{
            getSupportActionBar().setTitle("Consultar Ganado");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        ll_busqueda = findViewById(R.id.ll_busqueda);
        sp_busqueda = findViewById(R.id.sp_busqueda);
        tie_buscar = findViewById(R.id.tie_buscar);
        til_buscar = findViewById(R.id.til_buscar);
        rv_ganado = findViewById(R.id.rv_ganado);

        listaGanado = new ArrayList<>();
        consultar();

        sp_busqueda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAdapter = new AdpConsultarGanado(listaGanado,listaGanado,ConsultarGanado.this, sp_busqueda.getItemAtPosition(position).toString(),param);

                rv_ganado.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                rv_ganado.setItemAnimator(new DefaultItemAnimator());
                rv_ganado.setAdapter(mAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        consultar();

    }

    public void consultar(){
        listaGanado.clear();

        SQLiteDatabase myDB = openOrCreateDatabase(BuildConfig.Nombre_Base, MODE_PRIVATE, null);

        Cursor myCursor =
                myDB.rawQuery("select * from " + BuildConfig.Nombre_Tabla, null);

        while(myCursor.moveToNext()) {
            String codigo = myCursor.getString(0);
            String nombre = myCursor.getString(1);
            String imagen = myCursor.getString(2);
            String raza = myCursor.getString(3);
            String estado = myCursor.getString(9);
            String genero = myCursor.getString(5);

            ElementoGanado eg = new ElementoGanado(codigo,nombre,imagen,raza);
            eg.setEstado(estado);
            eg.setGenero(genero);
            listaGanado.add(eg);
        }

        myCursor.close();
        myDB.close();

        mAdapter = new AdpConsultarGanado(listaGanado,listaGanado,ConsultarGanado.this, sp_busqueda.getSelectedItem().toString(),param);

        rv_ganado.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        rv_ganado.setItemAnimator(new DefaultItemAnimator());
        rv_ganado.setAdapter(mAdapter);



        tie_buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAdapter.getFilter().filter(tie_buscar.getText().toString());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
