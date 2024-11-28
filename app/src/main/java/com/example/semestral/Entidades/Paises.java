package com.example.semestral.Entidades;

import android.os.Parcel;
import android.os.Parcelable;

public class Paises implements Parcelable {

    String pais, capital, bandera, mapa, localizacion, _id;

    public Paises() {
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getBandera() {
        return bandera;
    }

    public void setBandera(String bandera) {
        this.bandera = bandera;
    }

    public String getMapa() {
        return mapa;
    }

    public void setMapa(String mapa) {
        this.mapa = mapa;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    protected Paises(Parcel in) {
        pais = in.readString();
        capital = in.readString();
        bandera = in.readString();
        mapa = in.readString();
        localizacion = in.readString();
        _id = in.readString();
    }

    public static final Creator<Paises> CREATOR = new Creator<Paises>() {
        @Override
        public Paises createFromParcel(Parcel in) {
            return new Paises(in);
        }

        @Override
        public Paises[] newArray(int size) {
            return new Paises[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pais);
        parcel.writeString(capital);
        parcel.writeString(bandera);
        parcel.writeString(mapa);
        parcel.writeString(localizacion);
        parcel.writeString(_id);
    }
}
