package com.izv.android.actividadesinstituto;

import org.json.JSONObject;

/**
 * Created by ZAFRA on 06/02/2015.
 */
public class ActividadProfesor {

    private String id, idactividad, idprofesor;

    public ActividadProfesor() {
    }

    public ActividadProfesor(JSONObject object) {
        try {
            this.id = object.getString("id");
            this.idprofesor = object.getString("idprofesor");
            this.idactividad=object.getString("idactividad");
        } catch (Exception e) {

        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdactividad() {
        return idactividad;
    }

    public void setIdactividad(String idactividad) {
        this.idactividad = idactividad;
    }

    public String getIdprofesor() {
        return idprofesor;
    }

    public void setIdprofesor(String idprofesor) {
        this.idprofesor = idprofesor;
    }

    public JSONObject getJSON(){
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("id",this.id);
            jsonObject.put("idactividad",this.idactividad);
            jsonObject.put("idprofesor",this.idprofesor);
            return jsonObject;
        }catch (Exception e){
            return null;
        }
    }
}
