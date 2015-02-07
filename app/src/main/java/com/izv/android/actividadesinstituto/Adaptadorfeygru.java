package com.izv.android.actividadesinstituto;

import android.content.Context;
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
public class Adaptadorfeygru  extends ArrayAdapter<String> {

    private Context contexto;
    private ArrayList<String> lista;
    private int recurso;
    static LayoutInflater i;

    public Adaptadorfeygru(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        this.contexto=context;
        this.lista=objects;
        this.recurso=resource;
        this.i=(LayoutInflater)contexto.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder{
        public TextView tv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.tv=(TextView)convertView.findViewById(R.id.tvfeygru);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        String a;
        a=lista.get(position);
        vh.tv.setText(a);
        return convertView;
    }
}
