package com.izv.android.actividadesinstituto;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class Detalle extends Activity {

    private Actividad a;
    private ImageView im;
    private ArrayList<Grupo> gru;
    private ArrayList<Profesores> pro;
    private ArrayList<ActividadProfesor> actividadProfesor;
    private ArrayList<ActividadGrupo> actividadGrupos;
    private LinearLayout complementarias, extraescolares;
    private String grup="", prof="";
    private TextView profesores, grupos, departamentos, descripcion, fecha, lugar, horai, horaf, fechas, fechal, horas, horal, lugars, lugarl;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                       METODOS ON                                           //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle);
        gru=new ArrayList<Grupo>();
        pro=new ArrayList<Profesores>();
        actividadProfesor=new ArrayList<ActividadProfesor>();
        actividadGrupos=new ArrayList<ActividadGrupo>();
        complementarias = (LinearLayout) findViewById(R.id.complemen);
        extraescolares = (LinearLayout) findViewById(R.id.extraesco);
        im = (ImageView) findViewById(R.id.ivdetalle);
        profesores = (TextView) findViewById(R.id.tvprofes);
        grupos = (TextView) findViewById(R.id.tvgru);
        departamentos = (TextView) findViewById(R.id.tvdepar);
        descripcion = (TextView) findViewById(R.id.tvdescripcion);
        fecha = (TextView) findViewById(R.id.tvfecha);
        lugar = (TextView) findViewById(R.id.tvlugar);
        horai = (TextView) findViewById(R.id.tvhinicio);
        horaf = (TextView) findViewById(R.id.tvhfin);
        fechas = (TextView) findViewById(R.id.tvfsalida);
        fechal = (TextView) findViewById(R.id.tvfllegada);
        horas = (TextView) findViewById(R.id.tvhsalida);
        horal = (TextView) findViewById(R.id.tvhllegada);
        lugars = (TextView) findViewById(R.id.tvlsalida);
        lugarl = (TextView) findViewById(R.id.tvlllegada);
        a = new Actividad();
        Bundle b = getIntent().getExtras();
        if (b != null) {
            a.setId(b.getString("id"));
            a.setTipo(b.getString("tipo"));
            a.setIdprofesor(b.getString("idprofesor"));
            a.setFechai(b.getString("fechai"));
            a.setFechaf(b.getString("fechaf"));
            a.setLugari(b.getString("lugari"));
            a.setLugarl(b.getString("lugarf"));
            a.setDescripcion(b.getString("descripcion"));
            a.setAlumno(b.getString("alumno"));
        }
        GetSacarGrupos getSacarGrupos = new GetSacarGrupos();
        getSacarGrupos.execute("http://ieszv.x10.bz/restful/api/grupo");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                 METODOS AUXILIARES                                         //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void yadatoscargados(){
        sacargrupos();
        sacarprofesores();
        profesores.setText(prof);
        grupos.setText(grup);
        descripcion.setText(a.getDescripcion());
        if (a.getTipo().compareTo("complementaria")==0){
            complementarias.setVisibility(View.VISIBLE);
            horai.setText(a.getFechai().substring(10,a.getFechai().length()-3));
            horaf.setText(a.getFechaf().substring(10, a.getFechaf().length() - 3));
            fecha.setText(a.getFechai().substring(0, 10));
            lugar.setText(a.getLugari());
        }else {
            extraescolares.setVisibility(View.VISIBLE);
            fechas.setText(a.getFechai().substring(0, 10));
            fechal.setText(a.getFechaf().substring(0, 10));
            horas.setText(a.getFechai().substring(10, a.getFechai().length() - 3));
            horal.setText(a.getFechaf().substring(10, a.getFechaf().length() - 3));
            lugars.setText(a.getLugari());
            lugarl.setText(a.getLugarl());
        }
        File archivo = new File(String.valueOf(getExternalFilesDir(null)));
        Bitmap myBitmap;
        myBitmap = BitmapFactory.decodeFile(archivo.getAbsolutePath() + "/id_" + a.getId() + ".jpg");
        if (myBitmap==null){
            im.setImageDrawable(getResources().getDrawable(R.drawable.nofoto));
        }else {
            im.setImageBitmap(myBitmap);
        }
    }

    public void sacargrupos(){
        for (int x=0;x<actividadGrupos.size();x++){
            if (a.getId().compareTo(actividadGrupos.get(x).getIdactividad())==0){
                for (int i=0;i<gru.size();i++) {
                    if (actividadGrupos.get(x).getIdgrupo().compareTo(gru.get(i).getId())==0){
                        grup=grup+gru.get(i).getGrupo()+", ";
                    }
                }
            }
        }
        grup=grup.substring(0,grup.length()-2);
        grup=grup+".";
    }

    private void sacarprofesores(){
        for (int x=0;x<actividadProfesor.size();x++){
            if (a.getId().compareTo(actividadProfesor.get(x).getIdactividad())==0){
                for (int i=0;i<pro.size();i++) {
                    if (actividadProfesor.get(x).getIdprofesor().compareTo(pro.get(i).getId())==0){
                        prof=prof+pro.get(i).getNombre()+" "+ pro.get(i).getApellidos()+", ";
                    }
                }
            }
        }
        prof=prof.substring(0,prof.length()-2);
        prof=prof+".";
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
                    gru.add(g);
                }
            } catch (Exception e) {

            }
            GetSacarActividadesGrupos getSacarActividadesGrupos = new GetSacarActividadesGrupos();
            getSacarActividadesGrupos.execute("http://ieszv.x10.bz/restful/api/actividadgrupo");
        }
    }

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
            GetSacarProfesores getSacarProfesores = new GetSacarProfesores();
            getSacarProfesores.execute("http://ieszv.x10.bz/restful/api/profesor");
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
                    pro.add(pr);
                }
            } catch (Exception e) {

            }
            GetSacarActividadesProfesores getSacarActividadesProfesores = new GetSacarActividadesProfesores();
            getSacarActividadesProfesores.execute("http://ieszv.x10.bz/restful/api/actividadprofesor");
        }
    }

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
            yadatoscargados();
        }
    }

}
