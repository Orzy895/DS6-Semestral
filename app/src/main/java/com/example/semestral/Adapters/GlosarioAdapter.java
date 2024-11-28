package com.example.semestral.Adapters;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.example.semestral.Entidades.Paises;
import com.example.semestral.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GlosarioAdapter extends ArrayAdapter<Paises> {

    public GlosarioAdapter(Context context, List<Paises> paises){
        super(context, 0, paises);
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listviewglosario, parent, false);
        }
        Paises pais = getItem(pos);
        Log.i(TAG, "getView: "+pais);
        TextView nombre = convertView.findViewById(R.id.glosarioPaisNombre);
        if(pais != null){
            nombre.setText(pais.getPais());
        }
        nombre.setOnClickListener(v->{
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.glosariocustomdialog);
            Log.i(TAG, "en dialog "+pais);
            TextView nombrePais = dialog.findViewById(R.id.txtGlosarioPaís);
            TextView capitalPais = dialog.findViewById(R.id.txtGlosarioCapital);
            ImageView banderaPais = dialog.findViewById(R.id.imgGlosarioBandera);
            ImageView mapaPais = dialog.findViewById(R.id.imgGlosarioMapa);
            ImageView localPais = dialog.findViewById(R.id.imgGlosarioLocal);
            AppCompatButton cerrar = dialog.findViewById(R.id.btnGlosarioCerrar);
            nombrePais.setText(pais.getPais());
            capitalPais.setText(pais.getCapital());
            Picasso.get().load(pais.getBandera()).into(banderaPais);
            Picasso.get().load(pais.getMapa()).into(mapaPais);
            Picasso.get().load(pais.getLocalizacion()).into(localPais);
            cerrar.setOnClickListener(view -> dialog.dismiss());
            dialog.show();
        });
        return convertView;
    }

}
