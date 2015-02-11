package com.izv.android.actividadesinstituto;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;


public class Anadir extends Activity implements AdapterView.OnItemSelectedListener {

    private RadioGroup rg;
    private LinearLayout c, e, de;
    private int actividad=1;
    private ListView lvdepar;
    private String profesor, grupo, departamento, descripcion, lugar, fecha="", hora_inicio="", hora_fin="",
            fecha_salida="", fecha_llegada="", hora_salida="", hora_llegada="", lugar_salida, lugar_llegada;
    private EditText descrip, lu, lusalida, lullegada;
    private TextView profes, gru, depar;
    private TextView fe, hinicio, hfin, fesalida, fellegada, hsalida, hllegada;
    private ImageView iv;
    private Bitmap bitmap;
    private String id;
    private ArrayList<String> idprofesores, idgrupos;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                       METODOS ON                                           //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anadir);
        iv = (ImageView)findViewById(R.id.iv);
        iv.setVisibility(View.GONE);
        descrip=(EditText)findViewById(R.id.descripcion);
        lu=(EditText)findViewById(R.id.lugar);
        lusalida=(EditText)findViewById(R.id.lsalida);
        lullegada=(EditText)findViewById(R.id.lllegada);
        fe=(TextView)findViewById(R.id.fecha);
        hinicio=(TextView)findViewById(R.id.hinicio);
        hfin=(TextView)findViewById(R.id.hfin);
        fesalida=(TextView)findViewById(R.id.fsalida);
        fellegada=(TextView)findViewById(R.id.fllegada);
        hsalida=(TextView)findViewById(R.id.hsalida);
        hllegada=(TextView)findViewById(R.id.hllegada);
        profes=(TextView)findViewById(R.id.profes);
        gru=(TextView)findViewById(R.id.gru);
        depar=(TextView)findViewById(R.id.depar);
        c=(LinearLayout)findViewById(R.id.complementarias);
        e=(LinearLayout)findViewById(R.id.extraescolares);
        de=(LinearLayout)findViewById(R.id.de);
        de.setVisibility(View.GONE);
        e.setVisibility(View.GONE);
        rg=(RadioGroup)findViewById(R.id.grupo);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.com:
                        c.setVisibility(View.VISIBLE);
                        e.setVisibility(View.GONE);
                        actividad=1;
                        break;
                    case R.id.ex:
                        c.setVisibility(View.GONE);
                        e.setVisibility(View.VISIBLE);
                        actividad=2;
                        break;
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bitmap =savedInstanceState.getParcelable("foto");
        if (bitmap!=null){
            iv.setVisibility(View.VISIBLE);
            iv.setImageBitmap(bitmap);
        }
        fe.setText(savedInstanceState.getString("fecha"));
        fecha=savedInstanceState.getString("fecha");
        hinicio.setText(savedInstanceState.getString("horainicio"));
        hora_inicio=savedInstanceState.getString("horainicio");
        hfin.setText(savedInstanceState.getString("horafin"));
        hora_fin=savedInstanceState.getString("horafin");
        fesalida.setText(savedInstanceState.getString("fechasalida"));
        fecha_salida=savedInstanceState.getString("fechasalida");
        fellegada.setText(savedInstanceState.getString("fechallegada"));
        fecha_llegada=savedInstanceState.getString("fechallegada");
        hsalida.setText(savedInstanceState.getString("horasalida"));
        hora_salida=savedInstanceState.getString("horasalida");
        hllegada.setText(savedInstanceState.getString("horallegada"));
        hora_llegada=savedInstanceState.getString("horallegada");
    }

    @Override
    protected void onSaveInstanceState(Bundle savingInstanceState) {
        super.onSaveInstanceState(savingInstanceState);
        savingInstanceState.putParcelable("foto", bitmap);
        savingInstanceState.putString("fecha",fecha);
        savingInstanceState.putString("horainicio",hora_inicio);
        savingInstanceState.putString("horafin",hora_fin);
        savingInstanceState.putString("fechasalida",fecha_salida);
        savingInstanceState.putString("fechallegada",fecha_llegada);
        savingInstanceState.putString("horasalida",hora_salida);
        savingInstanceState.putString("horallegada",hora_llegada);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK && requestCode==1) {
            Uri selectedImage = data.getData();
            InputStream is;
            try {
                is = getContentResolver().openInputStream(selectedImage);
                BufferedInputStream bis = new BufferedInputStream(is);
                bitmap = BitmapFactory.decodeStream(bis);
                iv.setVisibility(View.VISIBLE);
                iv.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
            }
        }else {
            if (resultCode==RESULT_OK && requestCode==2) {
                profesor = data.getStringExtra("texto");
                idprofesores = data.getStringArrayListExtra("idprofesor");
                profes.setText(profesor);
            } else{
                if (resultCode==RESULT_OK && requestCode==3) {
                    grupo = data.getStringExtra("texto");
                    idgrupos = data.getStringArrayListExtra("idgrupos");
                    gru.setText(grupo);
                }
            }
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                  METODOS AUXILIARES                                        //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void guardarfoto(){
        if (bitmap!=null) {
            FileOutputStream salida;
            try {
                salida = new FileOutputStream(getExternalFilesDir(null) + "/id_" + id + ".jpg");
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, salida);
                salida.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        Toast.makeText(this, "Actividad guardada", Toast.LENGTH_SHORT).show();
        Intent i = new Intent();
        setResult(Activity.RESULT_OK, i);
        this.finish();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                     METODOS ONCLICK                                        //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void guardar(View view){
        descripcion=descrip.getText().toString();
        if (descripcion.compareTo("")!=0) {
            if (actividad == 1) {
                if (fe.getText().toString().compareTo("")==0 || lu.getText().toString().compareTo("")==0
                        ||hfin.getText().toString().compareTo("")==0 ||hinicio.getText().toString().compareTo("")==0
                        ||profes.getText().toString().compareTo("")==0 || gru.getText().toString().compareTo("")==0
                        || depar.getText().toString().compareTo("")==0) {
                    Toast.makeText(this, "Tienes que rellenar todos los datos", Toast.LENGTH_SHORT).show();
                } else {
                        lugar = lu.getText().toString();
                        Actividad ac = new Actividad();
                        ac.setId("0");
                        ac.setAlumno("zafra");
                        ac.setTipo("complementaria");
                        ac.setIdprofesor(idprofesores.get(0));
                        ac.setDescripcion(descripcion);
                        ac.setFechai(fecha + " " + hora_inicio+":0");
                        ac.setLugari(lugar);
                        ac.setLugarl(lugar);
                        ac.setFechaf(fecha + " " + hora_fin+":0");
                        PostRestfull post=new PostRestfull();
                        Parametros pp=new Parametros();
                        pp.url="http://ieszv.x10.bz/restful/api/actividad";
                        pp.jsonObject=ac.getJSON();
                        post.execute(pp);
                }
            } else {
                if(fesalida.getText().toString().compareTo("")==0 || fellegada.getText().toString().compareTo("")==0
                        || hsalida.getText().toString().compareTo("")==0 || hllegada.getText().toString().compareTo("")==0
                        || lusalida.getText().toString().compareTo("")==0 || lullegada.getText().toString().compareTo("")==0
                        ||profes.getText().toString().compareTo("")==0 || gru.getText().toString().compareTo("")==0
                        || depar.getText().toString().compareTo("")==0){
                    Toast.makeText(this,"Tienes que rellenar todos los datos",Toast.LENGTH_SHORT).show();
                }else{
                        lugar_salida=lusalida.getText().toString();
                        lugar_llegada=lullegada.getText().toString();
                        Actividad ac = new Actividad();
                        ac.setId("0");
                        ac.setAlumno("zafra");
                        ac.setTipo("extraescolar");
                        ac.setIdprofesor(idprofesores.get(0));
                        ac.setDescripcion(descripcion);
                        ac.setFechai(fecha_salida + " " + hora_salida);
                        ac.setLugari(lugar_salida);
                        ac.setLugarl(lugar_llegada);
                        ac.setFechaf(fecha_llegada + " " + hora_llegada);
                        PostRestfull post=new PostRestfull();
                        Parametros pp=new Parametros();
                        pp.url="http://ieszv.x10.bz/restful/api/actividad";
                        pp.jsonObject=ac.getJSON();
                        post.execute(pp);
                }
            }
        }else{
            Toast.makeText(this,"Tienes que rellenar todos los datos",Toast.LENGTH_SHORT).show();
        }
    }

    public void foto(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    public void profesores(View view){
        Intent intent=new Intent(this,AgregarProfesores.class);
        startActivityForResult(intent, 2);
    }


    public void grupos(View view){
        Intent intent=new Intent(this,AgregarGrupos.class);
        startActivityForResult(intent, 3);
    }


    public void departamentos(View view){
        de.setVisibility(View.VISIBLE);
        departamento="";
        depar.setText("");
        final ArrayList<String> lista=new ArrayList<String>();
        lista.add("Matematicas");
        lista.add("Lengua");
        lista.add("Ingles");
        lista.add("Religion");
        lista.add("Informatica");
        lista.add("Historia");
        Adaptadordialogo ad = new Adaptadordialogo(this,R.layout.itemdialogo,lista);
        lvdepar = (ListView)findViewById(R.id.lvdepartamentos);
        lvdepar.setAdapter(ad);
        lvdepar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox cb=(CheckBox)view.findViewById(R.id.cb);
                if (cb.isChecked()){
                    cb.setChecked(false);
                    int pos = departamento.indexOf(lista.get(i)+", ");
                    departamento=departamento.substring(0, pos) + departamento.substring(pos +
                            lista.size()+1, departamento.length());
                }else{
                    cb.setChecked(true);
                    departamento=departamento+lista.get(i)+", ";
                }
            }
        });
    }

    public void ocultardepartamento(View view){
        if (departamento.compareTo("")!=0) {
            departamento = departamento.substring(0, departamento.length() - 2);
            departamento = departamento + ".";
            depar.setText(departamento);
        }
        de.setVisibility(View.GONE);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                METODOS ONCLICK SOBRE TEXTVIEW PARA SACAR FECHAS Y HORAS                   //
    //                       Y CLASES NECESARIAS PARA OBTENER LOS DATOS                           //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void horainicio(View view){
        Date d=new Date();
        TimePickerDialog tpd = new TimePickerDialog(this,
                new ObtenerHoraInicio(), d.getHours(), d.getMinutes(), true);
        tpd.show();
    }

    public class ObtenerHoraInicio implements
            TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(TimePicker tp, int hour, int
                minute){
            hora_inicio=hour+":"+minute;
            hinicio.setText(hora_inicio);
        }
    }

    public void horafin(View view){
        Date d=new Date();
        TimePickerDialog tpd = new TimePickerDialog(this,
                new ObtenerHoraFin(), d.getHours(), d.getMinutes(), true);
        tpd.show();

    }
    public class ObtenerHoraFin implements
            TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(TimePicker tp, int hour, int
                minute){
            hora_fin=hour+":"+minute;
            hfin.setText(hora_fin);
        }
    }

    public void obtenerfecha(View view){
        Date d=new Date();
        DatePickerDialog dpd = new DatePickerDialog(this,
                new ObtenerFecha(), d.getYear()+1900, d.getMonth(), d.getDate());
        dpd.show();
    }

    public class ObtenerFecha implements
            DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year,
                              int monthOfYear,int dayOfMonth) {
            fecha=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
            fe.setText(fecha);
        }
    }

    public void fechasalida(View view){
        Date d=new Date();
        DatePickerDialog dpd = new DatePickerDialog(this,
                new ObtenerFechaSalida(),  d.getYear()+1900, d.getMonth(), d.getDate());
        dpd.show();
    }

    public class ObtenerFechaSalida implements
            DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year,
                              int monthOfYear,int dayOfMonth) {
            fecha_salida=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
            fesalida.setText(fecha_salida);
        }
    }

    public void fechallegada(View view){
        Date d=new Date();
        DatePickerDialog dpd = new DatePickerDialog(this,
                new ObtenerFechaLlegada(), d.getYear()+1900, d.getMonth(), d.getDate());
        dpd.show();
    }
    public class ObtenerFechaLlegada implements
            DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year,
                              int monthOfYear,int dayOfMonth) {
            fecha_llegada=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
            fellegada.setText(fecha_llegada);
        }
    }

    public void horasalida(View view){
        Date d=new Date();
        TimePickerDialog tpd = new TimePickerDialog(this,
                new ObtenerHoraSalida(), d.getHours(), d.getMinutes(), true);
        tpd.show();
    }
    public class ObtenerHoraSalida implements
            TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(TimePicker tp, int hour, int
                minute){
            hora_salida=hour+":"+minute;
            hsalida.setText(hora_salida);
        }
    }

    public void horallegada(View view){
        Date d=new Date();
        TimePickerDialog tpd = new TimePickerDialog(this,
                new ObtenerHoraLlegada(), d.getHours(), d.getMinutes(), true);
        tpd.show();
    }
    public class ObtenerHoraLlegada implements
            TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(TimePicker tp, int hour, int
                minute){
            hora_llegada=hour+":"+minute;
            hllegada.setText(hora_llegada);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                              METODOS CLIENTESRESTFULL                                      //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    class PostRestfull extends AsyncTask<Parametros,Void,String> {
        @Override
        protected String doInBackground(Parametros[] params) {
            id=ClienteRestFul.post(params[0].url,params[0].jsonObject);

            return id;
        }
        @Override
        protected void onPostExecute(String r) {
            super.onPostExecute(r);
            id=id.substring(5,id.length()-1);
            if (id.compareTo("0")==0){
                Toast.makeText(Anadir.this,"Esa actividad ya esta registrada",Toast.LENGTH_SHORT).show();
            }else {
                for (int i = 0; i < idprofesores.size(); i++) {
                    PostProfesoresYGrupos post = new PostProfesoresYGrupos();
                    Parametros pp = new Parametros();
                    ActividadProfesor ap = new ActividadProfesor();
                    ap.setId("0");
                    ap.setIdprofesor(idprofesores.get(i));
                    ap.setIdactividad(id);
                    Log.v("respuesta", ap.getJSON().toString());
                    pp.url = "http://ieszv.x10.bz/restful/api/actividadprofesor";
                    pp.jsonObject = ap.getJSON();
                    post.execute(pp);
                }
                for (int i = 0; i < idgrupos.size(); i++) {
                    PostProfesoresYGrupos post = new PostProfesoresYGrupos();
                    Parametros pp = new Parametros();
                    ActividadGrupo ag = new ActividadGrupo();
                    ag.setId("0");
                    ag.setIdgrupo(idgrupos.get(i));
                    ag.setIdactividad(id);
                    Log.v("respuesta", ag.getJSON().toString());
                    pp.url = "http://ieszv.x10.bz/restful/api/actividadgrupo";
                    pp.jsonObject = ag.getJSON();
                    post.execute(pp);
                }
                guardarfoto();
            }
        }
    }

    class PostProfesoresYGrupos extends AsyncTask<Parametros,Void,String> {

        @Override
        protected String doInBackground(Parametros[] params) {
            id=ClienteRestFul.post(params[0].url,params[0].jsonObject);
            return id;
        }


        @Override
        protected void onPostExecute(String r) {
            super.onPostExecute(r);
        }
    }

}