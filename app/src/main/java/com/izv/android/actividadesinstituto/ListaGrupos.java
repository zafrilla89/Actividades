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


public class ListaGrupos extends Activity {

    private ArrayList<String> lista;
    private ArrayList<Grupo> grupo;
    private String id;
    private boolean[] guardar;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                       METODOS ON                                           //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_grupos);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            id = b.getString("id");
        }
        lista = new ArrayList<String>();
        grupo = new ArrayList<Grupo>();
        GetSacarProfesores getSacarProfesores = new GetSacarProfesores();
        getSacarProfesores.execute("http://ieszv.x10.bz/restful/api/grupo");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                 METODOS AUXILIARES                                         //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void yacargado() {
        sacarProfesores();
        guardar=new boolean[grupo.size()];
        Adaptadordialogo ad = new Adaptadordialogo(this, R.layout.itemdialogo, lista);
        ListView lvprofe = (ListView) findViewById(R.id.lvlistaprofesores);
        lvprofe.setAdapter(ad);
        lvprofe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox cb = (CheckBox) view.findViewById(R.id.cb);
                if (cb.isChecked()) {
                    Log.v("aaaaaaaaaaaaaa", "no pulsado");
                    cb.setChecked(false);
                    guardar[i]=false;
                } else {
                    cb.setChecked(true);
                    Log.v("aaaaaaaaaaaaaa", "pulsado");
                    guardar[i]=true;
                }
            }
        });

    }

    private void sacarProfesores() {
        for (int c = 0; c < grupo.size(); c++) {
            Grupo g = grupo.get(c);
            lista.add(g.getGrupo());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                     METODOS ONCLICK                                        //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void agregarprofesores(View view){
        for (int i=0;i<guardar.length;i++) {
            if (guardar[i]==true) {
                PostRestfull put = new PostRestfull();
                Parametros p = new Parametros();
                ActividadGrupo ag = new ActividadGrupo();
                ag.setId("0");
                ag.setIdgrupo(grupo.get(i).getId());
                ag.setIdactividad(id);
                p.url = "http://ieszv.x10.bz/restful/api/actividadgrupo";
                p.jsonObject = ag.getJSON();
                put.execute(p);
            }
        }
        Intent i = new Intent();
        setResult(Activity.RESULT_OK, i);
        this.finish();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                              METODOS CLIENTESRESTFULL                                      //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    class GetSacarProfesores extends AsyncTask<String,Void,String> {

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
                    Grupo g = new Grupo(object);
                    grupo.add(g);
                }
            } catch (Exception e) {

            }
            yacargado();
        }
    }

}
