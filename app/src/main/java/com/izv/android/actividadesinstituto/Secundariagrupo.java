package com.izv.android.actividadesinstituto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class Secundariagrupo extends Activity {

    private String gr;
    private ArrayList<Actividad> datos, datosgrupos;
    private Adaptador aactividadgrupo;
    private ListView lv;
    private TextView tv;
    private ArrayList<ActividadGrupo> actividadGrupos;
    private ArrayList<Grupo> gru;
    private ArrayList<Bitmap> fotos;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                       METODOS ON                                           //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK) {
            finish();
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        int id=item.getItemId();
        AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int index=info.position;
        if (id == R.id.m_editar) {
            Actividad a;
            a=datosgrupos.get(index);
            Intent in = new Intent(Secundariagrupo.this,Modificar.class);
            in.putExtra("id",a.getId());
            in.putExtra("tipo",a.getTipo());
            in.putExtra("idprofesor",a.getIdprofesor());
            in.putExtra("fechai",a.getFechai());
            in.putExtra("fechaf",a.getFechaf());
            in.putExtra("lugari",a.getLugari());
            in.putExtra("lugarf",a.getLugarl());
            in.putExtra("descripcion",a.getDescripcion());
            in.putExtra("alumno",a.getAlumno());
            startActivityForResult(in,1);
        }else {
            if (id == R.id.m_borrar) {
                DeleteRestfull de = new DeleteRestfull();
                de.execute("http://ieszv.x10.bz/restful/api/actividad/"+datosgrupos.get(index).getId());
                File f=new File(getExternalFilesDir(null)+"/id_"+datosgrupos.get(index).getId()+".jpg");
                f.delete();
                datosgrupos.remove(index);
                fotos.remove(index);
                aactividadgrupo.notifyDataSetChanged();
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secundariagrupo);
        Bundle b = getIntent().getExtras();
        if(b !=null ) {
            gr = b.getString("grupos");
        }
        tv=(TextView)findViewById(R.id.tvtitulogrupo);
        tv.setText(gr);
        datos=new ArrayList<Actividad>();
        datosgrupos=new ArrayList<Actividad>();
        gru=new ArrayList<Grupo>();
        actividadGrupos=new ArrayList<ActividadGrupo>();
        GetSacarActividades get= new GetSacarActividades();
        get.execute("http://ieszv.x10.bz/restful/api/actividad/zafra");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menucontextual,menu);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                 METODOS AUXILIARES                                         //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public ArrayList<Bitmap> cargarfotos(ArrayList<Actividad> ac){
        File archivo = new File(String.valueOf(getExternalFilesDir(null)));
        Bitmap myBitmap;
        ArrayList<Bitmap> lis= new ArrayList<Bitmap>();
        for (int i=0;i<ac.size();i++) {
            myBitmap = BitmapFactory.decodeFile(archivo.getAbsolutePath() + "/id_" + ac.get(i).getId() + ".jpg");
            lis.add(myBitmap);
        }
        return lis;
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

    public void yacargadosdatos() {
        sacaractividadgrupo(gr);
        lv=(ListView)findViewById(R.id.lisgrupos);
        fotos=cargarfotos(datosgrupos);
        aactividadgrupo = new Adaptador(Secundariagrupo.this, R.layout.itemlistamovil, datosgrupos,fotos);
        lv.setAdapter(aactividadgrupo);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Actividad a=datosgrupos.get(i);
                Intent in = new Intent(Secundariagrupo.this,Detalle.class);
                in.putExtra("id",a.getId());
                in.putExtra("tipo",a.getTipo());
                in.putExtra("idprofesor",a.getIdprofesor());
                in.putExtra("fechai",a.getFechai());
                in.putExtra("fechaf",a.getFechaf());
                in.putExtra("lugari",a.getLugari());
                in.putExtra("lugarf",a.getLugarl());
                in.putExtra("descripcion",a.getDescripcion());
                in.putExtra("alumno",a.getAlumno());
                startActivity(in);
            }
        });
        registerForContextMenu(lv);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                              METODOS CLIENTESRESTFULL                                      //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

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

}
