package com.example.biotec.clases;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Arrays;

public class ElementoSpinner implements Serializable {

    private final static String TAG = ElementoSpinner.class.getSimpleName();
    private String codigo, descripcion;
    private String id_padre = "0";

    public ElementoSpinner(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public ElementoSpinner(JSONObject object) {
        try {
            this.codigo = String.valueOf(object.getInt("id"));
            this.descripcion = object.getString("descripcion");
            this.id_padre = String.valueOf(object.getInt("id_padre"));
        } catch (JSONException e) {
            Log.v(TAG, Arrays.toString(e.getStackTrace()));
        }
    }

    public String getId_padre() {
        return id_padre;
    }

    public void setId_padre(String id_padre) {
        this.id_padre = id_padre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
