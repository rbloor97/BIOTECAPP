package com.example.biotec.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.example.biotec.AcercaDe;
import com.example.biotec.Agendar;
import com.example.biotec.ConsultarGanado;
import com.example.biotec.Galeria;
import com.example.biotec.RegistrarGanado;
import com.example.biotec.Settings;
import com.example.biotec.databinding.FragmentHomeBinding;
import com.google.android.material.snackbar.Snackbar;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private LinearLayout ll_registrar, ll_consultar, ll_agendar, ll_ajustes;
    CardView cv_registrar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textHome;
        cv_registrar = binding.cvRegistrar;
        ll_registrar = binding.llRegistrar;
        ll_consultar = binding.llConsultar;
        ll_agendar = binding.llAgendar;
        ll_ajustes = binding.llAjustes;

        ll_ajustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Settings.class);
                getContext().startActivity(intent);
                }
        });


        ll_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), RegistrarGanado.class);
                getContext().startActivity(intent);
            }
        });

        ll_consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ConsultarGanado.class);
                intent.putExtra("param","C");
                getContext().startActivity(intent);
            }
        });

        ll_agendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Agendar.class);
                getContext().startActivity(intent);
            }
        });

        binding.llGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ConsultarGanado.class);
                intent.putExtra("param","G");
                getContext().startActivity(intent);
            }
        });

        binding.llAcerdaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AcercaDe.class);
                getContext().startActivity(intent);
            }
        });
        /*homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}