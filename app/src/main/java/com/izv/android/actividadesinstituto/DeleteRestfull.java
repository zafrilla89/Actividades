package com.izv.android.actividadesinstituto;

import android.os.AsyncTask;

/**
 * Created by ZAFRA on 07/02/2015.
 */
public class DeleteRestfull extends AsyncTask<String,Void,String> {

    @Override
    protected String doInBackground(String[] params) {
        String r = ClienteRestFul.delete(params[0]);
        return r;
    }


    @Override
    protected void onPostExecute(String r) {
        super.onPostExecute(r);
    }
}

