package com.kachundena.kreference.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kachundena.kreference.model.reference;
import com.kachundena.kreference.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class referenceAdapter extends RecyclerView.Adapter<referenceAdapter.MyViewHolder>{
    private List<reference> referenceList;

    public void setReferenceList(List<reference> References) {

        this.referenceList = References;
    }


    public referenceAdapter(List<reference> References) {

        this.referenceList = References;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rowReference = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_reference, viewGroup, false);
        return new MyViewHolder(rowReference);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        // Obtener la tarea de nuestra lista gracias al índice i
        reference Reference = referenceList.get(i);

        // Obtener los datos de la lista
        int id = Reference.getReference_id();
        String description = Reference.getDescription();
        String url = Reference.getUrl();

        // Y poner a los TextView los datos con setText
        myViewHolder.id.setText(Long.toString(id));
        myViewHolder.description.setText(description);
        myViewHolder.url.setText(url);
        if (i%2 == 0) {
            myViewHolder.itemView.setBackgroundColor(Color.parseColor("#f0f0f0"));
        }
        else {
            myViewHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }

    }

    @Override
    public int getItemCount() {
        return referenceList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, description, url;

        MyViewHolder(View itemView) {
            super(itemView);
            this.id = itemView.findViewById(R.id.txt_ID);
            this.description = itemView.findViewById(R.id.tvDescription);
            this.url = itemView.findViewById(R.id.tvUrl);
        }
    }


}

