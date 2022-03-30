package com.example.biotec;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biotec.adapter.AdpGaleria;
import com.example.biotec.adapter.AdpRegistroReproductivo;
import com.example.biotec.clases.DBHandler;
import com.example.biotec.clases.ElementoFoto;
import com.example.biotec.clases.ElementoGanado;
import com.example.biotec.clases.FormatoFecha;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Galeria extends AppCompatActivity {
    private LinearLayout ll_not_found;
    private TextView tv_codigo, tv_raza, tv_fecha;
    private RecyclerView rv_galeria;
    private ArrayList<ElementoFoto> fotos;
    private String codigo_ganado;
    private SQLiteDatabase myDB;
    String currentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String srcImage = "";
    private Button btn_add_foto;
    private ElementoGanado ganado;
    private DBHandler handler;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_galeria);

        getSupportActionBar().setTitle("Galeria");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        myDB = openOrCreateDatabase(BuildConfig.Nombre_Base, MODE_PRIVATE, null);
        handler = new DBHandler(myDB);

        ll_not_found = findViewById(R.id.ll_not_found);
        rv_galeria = findViewById(R.id.rv_galeria);
        btn_add_foto = findViewById(R.id.btn_add_image);
        tv_codigo = findViewById(R.id.tv_gcodigo);
        tv_raza = findViewById(R.id.tv_graza);
        tv_fecha = findViewById(R.id.tv_gfecha);
        btn_add_foto = findViewById(R.id.btn_add_image);

        fotos = new ArrayList<>();

        codigo_ganado = getIntent().getStringExtra("codigo_ganado");

        obtenerFotos();

        ganado = handler.obtenerGanado(codigo_ganado);

        tv_codigo.setText("Código: "+ganado.getCodigo());
        tv_raza.setText("Raza: "+ganado.getRaza());
        tv_fecha.setText(new FormatoFecha().formatearFecha(ganado.getFechaNacimiento()));

        rv_galeria.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        rv_galeria.setItemAnimator(new DefaultItemAnimator());
        rv_galeria.setAdapter(new AdpGaleria(fotos));


        btn_add_foto.setOnClickListener(new View.OnClickListener() {
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
                srcImage = photoURI.toString();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == -1) insertarFoto(srcImage);//iv_registrarGanado.setImageURI(Uri.parse(srcImage));
    }

    @Override
    protected void onResume() {
        super.onResume();
        obtenerFotos();
    }

    public void obtenerFotos(){
        fotos = handler.obtenerFotos(codigo_ganado);

        if(fotos.size() > 0) ll_not_found.setVisibility(View.GONE);

        rv_galeria.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        rv_galeria.setItemAnimator(new DefaultItemAnimator());
        rv_galeria.setAdapter(new AdpGaleria(fotos));
    }

    public void insertarFoto(String srcImage){
        ContentValues foto = new ContentValues();

        Calendar cal = Calendar.getInstance();
        String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String mes = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String year = String.valueOf(cal.get(Calendar.YEAR));

        if(mes.length() <2) mes ="0"+mes;
        foto.put("codigo_ganado",codigo_ganado);
        foto.put("imagen",srcImage);
        foto.put("fecha",day+mes+year);

        myDB.insert(BuildConfig.Tabla_fotos,null,foto);
    }


    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }


}
