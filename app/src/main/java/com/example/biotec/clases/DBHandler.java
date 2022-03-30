package com.example.biotec.clases;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.ajts.androidmads.library.ExcelToSQLite;
import com.example.biotec.BuildConfig;
import com.example.biotec.RegistrarGanado;
import com.example.biotec.Settings;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openDatabase;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import com.ajts.androidmads.library.SQLiteToExcel;


public class DBHandler {
    private SQLiteDatabase myDB;

    private static final int DB_VERSION = 1;
    public static String DB_FILEPATH = "/data/com.biotec.biotec/databases/"+BuildConfig.Nombre_Base;
    public static String BACKUP_PATH = "/Download/"+BuildConfig.Nombre_Base;
    public static String EXCEL_PATH = "";



    public DBHandler(SQLiteDatabase myDb) {
        this.myDB = myDb;
    }


    public void abrirBase(){
        myDB = openOrCreateDatabase(BuildConfig.Nombre_Base, null, null);;

    }

    public void borrarGanado(String codigo){

    }

    public long actualizarGanado(String codigo, ContentValues row){

        return myDB.update(BuildConfig.Nombre_Tabla,row,"codigo=?", new String[]{codigo} );

    }

    public ElementoGanado obtenerGanado(String codigo){
        ElementoGanado ganado = new ElementoGanado();
        Cursor cursor =
                myDB.rawQuery("select * from " + BuildConfig.Nombre_Tabla + " where codigo = '" + codigo + "'", null);
        while (cursor.moveToNext()){
            ganado.setCodigo(cursor.getString(0));
            ganado.setEspecie(cursor.getString(1));
            ganado.setImagen(cursor.getString(2));
            ganado.setRaza(cursor.getString(3));
            ganado.setFechaNacimiento(cursor.getString(4));
            ganado.setGenero(cursor.getString(5));
            ganado.setEdad(cursor.getString(6));
            ganado.setPeso(cursor.getDouble(7));
            ganado.setTamanio(cursor.getDouble(8));
            ganado.setEstado(cursor.getString(9));
            ganado.setProposito(cursor.getString(10));
            ganado.setLote(cursor.getString(11));
            ganado.setCodMadre(cursor.getString(12));
            ganado.setCodPadre(cursor.getString(13));
            ganado.setDescripcion(cursor.getString(14));
            ganado.setColor(cursor.getString(15));
            ganado.setFicha(cursor.getString(16));
            ganado.setCodigoMAGAD(cursor.getString(17));
        }
        cursor.close();
        return ganado;
    }

    public ArrayList<ElementoGanado> obtenerHato(){
        ArrayList<ElementoGanado> hato = new ArrayList<>();
        Cursor cursor =
                myDB.rawQuery("select * from " + BuildConfig.Nombre_Tabla, null);

        while(cursor.moveToNext()){
            ElementoGanado ganado = new ElementoGanado();
            ganado.setCodigo(cursor.getString(0));
            ganado.setEspecie(cursor.getString(1));
            ganado.setImagen(cursor.getString(2));
            ganado.setRaza(cursor.getString(3));
            ganado.setFechaNacimiento(cursor.getString(4));
            ganado.setGenero(cursor.getString(5));
            ganado.setEdad(cursor.getString(6));
            ganado.setPeso(cursor.getDouble(7));
            ganado.setTamanio(cursor.getDouble(8));
            ganado.setEstado(cursor.getString(9));
            ganado.setProposito(cursor.getString(10));
            ganado.setLote(cursor.getString(11));
            ganado.setCodMadre(cursor.getString(12));
            ganado.setCodPadre(cursor.getString(13));
            ganado.setDescripcion(cursor.getString(14));
            ganado.setDatosReproductivos(obtenerResgitroRep(ganado.getCodigo()));

            hato.add(ganado);
        }

        return hato;
    }

    public ArrayList<ElementoRegistroReproductivo> obtenerResgitroRep(String codigo_ganado){
        ArrayList<ElementoRegistroReproductivo> er = new ArrayList<>();
        Cursor myCursor =
                myDB.rawQuery("select * from " + BuildConfig.Tabla_Reproductivos + " where codigo_ganado = '" + codigo_ganado + "' order by id desc", null);

        while (myCursor.moveToNext()) {
            ElementoRegistroReproductivo registro = new ElementoRegistroReproductivo();
            registro.setCodigo_ganado(myCursor.getString(1));
            registro.setFecha_monta(myCursor.getString(2));
            registro.setFecha(myCursor.getString(3));
            registro.setFecha_parto_real(myCursor.getString(4));
            registro.setObservacion(myCursor.getString(5));
            registro.setNum_servicios(myCursor.getInt(6));
            registro.setNum_partos(myCursor.getInt(7));
            registro.setGenero_cria(myCursor.getString(8));
            registro.setCodigo_cria(myCursor.getString(9));
            registro.setEstado(myCursor.getString(10));

            er.add(registro);
        }
        return er;
    }

