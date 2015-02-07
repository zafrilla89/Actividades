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


public class AgregarGrupos extends Activity {

    private ArrayList<String> lista;
    private ArrayList<Grupo> grupos;
    private String[] idgrupos;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                       METODOS ON                                           //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregargrupos);
        lista = new ArrayList<String>();
        grupos = new ArrayList<Grupo>();
        GetSacarGrupos getSacarGrupos = new GetSacarGrupos();
        getSacarGrupos.execute("http://ieszv.x10.bz/restful/api/grupo");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                 METODOS AUXILIARES                                         //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void yacargado() {
        sacarProfesores();
        idgrupos=new String[lista.size()];
        Adaptadordialogo ad = new Adaptadordialogo(this, R.layout.itemdialogo, lista);
        ListView lvprofe = (ListView) findViewById(R.id.lvlistagrupos);
        lvprofe.setAdapter(ad);
        lvprofe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox cb = (CheckBox) view.findViewById(R.id.cb);
                if (cb.isChecked()) {
                    cb.setChecked(false);
                    idgrupos[i]=null;
                } else {
                    cb.setChecked(true);
                    idgrupos[i] = grupos.get(i).getId();
                }
            }
        });

    }

    private void sacarProfesores() {
        for (int c = 0; c < grupos.size(); c++) {
            Grupo g = grupos.get(c);
            lista.add(g.getGrupo());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                     METODOS ONCLICK                                        //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void agregarprofesores(View view) {
        ArrayList <String> ids=new ArrayList<String>();
        for (int i=0;i<idgrupos.length;i++){
            if (idgrupos[i]!=null){
                ids.add(idgrupos[i]);
            }
        }
        Intent i = new Intent();
        if (ids.size()!=0){
            i.putExtra("texto","Grupos seleccionados");
        }else{
            i.putExtra("texto","");
        }
        i.putExtra("idgrupos",ids);
        setResult(Activity.RESULT_OK, i);
        this.finish();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                              METODOS CLIENTESRESTFULL                                      //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    class GetSacarGrupos extends AsyncTask<String, Void, String> {

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
                    grupos.add(g);
                }
            } catch (Exception e) {

            }
            yacargado();
        }
    }
}


