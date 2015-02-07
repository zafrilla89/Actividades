package com.izv.android.actividadesinstituto;

import org.json.JSONObject;

/**
 * Created by ZAFRA on 05/02/2015.
 */
public class ActividadGrupo {

    private String id, idactividad, idgrupo;

    public ActividadGrupo() {
    }

    public ActividadGrupo(JSONObject object) {
        try {
            this.id = object.getString("id");
            this.idgrupo = object.getString("idgrupo");
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

    public String getIdgrupo() {
        return idgrupo;
    }

    public void setIdgrupo(String idgrupo) {
        this.idgrupo = idgrupo;
    }

    public JSONObject getJSON(){
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("id",this.id);
            jsonObject.put("idactividad",this.idactividad);
            jsonObject.put("idgrupo",this.idgrupo);
            return jsonObject;
        }catch (Exception e){
            return null;
        }
    }
}
