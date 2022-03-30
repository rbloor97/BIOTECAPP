package com.example.biotec.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.biotec.R;
import com.example.biotec.clases.ElementoRegistroReproductivo;
import com.example.biotec.clases.FormatoFecha;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdpRegistroReproductivo extends RecyclerView.Adapter<AdpRegistroReproductivo.RegistroReproductivoViewholder> {

    private ArrayList<ElementoRegistroReproductivo> listaRegistro;

    public AdpRegistroReproductivo(ArrayList<ElementoRegistroReproductivo> listaRegistro){
        this.listaRegistro = listaRegistro;
    }


    @NonNull
    @Override
    public RegistroReproductivoViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdpRegistroReproductivo.RegistroReproductivoViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_registro_reproductivo,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RegistroReproductivoViewholder holder, int i) {
        holder.tv_reg1.setText(new FormatoFecha().formatearFecha(listaRegistro.get(i).getFecha_parto_real()));
        if(listaRegistro.get(i).getGeneroCria() == null){
            holder.tv_reg2.setText("N/A");
        }else {
            holder.tv_reg2.setText(listaRegistro.get(i).getGeneroCria());
        }
        holder.tv_reg3.setText(listaRegistro.get(i).getObservacion());
        holder.tv_reg4.setText(listaRegistro.get(i).getEstado());
        if((i%2 != 0)) holder.table_reproductivo.setBackgroundColor(Color.parseColor("#e6e6e6"));
    }

    @Override
    public int getItemCount() {
        return listaRegistro.size();
    }

    public class RegistroReproductivoViewholder extends RecyclerView.ViewHolder{
        private TextView tv_reg1, tv_reg2, tv_reg3, tv_reg4;
        private TableLayout table_reproductivo;

        public RegistroReproductivoViewholder(@NonNull View itemView) {
            super(itemView);
            tv_reg1 = itemView.findViewById(R.id.tv_reg1);
            tv_reg2 = itemView.findViewById(R.id.tv_reg2);
            tv_reg3 = itemView.findViewById(R.id.tv_reg3);
            tv_reg4 = itemView.findViewById(R.id.tv_reg4);
            table_reproductivo = itemView.findViewById(R.id.table_reproductivo);
        }
    }
}
