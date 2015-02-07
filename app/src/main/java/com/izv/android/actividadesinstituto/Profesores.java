package com.izv.android.actividadesinstituto;

import org.json.JSONObject;

/**
 * Created by ZAFRA on 06/02/2015.
 */
public class Profesores {

    private String id, nombre, apellidos, departamento;

    public Profesores() {
    }

    public Profesores(JSONObject object) {
        try {
            this.id = object.getString("id");
            this.nombre = object.getString("nombre");
            this.apellidos=object.getString("apellidos");
            this.departamento=object.getString("departamento");
        } catch (Exception e) {

        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }


}
