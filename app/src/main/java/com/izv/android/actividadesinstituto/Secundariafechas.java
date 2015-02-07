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


public class Secundariafechas extends Activity {

    private String fe;
    private ArrayList<Actividad> datos, datosfecha;
    private ArrayList<Bitmap> fotos;
    private Adaptador aactividadfecha;
    private ListView lv;
    private TextView tv;

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
            a=datosfecha.get(index);
            Intent in = new Intent(Secundariafechas.this,Modificar.class);
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
                de.execute("http://ieszv.x10.bz/restful/api/actividad/" + datosfecha.get(index).getId());
                File f=new File(getExternalFilesDir(null)+"/id_"+datosfecha.get(index).getId()+".jpg");
                f.delete();
                datosfecha.remove(index);
                fotos.remove(index);
                aactividadfecha.notifyDataSetChanged();
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secundariafechas);
        Bundle b = getIntent().getExtras();
        if(b !=null ) {
            fe = b.getString("fechas");
        }
        tv=(TextView)findViewById(R.id.tvtitulofecha);
        tv.setText(fe);
        datos=new ArrayList<Actividad>();
        datosfecha=new ArrayList<Actividad>();
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

    public void yacargadosdatos() {
        sacaractividadfecha(fe);
        lv=(ListView)findViewById(R.id.lisfechas);
        fotos=cargarfotos(datosfecha);
        aactividadfecha = new Adaptador(Secundariafechas.this, R.layout.itemlistamovil, datosfecha, fotos);
        lv.setAdapter(aactividadfecha);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Actividad a=datosfecha.get(i);
                Intent in = new Intent(Secundariafechas.this,Detalle.class);
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
            yacargadosdatos();
        }
    }



}
