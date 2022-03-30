package com.example.biotec.clases;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormatoFecha {

    public String formatearFecha(String fecha){

        if( fecha.length() == 8){
            String dia = fecha.substring(0,2);
            String mes = fecha.substring(2,4);
            String anio = fecha.substring(4,8);
            return dia+"/"+mes+"/"+anio;
        }
        return "";
    }

    public int getDias(String fechaI, String fechaF){
        int dias = 0;
        SimpleDateFormat date = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat date2 = new SimpleDateFormat("ddMMyyyy");

        try {
            Date fechaInicio = date.parse(fechaI);
            Date fechafin = date2.parse(fechaF);
            int milisecondsByDay = 86400000;
            dias = (int) ((fechafin.getTime()-fechaInicio.getTime()) / milisecondsByDay);
            if(dias < 0) dias = 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dias;
    }

    public String sumMeses(String fecha, int suma){
        Calendar fechaI = Calendar.getInstance();

        try {
            fechaI.setTime(new SimpleDateFormat("dd/MM/yyy").parse(fecha));
            fechaI.add(Calendar.MONTH,suma);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  fechaI.get(Calendar.DAY_OF_MONTH) + "/"+fechaI.get(Calendar.MONTH)+"/"+fechaI.get(Calendar.YEAR);
    }

    public String obtenerEdad(String fecha){
        Calendar currentDate = Calendar.getInstance();
        Calendar bornDate = Calendar.getInstance();
        String edad = "";
        int difM;

        currentDate.getTime();
        try {
            bornDate.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(fecha));
            if(bornDate.getTime().getTime() > currentDate.getTime().getTime()){
                return null;
            }
            int difA = currentDate.get(Calendar.YEAR) - bornDate.get(Calendar.YEAR);

            difM = difA*12 +  currentDate.get(Calendar.MONTH) - bornDate.get(Calendar.MONTH);


            if(difA >= 1 && bornDate.get(Calendar.MONTH) > currentDate.get(Calendar.MONTH) ){
                difA -= 1;
            }

            if(difM >= 1 && bornDate.get(Calendar.DAY_OF_MONTH) > currentDate.get(Calendar.DAY_OF_MONTH) ){
                difM -= 1;
            }
            if(difA > 0) difM -= (difA*12);
            edad = difA + "A "+ difM + "M";

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        return edad;
    }
}
