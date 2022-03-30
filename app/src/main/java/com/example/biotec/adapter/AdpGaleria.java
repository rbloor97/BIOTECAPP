package com.example.biotec.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.biotec.R;
import com.example.biotec.clases.ElementoFoto;
import com.example.biotec.clases.FormatoFecha;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdpGaleria extends RecyclerView.Adapter<AdpGaleria.GaleriaViewHolder> {
    private ArrayList<ElementoFoto>  fotos;

    public AdpGaleria(ArrayList<ElementoFoto> fotos){
        this.fotos = fotos;
    }

    @NonNull
    @Override
    public GaleriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdpGaleria.GaleriaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consultar_ganado,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GaleriaViewHolder holder, int i) {
        holder.iv_consultarGanado.setImageURI(Uri.parse(fotos.get(i).getImagen()));
        String codigo = fotos.get(i).getCodigo_ganado();
        String fecha = fotos.get(i).getFecha();

        holder.tv_codigo.setText("Código: " + codigo);
        holder.tv_nombre.setText("Fecha: " + new FormatoFecha().formatearFecha(fecha));
        holder.tv_raza.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }

    public class GaleriaViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_consultarGanado;
        private TextView tv_codigo, tv_nombre, tv_raza;
        private LinearLayout ll_consultar_ganado;

        public GaleriaViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_consultarGanado = itemView.findViewById(R.id.iv_consultarGanado);
            tv_codigo = itemView.findViewById(R.id.tv_codigo);
            tv_nombre = itemView.findViewById(R.id.tv_nombre);
            tv_raza = itemView.findViewById(R.id.tv_raza);
            ll_consultar_ganado = itemView.findViewById(R.id.ll_consultar_ganado);
        }
    }
}
