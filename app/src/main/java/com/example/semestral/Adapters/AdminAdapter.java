package com.example.semestral.Adapters;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.example.semestral.Entidades.Paises;
import com.example.semestral.Entidades.Ranking;
import com.example.semestral.R;
import com.example.semestral.Services.ApiService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAdapter extends ArrayAdapter<Paises> {

    public AdminAdapter(Context context, List<Paises> paises){
        super(context, 0, paises);
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listviewadmin, parent, false);
        }
        Paises pais = getItem(pos);
        TextView nombre = convertView.findViewById(R.id.paisNombre);
        ImageView editar = convertView.findViewById(R.id.btnEditar);
        ImageView borrar = convertView.findViewById(R.id.btnBorrar);
        if(pais != null){
            nombre.setText(pais.getPais());
        }
        editar.setOnClickListener(v -> {
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.admincustomdialog);

            EditText txtPais = dialog.findViewById(R.id.txtAdminPais);
            EditText txtCapital = dialog.findViewById(R.id.txtAdminCapital);
            EditText txtBandera = dialog.findViewById(R.id.txtAdminBandera);
            EditText txtMapa = dialog.findViewById(R.id.txtAdminMapa);
            EditText txtMapaLocal = dialog.findViewById(R.id.txtAdminLocal);
            ImageView imgBandera = dialog.findViewById(R.id.imgAdminBandera);
            ImageView imgMapa = dialog.findViewById(R.id.imgAdminMapa);
            ImageView imgMapaLocal = dialog.findViewById(R.id.imgAdminLocal);
            AppCompatButton cancelar = dialog.findViewById(R.id.btnAdminCancelar);
            AppCompatButton cambiar = dialog.findViewById(R.id.btnAdminCambiar);

            txtPais.setText(pais.getPais());
            txtCapital.setText(pais.getCapital());
            txtBandera.setText(pais.getBandera());
            txtMapa.setText(pais.getMapa());
            txtMapaLocal.setText(pais.getLocalizacion());
            Picasso.get().load(pais.getBandera()).into(imgBandera);
            Picasso.get().load(pais.getMapa()).into(imgMapa);
            Picasso.get().load(pais.getLocalizacion()).into(imgMapaLocal);

            cancelar.setOnClickListener(view -> dialog.dismiss());
            cambiar.setOnClickListener(view -> {
                Paises paisModificado = new Paises();
                paisModificado.setBandera(txtBandera.getText().toString());
                paisModificado.setPais(txtPais.getText().toString());
                paisModificado.setMapa(txtMapa.getText().toString());
                paisModificado.setLocalizacion(txtMapaLocal.getText().toString());
                pais.setCapital(txtCapital.getText().toString());
                Call<Paises> caller = ApiService.getApiService().editCountry(pais.getId(), paisModificado);
                caller.enqueue(new Callback<Paises>() {
                    @Override
                    public void onResponse(Call<Paises> call, Response<Paises> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Pais modificado exitosamente", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Log.e("Error al modificar país", "Error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Paises> call, Throwable t) {

                    }
                });
            });
            dialog.show();
        });
        borrar.setOnClickListener(v ->{
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.admincustomdeleteconfirm);
            AppCompatButton cancelar = dialog.findViewById(R.id.btnEliminarCancelar);
            AppCompatButton confirmar = dialog.findViewById(R.id.btnEliminarConfirmar);
            cancelar.setOnClickListener(view -> dialog.dismiss());
            confirmar.setOnClickListener(view -> {
                Call<Paises> caller = ApiService.getApiService().deleteCountry(pais.getId());
                caller.enqueue(new Callback<Paises>() {
                    @Override
                    public void onResponse(Call<Paises> call, Response<Paises> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Pais borrado exitosamente", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Log.e("Error al eliminar país", "Error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Paises> call, Throwable t) {

                    }
                });
            });
            dialog.show();
        });
        return convertView;
    }

}
