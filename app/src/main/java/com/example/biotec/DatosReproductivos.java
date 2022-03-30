package com.example.biotec;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biotec.adapter.AdpRegistroReproductivo;
import com.example.biotec.clases.DBHandler;
import com.example.biotec.clases.ElementoGanado;
import com.example.biotec.clases.ElementoRegistroReproductivo;
import com.example.biotec.clases.FormatoFecha;
import com.example.biotec.clases.TablaDeDeaton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DatosReproductivos extends AppCompatActivity {
    private TextInputEditText tie_fechaMonta, tie_fechaPartoReal, tie_fechaPartoEstim, tie_servicios, tie_codPareja, tie_fecCelo;
    private TextInputLayout til_fechaMonta, til_fechaPartoReal, til_fechaPartoEstim, til_servicios, til_codPareja, til_fecCelo;
    private EditText et_observacion;
    private int mYear, mMonth, mDay;
    private Button btnCancelar, btnGuardar, btnNuevoParto;
    private int diasGestacion = 284;
    private String codigo_ganado;
    private int num_parto = 0;
    private RecyclerView rv_registroRep;
    private TextView tv_primerParto, tv_diasAbiertos, tv_tasaConcep, tv_intervalo, tv_numPartos, tv_ultServicio, tv_tasaParto, tv_servicios, tv_fecSecado;
    private CheckBox cb_macho, cb_hembra;
    private Spinner sp_estado_parto, sp_inseminacion;
    private SQLiteDatabase myDB;
    private DBHandler handler;
    private ElementoGanado ganado ;
    private ElementoRegistroReproductivo regActual;
    private ArrayList<ElementoRegistroReproductivo> listParto;



    public void onCreate(Bundle SavedInstance) {
        super.onCreate(SavedInstance);
        setContentView(R.layout.activity_datos_reproductivos);

        getSupportActionBar().setTitle("Datos Reproductivos");

        tie_fechaMonta = findViewById(R.id.tie_fechaMonta);
        til_fechaMonta = findViewById(R.id.til_fechaMonta);
        tie_fechaPartoReal = findViewById(R.id.tie_fechaPartoReal);
        til_fechaPartoReal = findViewById(R.id.til_fechaPartoReal);
        tie_fechaPartoEstim = findViewById(R.id.tie_fechaPartoEstim);
        til_fechaPartoEstim = findViewById(R.id.til_fechaPartoEstim);
        tie_servicios = findViewById(R.id.tie_servicios);
        til_servicios = findViewById(R.id.til_servicios);
        tie_codPareja = findViewById(R.id.tie_codPareja);
        til_codPareja = findViewById(R.id.til_codPareja);
        tie_fecCelo = findViewById(R.id.tie_fecCelo);
        til_fecCelo = findViewById(R.id.til_fecCelo);

        et_observacion = findViewById(R.id.et_observacion);

        tv_primerParto = findViewById(R.id.tv_primerParto);
        tv_diasAbiertos = findViewById(R.id.tv_diasAbiertos);
        tv_tasaConcep = findViewById(R.id.tv_tasaConcep);
        tv_intervalo = findViewById(R.id.tv_intervalo);
        tv_numPartos = findViewById(R.id.tv_numPartos);
        tv_ultServicio = findViewById(R.id.tv_ultServicio);
        tv_tasaParto = findViewById(R.id.tv_tasaParto);
        tv_servicios = findViewById(R.id.tv_servicios);
        tv_fecSecado = findViewById(R.id.tv_fecSecado);

        cb_macho = findViewById(R.id.cb_machoCria);
        cb_hembra = findViewById(R.id.cb_hembraCria);

        sp_estado_parto = findViewById(R.id.sp_estado_parto);
        sp_inseminacion = findViewById(R.id.sp_inseminacion);

        rv_registroRep = findViewById(R.id.rv_registroRep);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnNuevoParto = findViewById(R.id.btnNuevoParto);

        codigo_ganado = getIntent().getStringExtra("codigo_ganado");
        myDB = openOrCreateDatabase(BuildConfig.Nombre_Base, MODE_PRIVATE, null);
        handler = new DBHandler(myDB);
        ganado = handler.obtenerGanado(codigo_ganado);
        regActual = new ElementoRegistroReproductivo();

        listParto= new ArrayList<>();

        tie_servicios.setInputType(InputType.TYPE_CLASS_NUMBER);
        tie_servicios.setText("1");

        et_observacion.setText("");


        tie_fecCelo.setFocusable(false);
        tie_fecCelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(DatosReproductivos.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "dd/MM/yyyy"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                        tie_fecCelo.setText(sdf.format(myCalendar.getTime()));

                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                        til_fecCelo.setError(null);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.show();

            }
        });

        tie_fechaMonta.setFocusable(false);
        tie_fechaMonta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(DatosReproductivos.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "dd/MM/yyyy"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                        tie_fechaMonta.setText(sdf.format(myCalendar.getTime()));

                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                        til_fechaMonta.setError(null);

                        TablaDeDeaton deaton = new TablaDeDeaton();
                        calcularParto(deaton.numeroDeDeaton(mDay-1,mMonth), deaton, mYear, mMonth+1);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.show();

            }
        });


        tie_fechaPartoEstim.setFocusable(false);

        tie_fechaPartoReal.setFocusable(false);
        tie_fechaPartoReal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(DatosReproductivos.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "dd/MM/yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                        tie_fechaPartoReal.setText(sdf.format(myCalendar.getTime()));
                        til_fechaPartoReal.setError(null);

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
                }
            }
        });

        consultarDatos();
        consultarParto();
        calcularIndiceRep();

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valida = true;
                String fechaPartoReal = "";

                if(tie_codPareja.getText().toString().equals("")){
                    valida = false;
                    til_codPareja.setError("Ingrese el código de pareja");
                }
                if(tie_fechaMonta.getText().toString().equals("")){
                    valida = false;
                    til_fechaMonta.setError("Ingrese la fecha de monta");
                }
                if(tie_fecCelo.getText().toString().equals("")){
                    valida = false;
                    til_fecCelo.setError("Ingrese la fecha de celo");
                }
                if(tie_servicios.getText().toString().equals("")){
                    valida = false;
                    til_servicios.setError("Ingrese el número de servicio");
                }
                if(!tie_fechaPartoReal.getText().toString().equals("")){
                    fechaPartoReal = tie_fechaPartoReal.getText().toString().replace("/", "");
                }
                if(!sp_estado_parto.getSelectedItem().toString().equals("Concepcion")){
                    valida = false;
                    Snackbar.make(v,"Debe cambiar el estado a Concepcion",Snackbar.LENGTH_LONG)
                            .setAction("Accion",null).show();
                }
                if(cb_hembra.isChecked() || cb_macho.isChecked()){
                    valida = false;
                    Snackbar.make(v,"Aún no debe seleccionar la cria",Snackbar.LENGTH_LONG)
                            .setAction("Accion",null).show();
                }
                if(tie_servicios.getText().toString().equals("0")){
                    valida = false;
                    Snackbar.make(v,"El servicio debe ser mayos a 0",Snackbar.LENGTH_LONG)
                            .setAction("Accion",null).show();
                }

                if(valida) {
                    ContentValues row = new ContentValues();
                    row.put("codigo_ganado", codigo_ganado);
                    row.put("fecha_monta", tie_fechaMonta.getText().toString().replace("/", ""));
                    row.put("fecha_parto", tie_fechaPartoEstim.getText().toString().replace("/", ""));
                    row.put("fecha_parto_real", fechaPartoReal);
                    row.put("observacion",et_observacion.getText().toString());
                    row.put("num_servicios",Integer.valueOf(tie_servicios.getText().toString()));
                    row.put("num_partos",num_parto);
                    row.put("estado_parto",sp_estado_parto.getSelectedItem().toString());
                    row.put("codPareja",tie_codPareja.getText().toString());
                    row.put("fecha_celo",tie_fecCelo.getText().toString());
                    row.put("inseminacion", sp_inseminacion.getSelectedItem().toString());

                    if(guardarDatos(row,"Preñada") > 0) {

                        ContentValues ganado = new ContentValues();
                        ganado.put("estado","Preñada");

                        handler.actualizarGanado(codigo_ganado,ganado);

                        new AlertDialog.Builder(DatosReproductivos.this)
                                .setTitle("Confirmación")
                                .setMessage("Los datos de concepción han sido guardados/actualizados exitosamente.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .create().show();
                    }else{
                        Snackbar.make(v,"Error al guardar datos..",Snackbar.LENGTH_LONG)
                                .setAction("Accion",null).show();
                    }

                }
            }
        });
        btnCancelar.setOnClickListener(v -> finish());

        btnNuevoParto.setEnabled(true);
        btnNuevoParto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valida = true;


                if(tie_codPareja.getText().toString().equals("")){
                    valida = false;
                    tie_codPareja.setError("Ingrese el código de pareja");
                }
                if(tie_fecCelo.getText().toString().equals("")){
                    valida = false;
                    til_fecCelo.setError("Ingrese la fecha de celo");
                }
                if(tie_fechaMonta.getText().toString().equals("")){
                    valida = false;
                    til_fechaMonta.setError("Ingrese la fecha de monta");
                }
                if(tie_fechaPartoReal.getText().toString().equals("")){
                    valida = false;
                    til_fechaPartoReal.setError("Ingrese la fecha de parto");
                }
                if(tie_servicios.getText().toString().equals("")){
                    valida = false;
                    til_servicios.setError("Ingrese el número de servicio");
                }
                if(!cb_hembra.isChecked() && !cb_macho.isChecked()){
                    valida = false;
                    Snackbar.make(v,"Primero selecciona el genero de la cria",Snackbar.LENGTH_LONG)
                            .setAction("Accion",null).show();
                }
                if(sp_estado_parto.getSelectedItem().toString().equals("Vacia") || sp_estado_parto.getSelectedItem().toString().equals("Concepcion")){
                    valida = false;
                    Snackbar.make(v,"Estado debe ser Parto o Aborto",Snackbar.LENGTH_LONG)
                            .setAction("Accion",null).show();
                }if(!ganado.getEstado().equals("Preñada")){
                    valida = false;
                    Snackbar.make(v,"Debe guardar los datos antes de registrar parto",Snackbar.LENGTH_LONG)
                            .setAction("Accion",null).show();
                }

                if(valida) {

                    new AlertDialog.Builder(DatosReproductivos.this)
                            .setTitle("Confirmación")
                            .setMessage("Está seguro de registrar el parto con los datos seleccionados?")
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(agregarParto() > 0 ){
                                        Snackbar.make(v,"Datos agregados al registro ESTADO: "+ganado.getEstado(),Snackbar.LENGTH_LONG)
                                                .setAction("Accion",null).show();
                                        listParto.clear();
                                        consultarDatos();
                                        consultarParto();
                                        calcularIndiceRep();
                                    }else{
                                        Snackbar.make(v,"Error al registrar los datos",Snackbar.LENGTH_LONG)
                                                .setAction("Accion",null).show();
                                    }

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
            }
        });
    }

    public void consultarParto(){

        Cursor cursor =
                myDB.rawQuery("select * from " + BuildConfig.Tabla_Parto + " where codigo_ganado = '" + codigo_ganado + "' order by id asc", null);

       // listParto.clear();

        while (cursor.moveToNext()) {
            ElementoRegistroReproductivo e = new ElementoRegistroReproductivo();
            e.setCodigo_ganado(cursor.getString(2));
            e.setFecha(cursor.getString(3));
            e.setFecha_parto_real(cursor.getString(4));
            e.setGenero_cria(cursor.getString(5));
            e.setEstado(cursor.getString(6));
            e.setObservacion(cursor.getString(7));
            e.setNum_partos(cursor.getInt(8));
            e.setNum_servicios(cursor.getInt(9));
            listParto.add(e);
        }

        if(ganado.getDatosReproductivos() != null) {
            rv_registroRep.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
            rv_registroRep.setItemAnimator(new DefaultItemAnimator());
            rv_registroRep.setAdapter(new AdpRegistroReproductivo(listParto));
        }
    }

    public void consultarDatos(){

        if(existe()) {

                ArrayList<ElementoRegistroReproductivo> listaRep = new ArrayList<>();
                String fechaMonta = "";
                String fechaPEstim = "";
                String fechaPReal = "";
                String observacion = "";
                int num_servicio = 0;
                String estado_parto = "";
                String inseminacion = "";
                String codPareja = "";
                String fecha_celo = "";

                Cursor myCursor =
                        myDB.rawQuery("select * from " + BuildConfig.Tabla_Reproductivos + " where codigo_ganado = '" + codigo_ganado + "' order by id asc", null);

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
                    registro.setInseminacion(myCursor.getString(11));
                    registro.setCodPareja(myCursor.getString(12));
                    registro.setFecha_celo(myCursor.getString(13));

                    listaRep.add(registro);

                    fechaMonta = myCursor.getString(2);
                    fechaPEstim = myCursor.getString(3);
                    fechaPReal = myCursor.getString(4);
                    observacion = myCursor.getString(5);
                    num_servicio = myCursor.getInt(6);
                    inseminacion = myCursor.getString(11);
                    codPareja = myCursor.getString(12);
                    fecha_celo = myCursor.getString(13);

                    num_parto = myCursor.getInt(7);
                    estado_parto = myCursor.getString(10);

                    regActual = registro;
                }

            if (ganado.getEstado().equals("Preñada")) {
                ElementoRegistroReproductivo reg = listaRep.get(listaRep.size()-1);

                tie_codPareja.setText(reg.getCodPareja());
                tie_fecCelo.setText(reg.getFecha_celo());

                tie_fechaMonta.setText(new FormatoFecha().formatearFecha(reg.getFecha_monta()));
                tie_fechaPartoEstim.setText(new FormatoFecha().formatearFecha(reg.getFecha()));
                if(fechaPReal != null) tie_fechaPartoReal.setText(new FormatoFecha().formatearFecha(reg.getFecha_parto_real()));
                et_observacion.setText(reg.getObservacion());
                tie_servicios.setText(String.valueOf(num_servicio));

                for (int i = 0; i < sp_estado_parto.getAdapter().getCount(); i++) {
                    if (sp_estado_parto.getAdapter().getItem(i).toString().contains(estado_parto)) {
                        sp_estado_parto.setSelection(i);
                    }
                }
                for (int i = 0; i < sp_inseminacion.getAdapter().getCount(); i++) {
                    if (sp_inseminacion.getAdapter().getItem(i).toString().contains(inseminacion)) {
                        sp_inseminacion.setSelection(i);
                    }
                }
            }
             myCursor.close();

             ganado.setDatosReproductivos(listaRep);

        }
    }

    public long guardarDatos(ContentValues row, String estado){
        ContentValues ganado = new ContentValues();


        long result = 0;
        if(!existe()) {
            result =  myDB.insert(BuildConfig.Tabla_Reproductivos,null,row); //-1 on error
        }else{
            if(this.ganado.getEstado().equals("Vacia")) num_parto += 1;
            row.put("num_partos",num_parto);
            result = myDB.update(BuildConfig.Tabla_Reproductivos,row,"codigo_ganado=? and num_partos=?", new String[]{codigo_ganado,String.valueOf(num_parto)} ); //numero de filas afectadas
            if(result == 0){
                result = myDB.insert(BuildConfig.Tabla_Reproductivos,null,row);
            }
        }
        ganado.put("estado",estado);
        this.ganado.setEstado(estado);
        handler.actualizarGanado(codigo_ganado,ganado);
        return result;
    }

    private boolean existe(){
        Cursor myCursor = myDB.rawQuery("select * from "+BuildConfig.Tabla_Reproductivos+ " where codigo_ganado = '"+codigo_ganado+"'", null);
        boolean existe = false;
        while(myCursor.moveToNext()) {
            existe = true;
        }
        myCursor.close();
        return existe;
    }

    public void calcularIndiceRep(){

        if(existe()) {
            ArrayList<ElementoGanado> hato = handler.obtenerHato();

            ElementoRegistroReproductivo primerReg = new ElementoRegistroReproductivo();
            ElementoRegistroReproductivo antepReg = new ElementoRegistroReproductivo();
            ElementoRegistroReproductivo ultReg = new ElementoRegistroReproductivo();

            double noConcepcion = 0;
            double sc = 0;
            double srvEnVacas = 0;
            double becerNac = 0;

            for(ElementoGanado ganado:hato){
                if(ganado.getEstado().equals("Preñada")){
                    noConcepcion +=1;
                }
            }

            for(ElementoRegistroReproductivo er : listParto){
                srvEnVacas += er.getNum_servicios();
                if(er.getEstado()!= null && er.getEstado().equals("Parto")) becerNac +=1;
            }

            double tasaPrenez = Math.round(((noConcepcion/hato.size())*100)*100.0)/100.0;
            double tasaParto = Math.round(((becerNac/hato.size())*100)*100.0)/100.0;

            if(tasaPrenez < 0 ) tasaPrenez = 0;

            if(listParto.size() > 0 ) {

                primerReg = listParto.get(0);
                ultReg = listParto.get(listParto.size()-1);

                if(listParto.size() >1){
                    antepReg = listParto.get(listParto.size()-2);
                    tv_intervalo.setText(new FormatoFecha().getDias(antepReg.getFecha_parto_real(),ultReg.getFecha_parto_real())+ " días");
                }
                sc = Math.round((srvEnVacas/listParto.size())*100.0)/100.0;
                tv_primerParto.setText(new FormatoFecha().formatearFecha(primerReg.getFecha_parto_real()));
                tv_servicios.setText(String.valueOf(sc));
                tv_ultServicio.setText(String.valueOf(ultReg.getNum_servicios()));
            }

        if(ganado.getEstado() != null && ganado.getDatosReproductivos().size() > 0){

            ElementoRegistroReproductivo ultR = ganado.getDatosReproductivos().get(ganado.getDatosReproductivos().size() -1);
         /*   if(ganado.getEstado().equals("Vacia")) {
                try {
                    String Dateinicio = ultR.getFecha_parto_real();
                    SimpleDateFormat date = new SimpleDateFormat("ddMMyyyy");
                    Date fechafin = new Date(System.currentTimeMillis());
                    Date fechaInicio      = date.parse(Dateinicio);
                    int milisecondsByDay = 86400000;
                    int dias = (int) ((fechafin.getTime()-fechaInicio.getTime()) / milisecondsByDay);
                    if(dias < 0) dias = 0;
                    tv_diasAbiertos.setText(dias + " días");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else{*/
                tv_fecSecado.setText(new FormatoFecha().sumMeses(new FormatoFecha().formatearFecha(ultR.getFecha_monta()),8)); //Fecha de monta + 7 meses
                if(ganado.getDatosReproductivos().size() > 1) {
                    ElementoRegistroReproductivo antpR = ganado.getDatosReproductivos().get(ganado.getDatosReproductivos().size() - 2);
                    tv_diasAbiertos.setText(new FormatoFecha().getDias(antpR.getFecha_parto_real(), ultR.getFecha_monta()) + " días");
                }
            //}
        }

        tv_tasaParto.setText(tasaParto + " %");
        tv_numPartos.setText(String.valueOf(listParto.size()));
        tv_tasaConcep.setText(tasaPrenez+" %");
    }
    }

    public void calcularParto(int numeroDeaton, TablaDeDeaton deaton, int year, int month){

        int resultado = diasGestacion - numeroDeaton;
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
            tie_fechaPartoEstim.setText(sdf.format(date));
        }

    }

    public long agregarParto(){

        ContentValues reg = new ContentValues();

        if(regActual != null) {
            String cria = "";
            if(cb_hembra.isChecked()){
                cria = "H";
            }else if(cb_macho.isChecked()){
                cria = "M";
            }

            reg.put("codigo_ganado", regActual.getCodigo_ganado());
            reg.put("fecha_parto", regActual.getFecha());
            reg.put("fecha_parto_real", tie_fechaPartoReal.getText().toString().replace("/",""));
            reg.put("genero_cria", cria);
            reg.put("estado_parto", sp_estado_parto.getSelectedItem().toString().replace("/", ""));
            reg.put("observacion", et_observacion.getText().toString());
            reg.put("num_partos",num_parto+1);
            reg.put("num_servicios",Integer.valueOf(tie_servicios.getText().toString()));
        }
        long result =  myDB.insert(BuildConfig.Tabla_Parto,null,reg);
        if( result > 0) {

            ContentValues row = new ContentValues();
            row.put("codigo_ganado", codigo_ganado);
            row.put("fecha_monta", tie_fechaMonta.getText().toString().replace("/", ""));
            row.put("fecha_parto", tie_fechaPartoEstim.getText().toString().replace("/", ""));
            row.put("fecha_parto_real", tie_fechaPartoReal.getText().toString().replace("/", ""));
            row.put("observacion",et_observacion.getText().toString());
            row.put("num_servicios",Integer.valueOf(tie_servicios.getText().toString()));
            row.put("num_partos",num_parto);
            row.put("estado_parto",sp_estado_parto.getSelectedItem().toString());

            tie_codPareja.setText("");
            tie_fecCelo.setText("");
            tie_servicios.setText("1");
            tie_fechaMonta.setText("");
            tie_fechaPartoEstim.setText("");
            tie_fechaPartoReal.setText("");
            et_observacion.setText("");
            cb_hembra.setChecked(false);
            cb_macho.setChecked(false);
            sp_estado_parto.setSelection(0);
            sp_inseminacion.setSelection(0);

            //num_parto += 1;
            guardarDatos(row,"Vacia");

            ContentValues ganado = new ContentValues();
            ganado.put("estado","Vacia");
            this.ganado.setEstado("Vacia");
            handler.actualizarGanado(codigo_ganado,ganado);

        }
        return result;
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

}
