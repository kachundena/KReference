package com.kachundena.kreference;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;

public class About extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        final Button btn_dashboard = findViewById(R.id.btn_dashboard);
        btn_dashboard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    goDashBoard(v);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void goDashBoard(View view) throws ParseException {
        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);
    }

}

