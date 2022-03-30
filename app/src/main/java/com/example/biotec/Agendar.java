package com.example.biotec;

import android.os.Bundle;
import android.widget.CalendarView;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Agendar extends AppCompatActivity {
    CalendarView calendar;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_agendar);

        calendar = findViewById(R.id.calendar_view);

        getSupportActionBar().setTitle("Agendar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        long selectedDate = calendar.getDate();
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String fecha = dayOfMonth + "/" + (month +1) +"/"+year;
                Snackbar.make(view,fecha,Snackbar.LENGTH_SHORT)
                        .setAction("Accion",null).show();
            }
        });

    }

}
