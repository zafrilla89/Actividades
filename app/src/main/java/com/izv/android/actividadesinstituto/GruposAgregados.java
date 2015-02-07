package com.izv.android.actividadesinstituto;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;


public class GruposAgregados extends Activity {
    private ListView lvgrupos;
    private String id;
    private Adaptadorfeygru ad;
    private ArrayList<String> lista;
    private ArrayList<ActividadGrupo> actividadGrupos, actividadGruposmias;
    private ArrayList<Grupo> listagrupo;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                       METODOS ON                                           //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean onContextItemSelected(MenuItem item) {
        int id=item.getItemId();
        AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int index=info.position;
        if (id == R.id.m_borrar) {
            if (lista.size()<2){
                Toast.makeText(this, "No puedes borrar todos los profesores", Toast.LENGTH_SHORT).show();
            }else {
                DeleteRestfull deleteActividadRestfull = new DeleteRestfull();
                deleteActividadRestfull.execute("http://ieszv.x10.bz/restful/api/actividadgrupo/" + actividadGruposmias.get(index).getId());
                lista.remove(index);
                actividadGruposmias.remove(index);
                ad.notifyDataSetChanged();
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos_agregados);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            id = b.getString("id");
        }
        actividadGruposmias=new ArrayList<ActividadGrupo>();
        actividadGrupos=new ArrayList<ActividadGrupo>();
        lista=new ArrayList<String>();
        listagrupo=new ArrayList<Grupo>();
        GetSacarProfesores getSacarProfesores = new GetSacarProfesores();
        getSacarProfesores.execute("http://ieszv.x10.bz/restful/api/grupo");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.eliminar,menu);
    }

    @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK &&
                requestCode == 1) {
            actividadGrupos = new ArrayList<ActividadGrupo>();
            lista = new ArrayList<String>();
            actividadGruposmias = new ArrayList<ActividadGrupo>();
            GetSacarActividadesGrupos getSacarActividadesGrupos = new GetSacarActividadesGrupos();
            getSacarActividadesGrupos.execute("http://ieszv.x10.bz/restful/api/actividadgrupo");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                 METODOS AUXILIARES                                         //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void yacargado(){
        sacarGrupos();
        ad = new Adaptadorfeygru(this,R.layout.item,lista);
        lvgrupos = (ListView)findViewById(R.id.lvlistaprofesores);
        lvgrupos.setAdapter(ad);
        registerForContextMenu(lvgrupos);
    }

    private void sacarGrupos(){
        for (int i = 0; i < actividadGrupos.size(); i++) {
            ActividadGrupo ag = actividadGrupos.get(i);
            boolean esta=false;
            if (ag.getIdactividad().compareTo(id)==0) {
                for (int c = 0; c < listagrupo.size(); c++) {
                    Grupo g = listagrupo.get(c);
                    if (g.getId().compareTo(ag.getIdgrupo()) == 0) {
                        for (int x = 0; x < lista.size(); x++) {
                            if (lista.get(x).compareTo(g.getGrupo()) == 0) {
                                esta = true;
                            }
                        }
                        if (esta == false) {
                            lista.add(g.getGrupo());
                            actividadGruposmias.add(ag);
                        }
                    }
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                     METODOS ONCLICK                                        //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void agregar(View view){
        Intent in= new Intent(GruposAgregados.this, ListaGrupos.class);
        in.putExtra("id",id);
        startActivityForResult(in, 1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                              METODOS CLIENTESRESTFULL                                      //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    class GetSacarActividadesGrupos extends AsyncTask<String,Void,String> {

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
                    ActividadGrupo ag = new ActividadGrupo(object);
                    actividadGrupos.add(ag);
                }
            } catch (Exception e) {

            }
            yacargado();
        }
    }

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
                    listagrupo.add(g);
                    Log.v("SACAR GRUPOS", g.getId()+" "+g.getGrupo());
                }
            } catch (Exception e) {

            }
            GetSacarActividadesGrupos getSacarActividadesGrupos = new GetSacarActividadesGrupos();
            getSacarActividadesGrupos.execute("http://ieszv.x10.bz/restful/api/actividadgrupo");
        }
    }
}
