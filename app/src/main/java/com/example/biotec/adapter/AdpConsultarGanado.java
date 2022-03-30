package com.example.biotec.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.telephony.data.ApnSetting;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.biotec.Galeria;
import com.example.biotec.GanadoDetalle;
import com.example.biotec.R;
import com.example.biotec.clases.ElementoGanado;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class AdpConsultarGanado extends RecyclerView.Adapter<AdpConsultarGanado.ConsultarGanadoViewHolder>{

    private ArrayList<ElementoGanado> listaGanado;
    private ArrayList<ElementoGanado> listaFiltro;
    private Context context;
    private String busqueda;
    private String param;

    public AdpConsultarGanado(ArrayList<ElementoGanado> listaGanado, ArrayList<ElementoGanado> listaFiltro, Context context, String busqueda, String param) {
        this.listaGanado = listaGanado;
        this.context = context;
        this.listaFiltro = listaFiltro;
        this.busqueda = busqueda;
        this.param = param;
    }

    @NonNull
    @Override
    public ConsultarGanadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdpConsultarGanado.ConsultarGanadoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consultar_ganado,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultarGanadoViewHolder holder, int i) {
        String codigo = listaGanado.get(i).getCodigo();
        String imagen = listaGanado.get(i).getImagen();

        holder.tv_codigo.setText("Código: " + codigo);
        holder.tv_nombre.setText("Especie: " + listaGanado.get(i).getEspecie());
        holder.tv_raza.setText("Raza: " + listaGanado.get(i).getRaza());
        holder.tv_estado.setText("Estado: " + listaGanado.get(i).getEstado());
        if(imagen != null && !imagen.equals("")) {
            holder.iv_consultarGanado.setImageURI(Uri.parse(imagen));
        }else{
            Drawable toro = context.getResources().getDrawable(R.drawable.img_toro);
            Drawable vaca = context.getResources().getDrawable(R.drawable.img_cow);

            if(listaGanado.get(i).getGenero().equals("M")){
                holder.iv_consultarGanado.setImageDrawable(toro);
            }else{
                holder.iv_consultarGanado.setImageDrawable(vaca);
            }

        }

        holder.ll_consultar_ganado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(param.equals("G")){
                    intent = new Intent(context, Galeria.class);
                }else {
                    intent = new Intent(context, GanadoDetalle.class);
                }
                intent.putExtra("codigo_ganado",codigo);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listaGanado.size();
    }

    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString();
                ArrayList<ElementoGanado> filtered = new ArrayList<>();
                if (query.isEmpty() || query.length()==1 ) {
                    filtered = listaGanado;
                }else{
                    for(ElementoGanado ganado: listaFiltro){
                        if(busqueda.equals("Código")) {
                            if (ganado.getCodigo().toLowerCase().contains(query.toLowerCase())) {
                                filtered.add(ganado);
                            }
                        }
                        else if(busqueda.equals("Especie")) {
                            if (ganado.getEspecie().toLowerCase().contains(query.toLowerCase())) {
                                filtered.add(ganado);
                            }
                        }
                        else if(busqueda.equals("Raza")) {
                            if (ganado.getRaza().toLowerCase().contains(query.toLowerCase())) {
                                filtered.add(ganado);
                            }
                        }
                    }

                }
                FilterResults results = new FilterResults();
                results.count = filtered.size();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listaGanado = (ArrayList<ElementoGanado>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ConsultarGanadoViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_consultarGanado;
        private TextView tv_codigo, tv_nombre, tv_raza, tv_estado;
        private LinearLayout ll_consultar_ganado;

        public ConsultarGanadoViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_consultarGanado = itemView.findViewById(R.id.iv_consultarGanado);
            tv_codigo = itemView.findViewById(R.id.tv_codigo);
            tv_nombre = itemView.findViewById(R.id.tv_nombre);
            tv_raza = itemView.findViewById(R.id.tv_raza);
            tv_estado = itemView.findViewById(R.id.tv_estado);
            ll_consultar_ganado = itemView.findViewById(R.id.ll_consultar_ganado);
        }
    }
}
