package com.izv.android.actividadesinstituto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class Principal extends Activity {

    private ListView lvfecha, lvgrupos;
    private GridView lvactividadfecha, lvactividadgrupo;
    private Adaptador aactividadfecha, aactividadgrupo;
    private Adaptadorfeygru afecha, agrupo;
    private boolean dosFragmentos;
    private ArrayList<Actividad> datos, datosfecha, datosgrupos;
    private ArrayList<Grupo> gru;
    private ArrayList<String> fechas, grupos ;
    private ArrayList<ActividadGrupo> actividadGrupos;
    private ArrayList<Bitmap> fotos;
    private String estoy="";
    private int index;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                       METODOS ON                                           //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menucontextual,menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        int id=item.getItemId();
        AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        index=info.position;
        if (id == R.id.m_editar) {
            Actividad a;
            if (estoy=="fechas"){
                a=datosfecha.get(index);
            }else{
                a=datosgrupos.get(index);
            }
            Intent in = new Intent(Principal.this,Modificar.class);
            in.putExtra("id",a.getId());
            in.putExtra("tipo",a.getTipo());
            in.putExtra("idprofesor",a.getIdprofesor());
            in.putExtra("fechai",a.getFechai());
            in.putExtra("fechaf",a.getFechaf());
            in.putExtra("lugari",a.getLugari());
            in.putExtra("lugarf",a.getLugarl());
            in.putExtra("descripcion",a.getDescripcion());
            in.putExtra("alumno",a.getAlumno());
            startActivityForResult(in, 1);
        }else {
            if (id == R.id.m_borrar) {
                datos=new ArrayList<Actividad>();
                fechas= new ArrayList<String>();
                grupos=new ArrayList<String>();
                gru = new ArrayList<Grupo>();
                actividadGrupos=new ArrayList<ActividadGrupo>();
                if (estoy.compareTo("fechas")==0) {
                    File f=new File(getExternalFilesDir(null)+"/id_"+datosfecha.get(index).getId()+".jpg");
                    f.delete();
                    DeleteRestfull de = new DeleteRestfull();
                    de.execute("http://ieszv.x10.bz/restful/api/actividad/" + datosfecha.get(index).getId());
                    datosfecha.remove(index);
                    fotos.remove(index);
                    aactividadfecha.notifyDataSetChanged();
                }else{
                    File f=new File(getExternalFilesDir(null)+"/id_"+datosgrupos.get(index).getId()+".jpg");
                    f.delete();
                    DeleteRestfull de = new DeleteRestfull();
                    de.execute("http://ieszv.x10.bz/restful/api/actividad/" + datosgrupos.get(index).getId());
                    datosgrupos.remove(index);
                    fotos.remove(index);
                    aactividadgrupo.notifyDataSetChanged();
                }
                GetSacarActividades get= new GetSacarActividades();
                get.execute("http://ieszv.x10.bz/restful/api/actividad/zafra");
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
        lvactividadgrupo = (GridView) findViewById(R.id.lvactividadesgrupo);
        lvactividadfecha = (GridView) findViewById(R.id.lvactividadesfecha);
        datos = new ArrayList<Actividad>();
        fechas = new ArrayList<String>();
        grupos = new ArrayList<String>();
        gru = new ArrayList<Grupo>();
        actividadGrupos=new ArrayList<ActividadGrupo>();
        afecha=new Adaptadorfeygru(this,R.layout.item,fechas);
        agrupo=new Adaptadorfeygru(this,R.layout.item,grupos);
        GetSacarActividades get= new GetSacarActividades();
        get.execute("http://ieszv.x10.bz/restful/api/actividad/zafra");
        TabHost tabs = (TabHost) findViewById(R.id.tabHost);
        tabs.setup();
        TabHost.TabSpec spec = tabs.newTabSpec("");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Actividades");
        tabs.addTab(spec);
        spec = tabs.newTabSpec("");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Grupos");
        tabs.addTab(spec);
        tabs.setCurrentTab(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK) {
            datos=new ArrayList<Actividad>();
            fechas= new ArrayList<String>();
            grupos=new ArrayList<String>();
            gru = new ArrayList<Grupo>();
            actividadGrupos=new ArrayList<ActividadGrupo>();
            if (datosfecha!=null) {
                int x=datosfecha.size();
                for (int i = 0; i < x; i++) {
                    datosfecha.remove(datosfecha.size()-1);
                }
            }
            if (datosgrupos!=null) {
                int x=datosgrupos.size();
                for (int i = 0; i < x; i++) {
                    datosgrupos.remove(datosgrupos.size()-1);
                }
            }
            if (aactividadfecha!=null){
                aactividadfecha.notifyDataSetChanged();
            }
            if (aactividadgrupo!=null){
                aactividadgrupo.notifyDataSetChanged();
            }
            GetSacarActividades get= new GetSacarActividades();
            get.execute("http://ieszv.x10.bz/restful/api/actividad/zafra");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.barra_accion, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mas:
                Intent i= new Intent(this, Anadir.class);
                startActivityForResult(i, 1);
                return true;
        }
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                 METODOS AUXILIARES                                         //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void yacargadosdatos() {
        sacarfecha();
        sacargrupo();
        afecha=new Adaptadorfeygru(this,R.layout.item,fechas);
        agrupo=new Adaptadorfeygru(this,R.layout.item,grupos);
        lvfecha=(ListView)findViewById(R.id.lvfecha);
        lvgrupos=(ListView)findViewById(R.id.lvgrupos);
        lvfecha.setAdapter(afecha);
        lvgrupos.setAdapter(agrupo);
        if (findViewById(R.id.ffechavista) != null) {
            dosFragmentos = true;
        }
        if (dosFragmentos==true){
            lvfecha.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String f=fechas.get(i);
                    estoy="fechas";
                    datosfecha=new ArrayList<Actividad>();
                    sacaractividadfecha(f);
                    fotos=cargarfotos(datosfecha);
                    aactividadfecha = new Adaptador(Principal.this, R.layout.itemlista, datosfecha,fotos);
                    lvactividadfecha.setAdapter(aactividadfecha);
                    lvactividadfecha.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Actividad a=datosfecha.get(i);
                            Intent in = new Intent(Principal.this,Detalle.class);
                            in.putExtra("id",a.getId());
                            in.putExtra("tipo",a.getTipo());
                            in.putExtra("idprofesor",a.getIdprofesor());
                            in.putExtra("fechai",a.getFechai());
                            in.putExtra("fechaf",a.getFechaf());
                            in.putExtra("lugari",a.getLugari());
                            in.putExtra("lugarf",a.getLugarl());
                            in.putExtra("descripcion",a.getDescripcion());
                            in.putExtra("alumno",a.getAlumno());
                            startActivityForResult(in, 1);
                        }
                    });
                    registerForContextMenu(lvactividadfecha);
                }
            });
            lvgrupos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String g=grupos.get(i);
                    estoy="grupos";
                    datosgrupos=new ArrayList<Actividad>();
                    sacaractividadgrupo(g);
                    fotos=cargarfotos(datosgrupos);
                    aactividadgrupo = new Adaptador(Principal.this, R.layout.itemlista, datosgrupos,fotos);
                    lvactividadgrupo.setAdapter(aactividadgrupo);
                    lvactividadgrupo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Actividad a=datosgrupos.get(i);
                            Intent in = new Intent(Principal.this,Detalle.class);
                            in.putExtra("id",a.getId());
                            in.putExtra("tipo",a.getTipo());
                            in.putExtra("idprofesor",a.getIdprofesor());
                            in.putExtra("fechai",a.getFechai());
                            in.putExtra("fechaf",a.getFechaf());
                            in.putExtra("lugari",a.getLugari());
                            in.putExtra("lugarf",a.getLugarl());
                            in.putExtra("descripcion",a.getDescripcion());
                            in.putExtra("alumno",a.getAlumno());
                            startActivityForResult(in, 1);
                        }
                    });
                    registerForContextMenu(lvactividadgrupo);
                }
            });
        }else{
            lvfecha.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String f = fechas.get(i);
                    Intent in=new Intent(Principal.this,Secundariafechas.class);
                    in.putExtra("fechas",f);
                    startActivity(in);
                }
            });
            lvgrupos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String g = grupos.get(i);
                    Intent in=new Intent(Principal.this,Secundariagrupo.class);
                    in.putExtra("grupos",g);
                    startActivity(in);
                }
            });
        }
    }
    public void sacarfecha(){
        for (int i=0;i<datos.size();i++){
            Actividad a = new Actividad();
            a=datos.get(i);
            Boolean esta=false;
            String fecha=a.getFechai().substring(0,10);
            for (int c=0;c<fechas.size();c++){
                    String fe=fechas.get(c);
                    if (fecha.compareTo(fe)==0){
                        esta=true;
                    }
                }
                if (esta==false){
                    fechas.add(fecha);
                }
            }
        SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Date> dates= new ArrayList<Date>();
        for (int i=0;i<fechas.size();i++){
            String fecha=fechas.get(i);
            Date fe=new Date();
            try {
                fe = formatoDeFecha.parse(fecha);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            dates.add(fe);
        }
        Collections.sort(dates);
        fechas=new ArrayList<String>();
        for (int i=0;i<dates.size();i++){
            boolean esta=true;
            String fecha;
            Date fe=dates.get(i);
            fecha = formatoDeFecha.format(fe);
            for (int c=0;c<fechas.size();c++){
                String fec=fechas.get(c);
                if (fec.compareTo(fecha)==0){
                    esta=true;
                }
            }
            if (esta==true) {
                fechas.add(fecha);
            }
        }
    }

    public void sacargrupo(){
        for (int i=0;i<gru.size();i++){
        grupos.add(gru.get(i).getGrupo());
        }
    }



    public void sacaractividadfecha(String f) {
        for (int i = 0; i < datos.size(); i++) {
            Actividad a = new Actividad();
            a = datos.get(i);
            String fecha=a.getFechai().substring(0,10);
                if (f.compareTo(fecha)==0){
                    datosfecha.add(a);
                }
        }
    }


    public void sacaractividadgrupo(String g) {
        for (int v=0;v<gru.size();v++) {
            if (gru.get(v).getGrupo().compareTo(g) == 0) {
                for (int i = 0; i < actividadGrupos.size(); i++) {
                    ActividadGrupo ac = actividadGrupos.get(i);
                    if (ac.getIdgrupo().compareTo(gru.get(v).getId()) == 0) {
                        for (int x = 0; x < datos.size(); x++) {
                            Actividad a = new Actividad();
                            a = datos.get(x);
                            if (ac.getIdactividad().compareTo(a.getId()) == 0) {
                                datosgrupos.add(a);
                            }
                        }
                    }
                }
            }
        }
    }

    public ArrayList<Bitmap> cargarfotos(ArrayList<Actividad> ac){
        File archivo = new File(String.valueOf(getExternalFilesDir(null)));
        Bitmap myBitmap;
        ArrayList<Bitmap> lis= new ArrayList<Bitmap>();
        for (int i=0;i<ac.size();i++) {
            myBitmap = BitmapFactory.decodeFile(archivo.getAbsolutePath() + "/id_" + ac.get(i).getId()+".jpg");
            lis.add(myBitmap);
        }
        return lis;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                              METODOS CLIENTESRESTFULL                                      //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    class GetSacarActividades extends AsyncTask<String,Void,String> {

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
                    Actividad a = new Actividad(object);
                    datos.add(a);
                }
            } catch (Exception e) {

            }
            GetSacarGrupos get = new GetSacarGrupos();
            get.execute("http://ieszv.x10.bz/restful/api/grupo");
        }
    }

    class GetSacarGrupos extends AsyncTask<String,Void,String> {

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
            GetSacarActividadedgrupos get = new GetSacarActividadedgrupos();
            get.execute("http://ieszv.x10.bz/restful/api/actividadgrupo");
        }
    }

    class GetSacarActividadedgrupos extends AsyncTask<String,Void,String> {

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
            yacargadosdatos();
            }
        }

}



