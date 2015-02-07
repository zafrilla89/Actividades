package com.izv.android.actividadesinstituto;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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


public class ProfesoresAgregados extends Activity {

    private ListView lvprofe;
    private String id;
    private Adaptadorfeygru ad;
    private ArrayList<String> lista;
    private ArrayList<ActividadProfesor> actividadProfesor, actividadProfesoresmias;
    private ArrayList<Profesores> listaprofesores;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                       METODOS ON                                           //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.eliminar,menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        int id=item.getItemId();
        AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int index=info.position;
            if (id == R.id.m_borrar) {
                if (lista.size()<2){
                    Toast.makeText(this,"No puedes borrar todos los profesores",Toast.LENGTH_SHORT).show();
                }else {
                    DeleteRestfull deleteActividadRestfull = new DeleteRestfull();
                    deleteActividadRestfull.execute("http://ieszv.x10.bz/restful/api/actividadprofesor/" + actividadProfesoresmias.get(index).getId());
                    lista.remove(index);
                    actividadProfesoresmias.remove(index);
                    ad.notifyDataSetChanged();
                }
            }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profesoresagregados);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            id = b.getString("id");
        }
        actividadProfesoresmias=new ArrayList<ActividadProfesor>();
        actividadProfesor=new ArrayList<ActividadProfesor>();
        lista=new ArrayList<String>();
        listaprofesores=new ArrayList<Profesores>();
        GetSacarProfesores getSacarProfesores = new GetSacarProfesores();
        getSacarProfesores.execute("http://ieszv.x10.bz/restful/api/profesor");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK &&
                requestCode == 1) {
            actividadProfesor = new ArrayList<ActividadProfesor>();
            lista = new ArrayList<String>();
            actividadProfesoresmias = new ArrayList<ActividadProfesor>();
            GetSacarActividadesProfesores getSacarActividadesProfesores = new GetSacarActividadesProfesores();
            getSacarActividadesProfesores.execute("http://ieszv.x10.bz/restful/api/actividadprofesor");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                 METODOS AUXILIARES                                         //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void yacargado(){
        sacarProfesores();
        ad = new Adaptadorfeygru(this,R.layout.item,lista);
        lvprofe = (ListView)findViewById(R.id.lveliminarprofesor);
        lvprofe.setAdapter(ad);
        registerForContextMenu(lvprofe);
    }

    private void sacarProfesores(){
        for (int i = 0; i < actividadProfesor.size(); i++) {
            ActividadProfesor ap = actividadProfesor.get(i);
            boolean esta=false;
            if (ap.getIdactividad().compareTo(id)==0) {
                for (int c = 0; c < listaprofesores.size(); c++) {
                    Profesores pr = listaprofesores.get(c);
                    if (pr.getId().compareTo(ap.getIdprofesor()) == 0) {
                        for (int x = 0; x < lista.size(); x++) {
                            if (lista.get(x).compareTo(pr.getNombre()) == 0) {
                                esta = true;
                            }
                        }
                        if (esta == false) {
                            lista.add(pr.getNombre() + " " + pr.getApellidos());
                            actividadProfesoresmias.add(ap);
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
        Intent in= new Intent(ProfesoresAgregados.this, Listaprofesor.class);
        in.putExtra("id",id);
        startActivityForResult(in, 1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                              METODOS CLIENTESRESTFULL                                      //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    class GetSacarActividadesProfesores extends AsyncTask<String,Void,String> {

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
                    ActividadProfesor ap = new ActividadProfesor(object);
                    actividadProfesor.add(ap);
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
                    Profesores pr = new Profesores(object);
                    listaprofesores.add(pr);
                }
            } catch (Exception e) {

            }
            GetSacarActividadesProfesores getSacarActividadesProfesores = new GetSacarActividadesProfesores();
            getSacarActividadesProfesores.execute("http://ieszv.x10.bz/restful/api/actividadprofesor");
        }
    }

 }
