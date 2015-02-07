package com.izv.android.actividadesinstituto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZAFRA on 11/12/2014.
 */
public class Actividad {

    private String id, idprofesor, tipo, fechai,fechaf, lugari, lugarf, descripcion, alumno;

    public Actividad(String id, String idprofesor, String tipo, String fechai, String fechaf, String lugari, String lugarf, String descripcion, String alumno) {
        this.id = id;
        this.idprofesor = idprofesor;
        this.tipo = tipo;
        this.fechai = fechai;
        this.fechaf = fechaf;
        this.lugari = lugari;
        this.lugarf = lugarf;
        this.descripcion = descripcion;
        this.alumno = alumno;
    }

    public Actividad(JSONObject object){
        try {
            this.id = object.getString("id");
            this.idprofesor = object.getString("idprofesor");
            this.tipo = object.getString("tipo");
            this.fechai = object.getString("fechai");
            this.fechaf = object.getString("fechaf");
            this.lugari = object.getString("lugari");
            this.lugarf = object.getString("lugarf");
            this.descripcion = object.getString("descripcion");
            this.alumno = object.getString("alumno");
        }catch (Exception e){

        }
    }

    public Actividad(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdprofesor() {
        return idprofesor;
    }

    public void setIdprofesor(String idprofesor) {
        this.idprofesor = idprofesor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFechai() {
        return fechai;
    }

    public void setFechai(String fechai) {
        this.fechai = fechai;
    }

    public String getFechaf() {
        return fechaf;
    }

    public void setFechaf(String fechaf) {
        this.fechaf = fechaf;
    }

    public String getLugari() {
        return lugari;
    }

    public void setLugari(String lugari) {
        this.lugari = lugari;
    }

    public String getLugarl() {
        return lugarf;
    }

    public void setLugarl(String lugarl) {
        this.lugarf = lugarl;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAlumno() {
        return alumno;
    }

    public void setAlumno(String alumno) {
        this.alumno = alumno;
    }

    public JSONObject getJSON(){
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("id",this.id);
            jsonObject.put("idprofesor",this.idprofesor);
            jsonObject.put("tipo",this.tipo);
            jsonObject.put("fechai",this.fechai);
            jsonObject.put("fechaf",this.fechaf);
            jsonObject.put("lugari",this.lugari);
            jsonObject.put("lugarf",this.lugarf);
            jsonObject.put("descripcion",this.descripcion);
            jsonObject.put("alumno",this.alumno);
            return jsonObject;
        }catch (Exception e){
            return null;
        }
    }
}
