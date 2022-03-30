package com.example.biotec;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.biotec.clases.DBHandler;
import com.example.biotec.clases.ElementoGanado;
import com.example.biotec.clases.FormatoFecha;
import com.example.biotec.clases.TablaDeDeaton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class DatosAdicionales extends AppCompatActivity {
    private TextInputEditText tie_fechaNacimiento, tie_fechaDestete;
    private String codigo_ganado;
    private SQLiteDatabase myDB;
    private int constante = 180;
    private Button btn_ficha;
    private ImageView iv_ficha;
    private String srcImg = "";
    private DBHandler handler;
    String currentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    ElementoGanado ganado;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_adicionales);

        getSupportActionBar().setTitle("Datos Adicionales");

        tie_fechaDestete = findViewById(R.id.tie_fechaDestete);
        tie_fechaNacimiento = findViewById(R.id.tie_fechaNacimiento);
        btn_ficha = findViewById(R.id.btn_ficha);
        iv_ficha = findViewById(R.id.iv_ficha);


        codigo_ganado = getIntent().getStringExtra("codigo_ganado");
        myDB = openOrCreateDatabase(BuildConfig.Nombre_Base, MODE_PRIVATE, null);
        handler = new DBHandler(myDB);

        tie_fechaNacimiento.setFocusable(false);
        tie_fechaDestete.setFocusable(false);

        consultarDatos();

        if(srcImg != null) iv_ficha.setImageURI(Uri.parse(srcImg));

        btn_ficha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = codigo_ganado+"_" + timeStamp + "_";
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
                Toast.makeText(getApplicationContext(),"No se pudo cargar la imagen",Toast.LENGTH_SHORT);

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.biotec.biotec.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                srcImg = photoURI.toString();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == -1) agregarFicha(srcImg);// iv_ficha.setImageURI(Uri.parse(srcImg));
    }

    public void agregarFicha(String srcImg){
        ContentValues row = new ContentValues();
                row.put("ficha",srcImg);
        if(handler.actualizarGanado(codigo_ganado, row) >0 ) {
            iv_ficha.setImageURI(Uri.parse(srcImg));
        }else{
        }
    }

    public void consultarDatos(){

        ganado = handler.obtenerGanado(codigo_ganado);
 /*
        Cursor myCursor =
                myDB.rawQuery("select * from " + BuildConfig.Nombre_Tabla, null);
        while (myCursor.moveToNext()){
            if(codigo_ganado.equals(myCursor.getString(0))){*/
                String fecha = ganado.getFechaNacimiento();
                int dia = Integer.valueOf(fecha.substring(0,2));
                int mes = Integer.valueOf(fecha.substring(2,4));
                int anio = Integer.valueOf(fecha.substring(4,8));

                String fechaFormato = new FormatoFecha().formatearFecha(fecha);


                tie_fechaNacimiento.setText(fechaFormato);
                srcImg = ganado.getFicha();

                TablaDeDeaton deaton = new TablaDeDeaton();

                calcularDestete(deaton.getNumIzq()[dia-1][mes-1],deaton,anio,mes);
 //           }
   //     }

    }

    public void calcularDestete(int numeroIzq, TablaDeDeaton deaton, int year, int month){
        int resultado = numeroIzq + constante;
        int [][] coordenadas = deaton.numIzq(resultado);
        int dia = coordenadas[0][0];
        int mes = coordenadas[0][1];

        if(mes <= month ){
            year += 1;
        }

        ZoneId defaultZoneId = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            defaultZoneId = ZoneId.systemDefault();
            LocalDate localDate = LocalDate.of(year, mes + 1, dia + 1);
            Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            tie_fechaDestete.setText(sdf.format(date));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
