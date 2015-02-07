package com.izv.android.actividadesinstituto;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ZAFRA on 13/12/2014.
 */
public class Adaptador extends ArrayAdapter<Actividad> {

    private Context contexto;
    private ArrayList<Actividad> lista;
    private ArrayList<Bitmap> fotos;
    private int recurso;
    static LayoutInflater i;

    public Adaptador(Context context, int resource, ArrayList<Actividad> objects, ArrayList<Bitmap> f) {
        super(context, resource, objects);
        this.contexto=context;
        this.lista=objects;
        this.fotos=f;
        this.recurso=resource;
        this.i=(LayoutInflater)contexto.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder{
        public ImageView ivfoto;
        public TextView tv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.ivfoto = (ImageView) convertView.findViewById(R.id.ivlista);
            vh.tv = (TextView) convertView.findViewById(R.id.tvlista);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Actividad a;
        a = lista.get(position);
        if (fotos.get(position) == null) {
            vh.ivfoto.setImageDrawable(contexto.getResources().getDrawable(R.drawable.nofoto));
        } else {
            vh.ivfoto.setImageBitmap(fotos.get(position));
        }
        vh.tv.setText(a.getDescripcion());
        return convertView;
    }
}

