package com.kachundena.kreference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kachundena.kreference.adapter.referenceAdapter;
import com.kachundena.kreference.controller.referenceController;
import com.kachundena.kreference.model.reference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<reference> References;
    private referenceController ReferenceController;
    private referenceAdapter ReferenceAdapter;

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ReferenceController = new referenceController(MainActivity.this);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewConcepts);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        References = new ArrayList<>();

        // specify an adapter (see also next example)
        ReferenceAdapter = new referenceAdapter(References);
        recyclerView.setAdapter(ReferenceAdapter);
        try {
            refreshReferences();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            public void onClick(View view, int position) {
                reference selectedReference = References.get(position);
                Intent intent = new Intent(MainActivity.this, EditReference.class);
                intent.putExtra("referenceid", selectedReference.getReference_id());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        final Button btn_newitem = findViewById(R.id.btn_newitem);
        btn_newitem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditReference.class);
                intent.putExtra("referenceid", 0);
                startActivity(intent);
            }
        });



        final EditText tv_filter = findViewById(R.id.tv_filter);
        tv_filter.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //if (keyCode == KeyEvent.KEYCODE_ENTER) {//
                    try {
                        refreshReferences(tv_filter.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                //}
                return false;
            }
        });
    }

    public void refreshReferences() throws ParseException {
        if (ReferenceAdapter == null) return;
        References = ReferenceController.getReferences();
        ReferenceAdapter.setReferenceList(References);
        ReferenceAdapter.notifyDataSetChanged();
    }

    public void refreshReferences(String filter) throws ParseException {
        if (ReferenceAdapter == null) return;
        References = ReferenceController.getReferences(filter);
        ReferenceAdapter.setReferenceList(References);
        ReferenceAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_about:
                openAbout();
                return true;
            case R.id.navigation_home:
                return true;
            case R.id.navigation_import:
                Context contexte = getApplicationContext();
                JsonImport();
                CharSequence texte = "Import data";
                int duratione = Toast.LENGTH_SHORT;
                Toast toaste = Toast.makeText(contexte, texte, duratione);
                toaste.show();
                return true;
            case R.id.navigation_export:
                Context contexti = getApplicationContext();
                JsonExport();
                CharSequence texti = "Export data";
                int durationi = Toast.LENGTH_SHORT;
                Toast toasti = Toast.makeText(contexti, texti, durationi);
                toasti.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void StringToJSON(File fileJSON, String valor) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // start runtime permission
                Boolean hasPermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED);
                if (!hasPermission) {
                    //Log.e(TAG, "get permision   ");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {
                    //Log.e(TAG, "get permision-- already granted ");
                }
            } else {
                //readfile();
            }
            fileJSON.createNewFile();
            FileWriter fileWriter = new FileWriter(fileJSON);
            valor = "{\n" +
                    "    \"references\": " + valor + "\n" +
                    "}";
            fileWriter.write(valor);
            //System.out.println(valor);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected String JSONToString(File fileJSON) {
        try {
            String retorno = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // start runtime permission
                Boolean hasPermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED);
                if (!hasPermission) {
                    //Log.e(TAG, "get permision   ");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {
                    //Log.e(TAG, "get permision-- already granted ");
                }
            } else {
                //readfile();
            }
            FileInputStream fis = new FileInputStream(fileJSON);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                retorno = retorno + strLine;
            }
            in.close();
            return retorno;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    protected void JsonImport() {
        try {
            // get JSONObject from JSON file
            File fileData = null;
            File sdcard = Environment.getExternalStorageDirectory();
            File dir = new File(sdcard.getAbsolutePath() + "/kreference/");
            if (dir.exists()) {
                fileData = new File(dir, "references_import.json");
            }
            JSONObject obj = new JSONObject(JSONToString(fileData));
            // fetch JSONArray named users
            JSONArray JReferences = obj.getJSONArray("references");
            for (int i = 0; i < JReferences.length(); i++) {
                // create a JSONObject for fetching single user data
                JSONObject JReference = JReferences.getJSONObject(i);
                long reference_id = 0;
                reference Reference = new reference();
                Reference.setReference_id(0);
                Reference.setDescription(JReference.getString("description"));
                Reference.setUrl(JReference.getString("url"));
                Reference.setStart_date(dateFormat.parse(JReference.getString("start_date")));
                Reference.setMetadata(JReference.getString("metadata"));
                reference_id = ReferenceController.newReference(Reference);
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    protected void JsonExport() {
        try {
            ArrayList<reference> References = ReferenceController.getReferences();
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(References);            // write to string
            // get JSONObject from JSON file
            File fileData = null;
            File sdcard = Environment.getExternalStorageDirectory();
            File dir = new File(sdcard.getAbsolutePath() + "/kreference/");
            if (dir.exists()) {
                fileData = new File(dir, "references_export.json");
            }
            // Write to file
            StringToJSON(fileData, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected File getFileJSON(File myExternalFile, String szFilePath) {
        String filename = "pm.json";
        String filepath = "Download";
        String retorno = "";
        String myData = "";
        if (!szFilePath.isEmpty()) {
            if (szFilePath.startsWith("/document/raw:")) {
                retorno = szFilePath.replace("/document/raw:", "");
                //szFilePath.replaceFirst("/document/raw:", "");
            } else {
                retorno = szFilePath;
            }
        }
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            myExternalFile = null;
        } else {
            //myExternalFile = new File(getExternalFilesDir(filepath), filename);
            myExternalFile = new File(retorno);
        }
        return myExternalFile;
    }


    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public void openAbout() {
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }


}