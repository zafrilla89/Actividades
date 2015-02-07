package com.izv.android.actividadesinstituto;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;


public class AgregarProfesores extends Activity {

    private ArrayList<String> lista;
    private ArrayList<Profesores> profesores;
    private String[] idprofesor;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                       METODOS ON                                           //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregarprofesores);
        lista = new ArrayList<String>();
        profesores = new ArrayList<Profesores>();
        GetSacarProfesores getSacarProfesores = new GetSacarProfesores();
        getSacarProfesores.execute("http://ieszv.x10.bz/restful/api/profesor");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                 METODOS AUXILIARES                                         //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void yacargado() {
        sacarProfesores();
        idprofesor=new String[lista.size()];
        Adaptadordialogo ad = new Adaptadordialogo(this, R.layout.itemdialogo, lista);
        ListView lvprofe = (ListView) findViewById(R.id.lvlistaprofesores);
        lvprofe.setAdapter(ad);
        lvprofe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox cb = (CheckBox) view.findViewById(R.id.cb);
                if (cb.isChecked()) {
                    cb.setChecked(false);
                    idprofesor[i]=null;
                } else {
                    cb.setChecked(true);
                    idprofesor[i] = profesores.get(i).getId();
                }
            }
        });

    }

    private void sacarProfesores() {
        for (int c = 0; c < profesores.size(); c++) {
            Profesores pr = profesores.get(c);
            lista.add(pr.getNombre() + " " + pr.getApellidos());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                     METODOS ONCLICK                                        //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void agregarprofesores(View view) {
        ArrayList <String> ids=new ArrayList<String>();
        for (int i=0;i<idprofesor.length;i++){
            if (idprofesor[i]!=null){
                ids.add(idprofesor[i]);
            }
        }
        Intent i = new Intent();
        if (ids.size()!=0){
            i.putExtra("texto","Profesores seleccionados");
        }else{
            i.putExtra("texto","");
        }
        i.putExtra("idprofesor",ids);
        setResult(Activity.RESULT_OK, i);
        this.finish();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                              METODOS CLIENTESRESTFULL                                      //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    class GetSacarProfesores extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] params) {
            String r = ClienteRestFul.get(params[0]);
            return r;
        }


        @Override
        protected void onPostExecute(String r) {
            super.onPostExecute(r);
            JSONTokener token = new JSONTokener(r);
            try {
                JSONArray array = new JSONArray(token);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Profesores pr = new Profesores(object);
                    profesores.add(pr);
                }
            } catch (Exception e) {

            }
            yacargado();
        }
    }
}

