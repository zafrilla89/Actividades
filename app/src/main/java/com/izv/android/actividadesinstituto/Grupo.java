package com.izv.android.actividadesinstituto;

import org.json.JSONObject;

/**
 * Created by ZAFRA on 05/02/2015.
 */
public class Grupo {
    private String id, grupo;

    public Grupo() {
    }

    public Grupo(JSONObject object) {
        try {
            this.id = object.getString("id");
            this.grupo = object.getString("grupo");
        } catch (Exception e) {

        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }


}
