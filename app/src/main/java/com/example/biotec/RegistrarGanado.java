package com.example.biotec;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.biotec.clases.FormatoFecha;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import static android.content.ContentValues.TAG;

public class RegistrarGanado extends AppCompatActivity {
    private ImageView iv_registrarGanado;
    private Button btnRegistrar, btnCancel;
    String currentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private TextInputLayout til_codigo,til_fechaNacimiento,til_edad,til_peso
            ,til_tamanio,til_numeroLote, til_madre, til_padre, til_razaOtro, til_codigoMAGAD, til_color;
    private TextInputEditText tie_codigo, tie_fechaNacimiento, tie_edad, tie_peso, tie_tamanio, tie_numeroLote,
            tie_madre, tie_padre, tie_razaOtro, tie_codigoMAGAD, tie_color;
    private CheckBox cb_macho, cb_hembra;
    private EditText et_descripcion;
    private Spinner sp_specie,sp_raza,sp_estado, sp_proposito, sp_color;
    private int mYear, mMonth, mDay;
    SQLiteDatabase myDB;
    private String srcImage = "";

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_registrar_ganado);

        getSupportActionBar().setTitle("Registrar Ganado");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        iv_registrarGanado = findViewById(R.id.iv_registrarGanado);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnCancel = findViewById(R.id.btncancel);

        inicializarControles();

        iv_registrarGanado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirBase();
                boolean valida = true;
                String genero = "";
                String fechaNacimiento = tie_fechaNacimiento.getText().toString();
                String raza = sp_raza.getSelectedItem().toString();

                if(tie_codigo.getText().toString().equals("")){
                    til_codigo.setError("Agrege un código");
                    valida =false;
                }if(tie_codigoMAGAD.getText().toString().equals("")){
                    til_codigoMAGAD.setError("Agrege un código Magad");
                    valida =false;
                }
                if(fechaNacimiento.equals("")){
                    til_fechaNacimiento.setError("Agrege una fecha de nacimiento");
                    valida = false;
                }
                if(new FormatoFecha().obtenerEdad(fechaNacimiento) == null){
                    til_fechaNacimiento.setError("La fecha no debe ser mayor a la actual");
                    valida = false;
                }
                if(esRepetido()){
                    til_codigo.setError("El código ya existe");
                    valida = false;
                }
                if(cb_hembra.isChecked()){
                    genero = "H";
                }else if(cb_macho.isChecked()){
                    genero = "M";
                }
                if(!cb_hembra.isChecked() && !cb_macho.isChecked()){
                    valida = false;
                    Snackbar.make(v,"Debe elegir el género",Snackbar.LENGTH_LONG)
                            .setAction("Accion",null).show();
                }
                if(raza.equals("Otro")){
                    if(tie_razaOtro.getText().toString().equals("")){
                        til_razaOtro.setError("Escriba una raza");
                        valida=false;
                    }else{
                        raza = tie_razaOtro.getText().toString().trim();
                    }

                }


                if(valida) {
                    ContentValues ganado = new ContentValues();

                    ganado.put("codigo", tie_codigo.getText().toString().trim());
                    ganado.put("codigoMAGAD", tie_codigoMAGAD.getText().toString().trim());
                    ganado.put("especie",sp_specie.getSelectedItem().toString());
                    ganado.put("raza", raza);
                    ganado.put("genero",genero);
                    ganado.put("fecha_nacimiento", fechaNacimiento.replace("/", ""));
                    ganado.put("edad",tie_edad.getText().toString());
                    ganado.put("peso",tie_peso.getText().toString());
                    ganado.put("estado",sp_estado.getSelectedItem().toString());
                    ganado.put("proposito",sp_proposito.getSelectedItem().toString());
                    ganado.put("lote",tie_numeroLote.getText().toString().trim());
                    ganado.put("cod_madre",tie_madre.getText().toString().trim());
                    ganado.put("cod_padre",tie_padre.getText().toString().trim());
                    ganado.put("descripcion",et_descripcion.getText().toString());
                    ganado.put("color",tie_color.getText().toString());
                    if(!srcImage.equals("")){
                        ganado.put("imagen",srcImage);

                        ContentValues foto = new ContentValues();
                        Calendar cal = Calendar.getInstance();
                        String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
                        String mes = String.valueOf(cal.get(Calendar.MONTH) + 1);
                        String year = String.valueOf(cal.get(Calendar.YEAR));
                        foto.put("foto",srcImage);
                        foto.put("codigo_ganado",tie_codigo.getText().toString());
                        foto.put("fecha",day+mes+year);
                        myDB.insert(BuildConfig.Tabla_fotos, null, foto);
                    }
                    if(new FormatoFecha().obtenerEdad(fechaNacimiento) != null ) ganado.put("edad",new FormatoFecha().obtenerEdad(fechaNacimiento));

                    if(insertarBase(myDB, ganado)) {
                        //Toast.makeText(getApplicationContext(),"Los datos han sido guardados exitosamente",Toast.LENGTH_SHORT);
                        new AlertDialog.Builder(RegistrarGanado.this)
                                .setTitle("Confirmación de Registro")
                                .setMessage("Los datos han sido guardados exitosamente")
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

                }

                myDB.close();
            }
        });

        btnCancel.setOnClickListener(v -> finish());
    }

    private void inicializarControles(){
        til_codigo = findViewById(R.id.til_codigo);
        til_fechaNacimiento = findViewById(R.id.til_fechaNacimiento);
        til_edad = findViewById(R.id.til_edad);
        til_peso = findViewById(R.id.til_peso);
        //til_tamanio = findViewById(R.id.til_tamanio);
        til_numeroLote = findViewById(R.id.til_numeroLote);
        til_madre = findViewById(R.id.til_madre);
        til_padre = findViewById(R.id.til_padre);
        til_razaOtro = findViewById(R.id.til_razaOtro);
        til_codigoMAGAD = findViewById(R.id.til_codigoMAGAD);
        til_color = findViewById(R.id.til_color);

        tie_codigo = findViewById(R.id.tie_codigo);
        tie_fechaNacimiento = findViewById(R.id.tie_fechaNacimiento);
        tie_edad = findViewById(R.id.tie_edad);
        tie_peso = findViewById(R.id.tie_peso);
        //tie_tamanio = findViewById(R.id.tie_tamanio);
        tie_numeroLote = findViewById(R.id.tie_numeroLote);
        tie_madre = findViewById(R.id.tie_madre);
        tie_padre = findViewById(R.id.tie_padre);
        tie_razaOtro = findViewById(R.id.tie_razaOtro);
        tie_codigoMAGAD  = findViewById(R.id.tie_codigoMAGAD);
        tie_color = findViewById(R.id.tie_color);


        cb_hembra = findViewById(R.id.cb_hembra);
        cb_macho = findViewById(R.id.cb_macho);

        et_descripcion = findViewById(R.id.et_descripcion);

        sp_specie = findViewById(R.id.sp_especie);
        sp_raza = findViewById(R.id.sp_raza);
        sp_estado = findViewById(R.id.sp_estado);
        sp_proposito = findViewById(R.id.sp_proposito);
        //sp_color = findViewById(R.id.sp_color);



        tie_peso.setInputType(InputType.TYPE_CLASS_NUMBER);
        //tie_tamanio.setInputType(InputType.TYPE_CLASS_NUMBER);
        tie_numeroLote.setInputType(InputType.TYPE_CLASS_NUMBER);

        cb_hembra.setChecked(true);
        til_razaOtro.setVisibility(View.GONE);


        tie_edad.setEnabled(false);
        tie_fechaNacimiento.setFocusable(false);
        tie_fechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog (RegistrarGanado.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "dd/MM/yyyy"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                        tie_fechaNacimiento.setText(sdf.format(myCalendar.getTime()));
                        if(new FormatoFecha().obtenerEdad(tie_fechaNacimiento.getText().toString()) == null){
                            til_fechaNacimiento.setError("La fecha no debe ser mayor a la actual");
                        }else {
                            tie_edad.setText(String.valueOf(new FormatoFecha().obtenerEdad(tie_fechaNacimiento.getText().toString())));
                            til_fechaNacimiento.setError(null);
                        }
                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.show();


            }
        });

        cb_hembra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if (cb_macho.isChecked()) {
                            cb_macho.setChecked(false);
                    }
                }
            }
        });

        cb_macho.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if (cb_hembra.isChecked()) {
                        cb_hembra.setChecked(false);
                    }
                    sp_estado.setEnabled(false);
                }else {
                    sp_estado.setEnabled(true);
                }
            }
        });

        sp_raza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if( position == 6){
                    til_razaOtro.setVisibility(View.VISIBLE);
                }else
                {
                    til_razaOtro.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void abrirBase(){
        myDB = openOrCreateDatabase(BuildConfig.Nombre_Base, MODE_PRIVATE, null);
        /*myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS ganado (name VARCHAR(200), age INT, is_single INT)"
        );*/
    }



    private boolean insertarBase(SQLiteDatabase myDB, ContentValues row){

        long result = myDB.insert(BuildConfig.Nombre_Tabla, null, row);
        return (result != -1);

    }

    private boolean esRepetido(){
        Cursor myCursor = myDB.rawQuery("select * from "+BuildConfig.Nombre_Tabla, null);

        boolean repetido = false;
        while(myCursor.moveToNext()) {
            if(tie_codigo.getText().toString().equals(myCursor.getString(0))){
                repetido = true;
            }
        }
        myCursor.close();
        return repetido;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = tie_codigo.getText().toString()+"_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(RegistrarGanado.this,"No se pudo cargar la imagen",Toast.LENGTH_SHORT);

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.biotec.biotec.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                srcImage = photoURI.toString();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == -1) iv_registrarGanado.setImageURI(Uri.parse(srcImage));
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }


}
