package com.izv.android.actividadesinstituto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ZAFRA on 13/12/2014.
 */
public class Adaptadordialogo extends ArrayAdapter<String> {
    private Context contexto;
    private ArrayList<String> lista;
    private int recurso;
    static LayoutInflater i;

    @Override
    public boolean isEnabled(int position){
        return true;
    }

    public Adaptadordialogo(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        this.contexto=context;
        this.lista=objects;
        this.recurso=resource;
        this.i=(LayoutInflater)contexto.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder{
        public CheckBox cb;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.cb=(CheckBox)convertView.findViewById(R.id.cb);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        String elemento;
        elemento=lista.get(position);
        vh.cb.setText(elemento);
        return convertView;
    }

}
