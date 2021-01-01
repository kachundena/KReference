package com.kachundena.kreference;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kachundena.kreference.controller.referenceController;
import com.kachundena.kreference.model.reference;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ViewReference   extends AppCompatActivity {
    private referenceController ReferenceController;
    TextView txt_desc;
    EditText txt_id;
    WebView vw_url;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_reference);
        txt_desc = findViewById(R.id.tv_desc);
        txt_id = findViewById(R.id.txt_id);
        vw_url = findViewById(R.id.vw_url);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
            return;
        }
        int referenceid = extras.getInt("referenceid");
        if (referenceid != 0) {
            ReferenceController = new referenceController(ViewReference.this);
            try {
                reference Reference = ReferenceController.getReference(referenceid);
                txt_id.setText(Integer.toString(Reference.getReference_id()));
                txt_desc.setText(Reference.getDescription());
                final WebSettings vwUrlAdj = vw_url.getSettings();
                vwUrlAdj.setJavaScriptEnabled(true);
                vw_url.loadUrl(Reference.getUrl());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            txt_id.setText("0");
        }
        final Button btn_goback = findViewById(R.id.btn_goback);
        btn_goback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    goBack(v);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebView vwUrl;
        vwUrl = (WebView) findViewById(R.id.vw_url);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (vwUrl.canGoBack()) {
                        vwUrl.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    public void goBack(View view) throws ParseException {
        int referenceid = Integer.parseInt(txt_id.getText().toString());
        Intent intent = new Intent(this, EditReference.class);
        intent.putExtra("referenceid", referenceid);
        startActivity(intent);
    }


}


