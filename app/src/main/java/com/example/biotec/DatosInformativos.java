package com.example.biotec;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.biotec.clases.DBHandler;
import com.example.biotec.clases.ElementoGanado;
import com.example.biotec.clases.FormatoFecha;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class DatosInformativos extends AppCompatActivity {
    public Button btn_cancelar, btn_Actualizar;
    public TextInputEditText tie_dicodigo, tie_difechaNacimiento, tie_diedad, tie_dipeso,tie_ditamanio,
            tie_dinumeroLote,tie_dimadre, tie_dipadre, tie_dirazaOtro,tie_dicodigoMAGAD, tie_dicolor;
    public TextInputLayout til_difechaNacimiento, til_diedad, til_dipeso,til_ditamanio,
            til_dinumeroLote,til_dimadre, til_dipadre, til_dirazaOtro, til_dicodigoMAGAD, til_dicolor;
    public Spinner sp_diespecie, sp_diraza, sp_diestado, sp_diproposito;
    public EditText et_didescripcion;
    public CheckBox cb_dimacho, cb_dihembra;
    private String codigo_ganado;
    private SQLiteDatabase myDB;
    private DBHandler handler;
    private int mYear, mMonth, mDay;

    public void onCreate(Bundle SaverInstance) {
        super.onCreate(SaverInstance);
        setContentView(R.layout.activity_datos_informativos);
        inicializarControles();

        getSupportActionBar().setTitle("Datos Informativos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        obtenerDatos();


        btn_Actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valida = true;
                String genero = "";
                String fechaNacimiento = tie_difechaNacimiento.getText().toString();
                String raza = sp_diraza.getSelectedItem().toString();

                if(fechaNacimiento.equals("")){
                    til_difechaNacimiento.setError("Agrege una fecha de nacimiento");
                    valida = false;
                }
                if(new FormatoFecha().obtenerEdad(fechaNacimiento) == null){
                    til_difechaNacimiento.setError("La fecha no debe ser mayor a la actual");
                    valida = false;
                }
                if(cb_dihembra.isChecked()){
                    genero = "H";
                }else if(cb_dimacho.isChecked()){
                    genero = "M";
                }
                if(!cb_dihembra.isChecked() && !cb_dimacho.isChecked()){
                    valida = false;
                    Snackbar.make(v,"Debe elegir el género",Snackbar.LENGTH_LONG)
                            .setAction("Accion",null).show();
                }
                if(raza.equals("Otro")){
                    if(tie_dirazaOtro.getText().toString().equals("")){
                        til_dirazaOtro.setError("Escriba una raza");
                        valida=false;
                    }else{
                        raza = tie_dirazaOtro.getText().toString().trim();
                    }

                }

                if(valida) {
                    ContentValues ganado = new ContentValues();

                    ganado.put("fecha_nacimiento", fechaNacimiento.replace("/", ""));
                    ganado.put("raza", raza);
                    ganado.put("estado",sp_diestado.getSelectedItem().toString());
                    ganado.put("genero",genero);
                    if(new FormatoFecha().obtenerEdad(fechaNacimiento) != null ) ganado.put("edad",new FormatoFecha().obtenerEdad(fechaNacimiento));
                    ganado.put("edad",tie_diedad.getText().toString());
                    ganado.put("peso",tie_dipeso.getText().toString());
                    ganado.put("estado",sp_diestado.getSelectedItem().toString());
                    ganado.put("proposito",sp_diproposito.getSelectedItem().toString());
                    ganado.put("lote",tie_dinumeroLote.getText().toString().trim());
                    ganado.put("cod_madre",tie_dimadre.getText().toString().trim());
                    ganado.put("cod_padre",tie_dipadre.getText().toString().trim());
                    ganado.put("descripcion",et_didescripcion.getText().toString());
                    ganado.put("color",tie_dicolor.getText().toString());
                    ganado.put("codigoMAGAD", tie_dicodigoMAGAD.getText().toString().trim());
                    if(actualizar(ganado) > 0) {
                        //Toast.makeText(getApplicationContext(),"Los datos han sido guardados exitosamente",Toast.LENGTH_SHORT);
                        new AlertDialog.Builder(DatosInformativos.this)
                                .setTitle("Confirmación de Actualizacóin")
                                .setMessage("Los datos han sido actualizados exitosamente")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .create()
                                .show();
                    }else{

                        Snackbar.make(v,"Ocurrió un error en el registro...",Snackbar.LENGTH_LONG)
                                .setAction("Accion",null).show();
                    }
                    myDB.close();
                }
            }
        });
        btn_cancelar.setOnClickListener(v -> finish());


    }

    private long actualizar(ContentValues row) {
        return handler.actualizarGanado(codigo_ganado,row);
    }

    public void inicializarControles(){
        btn_cancelar = findViewById(R.id.btn_cancelar);
        btn_Actualizar = findViewById(R.id.btnActualizar);

        tie_dicodigo = findViewById(R.id.tie_dicodigo);
        tie_difechaNacimiento = findViewById(R.id.tie_difechaNacimiento);
        sp_diespecie = findViewById(R.id.sp_diespecie);
        sp_diraza = findViewById(R.id.sp_diraza);
        sp_diestado = findViewById(R.id.sp_diestado);
        sp_diproposito = findViewById(R.id.sp_diproposito);
        tie_diedad = findViewById(R.id.tie_diedad);
        tie_dipeso = findViewById(R.id.tie_dipeso);
        //tie_ditamanio = findViewById(R.id.tie_ditamanio);
        tie_dinumeroLote = findViewById(R.id.tie_dinumeroLote);
        tie_dimadre = findViewById(R.id.tie_dimadre);
        tie_dipadre = findViewById(R.id.tie_dipadre);
        tie_dirazaOtro = findViewById(R.id.tie_dirazaOtro);
        tie_dicodigoMAGAD =findViewById(R.id.tie_dicodigoMAGAD);
        tie_dicolor = findViewById(R.id.tie_dicolor);

        til_dipeso = findViewById(R.id.til_dipeso);
        til_dinumeroLote = findViewById(R.id.til_dinumeroLote);
        til_dimadre = findViewById(R.id.til_dimadre);
        til_dipadre = findViewById(R.id.til_dipadre);
        til_dirazaOtro = findViewById(R.id.til_dirazaOtro);
        til_difechaNacimiento = findViewById(R.id.til_difechaNacimiento);
        til_dicodigoMAGAD = findViewById(R.id.til_dicodigoMAGAD);
        til_dicolor = findViewById(R.id.til_dicolor);

        et_didescripcion = findViewById(R.id.et_didescripcion);
        cb_dimacho = findViewById(R.id.cb_dimacho);
        cb_dihembra = findViewById(R.id.cb_dihembra);

        myDB = openOrCreateDatabase(BuildConfig.Nombre_Base, MODE_PRIVATE, null);
        handler = new DBHandler(myDB);
        codigo_ganado = getIntent().getStringExtra("codigo_ganado");

        tie_diedad.setInputType(InputType.TYPE_CLASS_NUMBER);
        tie_dipeso.setInputType(InputType.TYPE_CLASS_NUMBER);
        //tie_ditamanio.setInputType(InputType.TYPE_CLASS_NUMBER);
        tie_dinumeroLote.setInputType(InputType.TYPE_CLASS_NUMBER);

        tie_dicodigo.setEnabled(false);
        //tie_difechaNacimiento.setEnabled(false);
        sp_diespecie.setEnabled(false);
        //tie_dimadre.setEnabled(false);
        //tie_dipadre.setEnabled(false);
        cb_dihembra.setEnabled(false);
        cb_dimacho.setEnabled(false);
        sp_diestado.setEnabled(false);

        tie_difechaNacimiento.setFocusable(false);
        tie_difechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog (DatosInformativos.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "dd/MM/yyyy"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                        tie_difechaNacimiento.setText(sdf.format(myCalendar.getTime()));
                        if(new FormatoFecha().obtenerEdad(tie_difechaNacimiento.getText().toString()) == null){
                            til_difechaNacimiento.setError("La fecha no debe ser mayor a la actual");
                        }else {
                            tie_diedad.setText(String.valueOf(new FormatoFecha().obtenerEdad(tie_difechaNacimiento.getText().toString())));
                            til_difechaNacimiento.setError(null);
                        }
                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.show();


            }
        });

        sp_diraza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if( position == 6){
                    til_dirazaOtro.setVisibility(View.VISIBLE);
                }else
                {
                    til_dirazaOtro.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void obtenerDatos(){
        ElementoGanado ganado = handler.obtenerGanado(codigo_ganado);
        tie_dicodigo.setText(ganado.getCodigo());
        if(ganado.getGenero().equals("M")){
            cb_dimacho.setChecked(true);
        }else {
            cb_dihembra.setChecked(true);
        }
        tie_difechaNacimiento.setText(new FormatoFecha().formatearFecha(ganado.getFechaNacimiento()));
        tie_diedad.setText(ganado.getEdad());
        tie_dipeso.setText(String.valueOf(ganado.getPeso()));
        tie_dinumeroLote.setText(ganado.getLote());
        tie_dimadre.setText(ganado.getCodMadre());
        tie_dipadre.setText(ganado.getCodPadre());
        et_didescripcion.setText(ganado.getDescripcion());
        tie_dicodigoMAGAD.setText(ganado.getCodigoMAGAD());
        tie_dicolor.setText(ganado.getColor());

        for (int i = 0; i < sp_diestado.getAdapter().getCount(); i++) {
            if (sp_diestado.getAdapter().getItem(i).toString().contains(ganado.getEstado())) {
                sp_diestado.setSelection(i);
            }
        }
        for (int i = 0; i < sp_diproposito.getAdapter().getCount(); i++) {
            if (sp_diproposito.getAdapter().getItem(i).toString().contains(ganado.getProposito())) {
                sp_diproposito.setSelection(i);
            }
        }

        boolean razaOtro = true;
        for (int i = 0; i < sp_diraza.getAdapter().getCount(); i++) {
            if (sp_diraza.getAdapter().getItem(i).toString().contains(ganado.getRaza())) {
                sp_diraza.setSelection(i);
                til_dirazaOtro.setVisibility(View.GONE);
                razaOtro = false;
            }
        }

        if(razaOtro){
            sp_diraza.setSelection(6);
            til_dirazaOtro.setVisibility(View.VISIBLE);
            tie_dirazaOtro.setText(ganado.getRaza());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}