    public ArrayList<ElementoFoto> obtenerFotos(String codigo){
        ArrayList<ElementoFoto> fotos = new ArrayList<>();

        Cursor cursor =
                myDB.rawQuery("select * from " + BuildConfig.Tabla_fotos + " where codigo_ganado = '" +codigo+"'", null);

        while (cursor.moveToNext()){
            ElementoFoto foto = new ElementoFoto();
            foto.setCodigo_ganado(cursor.getString(1));
            foto.setImagen(cursor.getString(2));
            foto.setFecha(cursor.getString(3));
            foto.setDescripcion(cursor.getString(4));

            fotos.add(foto);
        }
        return fotos;
    }

    public void exportDB2(Context context){ //,String databaseName, String databaseExt, boolean sameNameDatabase) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        // get current date time with Date()
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        // get current date time with Calendar()
        Calendar cal = Calendar.getInstance();
        System.out.println(dateFormat.format(cal.getTime()));
        /*File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getExternalStorageDirectory();// Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;*/

        exportDBtoExcel(context);
        new AlertDialog.Builder(context)
                .setTitle("Confirmación")
                .setMessage("Los datos han sido exportados exitosamente en " + "/Download/Control-Vet7B.xls")
                .setPositiveButton("Compartir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File backupDB = new File(EXCEL_PATH);

                        MediaScannerConnection.scanFile(context, new String[]{backupDB.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                            }
                        });
                        Uri photoURI = FileProvider.getUriForFile(context,
                                "com.biotec.biotec.fileprovider",
                                backupDB);

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("*/*");
                        intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                        context.startActivity(Intent.createChooser(intent, "Compartir"));
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();

    }

    public void importDB (Context context){
        ExcelToSQLite excelToSQLite = new ExcelToSQLite(context, BuildConfig.Nombre_Base,true);
        String path = Environment.getExternalStorageDirectory() + "/Download/Control-Vet7B.xls";
        excelToSQLite.importFromFile(path, new ExcelToSQLite.ImportListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted(String dbName) {
                Toast.makeText(
                        context,
                        " completed! \n", Toast.LENGTH_LONG)
                        .show();

            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(
                        context,
                        "Archivo exportado\n", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }
    public void exportDBtoExcel(Context context){
        SQLiteToExcel sqliteToExcel = new SQLiteToExcel(context, BuildConfig.Nombre_Base,Environment.getExternalStorageDirectory() + "/Download/");

        sqliteToExcel.exportAllTables("Control-Vet7B.xls", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {

            }
            @Override
            public void onCompleted(String filePath) {
               EXCEL_PATH = filePath;
            }
            @Override
            public void onError(Exception e) {

            }
        });

    }
    public void eliminarGanado(String codigo){

        myDB.delete(BuildConfig.Nombre_Tabla,"codigo=?",new String[]{codigo} );
        myDB.delete(BuildConfig.Tabla_Parto,"codigo_ganado=?",new String[]{codigo} );
        myDB.delete(BuildConfig.Tabla_fotos,"codigo_ganado=?",new String[]{codigo} );
        myDB.delete(BuildConfig.Tabla_Reproductivos,"codigo_ganado=?",new String[]{codigo} );
        myDB.close();
    }


    public void exportarCSV(SQLiteDatabase db, Context context) {
        File carpeta = new File(Environment.getExternalStorageDirectory() + "/Download/ExportarSQLiteCSV");
        String archivoAgenda = carpeta.toString() + "/" + "Control-Vet7B.csv";

        boolean isCreate = false;
        if(!carpeta.exists()) {
            isCreate = carpeta.mkdir();
        }

        try {
            FileWriter fileWriter = new FileWriter(archivoAgenda);

            Cursor fila = db.rawQuery("select * from " + BuildConfig.Nombre_Tabla, null);

            if(fila != null && fila.getCount() != 0) {
                fila.moveToFirst();
                do {

                    fileWriter.append(fila.getString(0));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(1));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(2));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(3));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(4));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(5));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(6));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(7));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(8));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(9));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(10));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(11));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(12));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(13));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(14));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(15));
                    fileWriter.append(",");
                    fileWriter.append(fila.getString(16));
                    fileWriter.append("\n");

                } while(fila.moveToNext());
            } else {
                Toast.makeText(context, "No hay registros.", Toast.LENGTH_LONG).show();
            }

            db.close();
            fileWriter.close();
            Toast.makeText(context, "SE CREO EL ARCHIVO CSV EXITOSAMENTE", Toast.LENGTH_LONG).show();

        } catch (Exception e) { }
    }

    public void compartir(@NonNull Context context){
        File file =new File(DB_FILEPATH);//context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"_20220305_015649_450469139066867347.jpg");
        FileOutputStream out = null;
        //Bitmap bmp = Bitmap.createBitmap(tv.getWidth(), tv.getHeight(), Bitmap.Config.ARGB_8888); //Código para crear un recibo en imagen

        try {
            out = new FileOutputStream(file);
        //bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
        out.close();
        MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
                Log.i("ExternalStorage", "Scanned " + path + ":");
                Log.i("ExternalStorage", "-> uri=" + uri);
            }
        });
        Uri photoURI = FileProvider.getUriForFile(context,
                "com.biotec.biotec.fileprovider",
                file);

        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_STREAM, photoURI);
        context.startActivity(Intent.createChooser(intent, "Compartir"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
}
