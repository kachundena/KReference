package com.kachundena.kreference;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.kachundena.kreference.controller.referenceController;
import com.kachundena.kreference.model.reference;
import com.kachundena.kreference.util.DatePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import me.gujun.android.taggroup.TagGroup;

public class EditReference   extends AppCompatActivity {

    private referenceController ReferenceController;
    EditText txt_id;
    EditText txt_desc;
    EditText txt_url;
    EditText txt_startdate;
    //EditText txt_metadata;
    TagGroup tag_metadata;
    TextView txt_titleproject;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_reference);
        txt_id = findViewById(R.id.txt_id);
        txt_desc = findViewById(R.id.txt_desc);
        txt_url = findViewById(R.id.txt_url);
        txt_startdate = findViewById(R.id.txt_startdate);
        //txt_metadata = findViewById(R.id.txt_metadata);
        tag_metadata = (TagGroup) findViewById(R.id.tag_metadata);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
            return;
        }
        int referenceid = extras.getInt("referenceid");
        if (referenceid != 0) {
            ReferenceController = new referenceController(EditReference.this);
            try {
                reference Reference = ReferenceController.getReference(referenceid);
                txt_id.setText(Integer.toString(Reference.getReference_id()));
                txt_desc.setText(Reference.getDescription());
                txt_url.setText(Reference.getUrl());
                txt_startdate.setText(dateFormat.format(Reference.getStart_date()));
                if (Reference.getMetadata() != null) {
                    tag_metadata.setTags(Reference.getMetadata().split("#"));
                }
                //txt_metadata.setText(Reference.getMetadata());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            txt_id.setText("0");
        }
        final Button btn_ok = findViewById(R.id.btn_ok);
        final Button btn_cancel = findViewById(R.id.btn_cancel);
        final Button btn_viewref = findViewById(R.id.btn_viewref);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {
                try {
                    saveReference(v);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    cancelReference(v);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_viewref.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    viewReference(v);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        txt_startdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });



    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveReference(View view) throws ParseException {
        int referenceid = Integer.parseInt(txt_id.getText().toString());
        ReferenceController = new referenceController(EditReference.this);
        String description = txt_desc.getText().toString();
        String url = txt_url.getText().toString();
        String startdate = txt_startdate.getText().toString();
        //String metadata = txt_metadata.getText().toString();
        String metadata  = String.join("#", tag_metadata.getTags());
        reference Reference = new reference();
        Reference.setReference_id(referenceid);
        Reference.setDescription(description);
        Reference.setUrl(url);
        Reference.setStart_date(dateFormat.parse(startdate));
        Reference.setMetadata(metadata);
        if (referenceid == 0) {
            // insert
            ReferenceController.newReference(Reference);
        } else {
            ReferenceController.updateReference(Reference);
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void cancelReference(View view) throws ParseException {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void viewReference(View view) throws ParseException {
        int referenceid = Integer.parseInt(txt_id.getText().toString());
        Intent intent = new Intent(this, ViewReference.class);
        intent.putExtra("referenceid", referenceid);
        startActivity(intent);
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = String.format("%02d" , day) + "/" + String.format("%02d" , month+1) + "/" + String.format("%04d" , year);
                txt_startdate.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }



}

