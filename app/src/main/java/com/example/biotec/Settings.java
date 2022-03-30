package com.example.biotec;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.biotec.clases.DBHandler;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.sql.SQLData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.FileProvider;

public class Settings extends AppCompatActivity {
    private static final int ACTIVITY_CHOOSE_FILE = 0;
    private SwitchCompat btnSwitch;
    private MaterialButton btn_export;
    private MaterialButton btn_import;
    private SQLiteDatabase myDB;
    private DBHandler handler;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_settings);

        btnSwitch = findViewById(R.id.btn_switch);
        btn_export = findViewById(R.id.btn_export);
        btn_import = findViewById(R.id.btn_import);
        btnSwitch.setVisibility(View.GONE);
        getSupportActionBar().setTitle("Ajustes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        myDB = openOrCreateDatabase(BuildConfig.Nombre_Base, MODE_PRIVATE, null);
        handler = new DBHandler(myDB);

        btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
            }
        });

        btn_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handler.exportDB2(Settings.this);
                //handler.exportarCSV(myDB,Settings.this);
                //handler.exportDBtoExcel(Settings.this);
            }
        });

        btn_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handler.importDB(Settings.this);
               /* Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory() + "/Download/");

                Intent chooseFile;
                Intent intent;
                chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
                //chooseFile.setType("/");
                chooseFile.setDataAndType(selectedUri, "");
                intent = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);*/
                //handler.importDB(Settings.this);
            }
        });
    }

    /*
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        String path = "";
        if (requestCode == ACTIVITY_CHOOSE_FILE) {
            Uri uri = data.getData();
            String FilePath = uri.getPath(); // should the path be here in this string

            Toast.makeText(
                    Settings.this,
                    "DB " + FilePath + " imported! \n", Toast.LENGTH_LONG)
                    .show();

            //handler.importDB(Settings.this,uri.getPath());
        }
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
