package com.izv.android.actividadesinstituto;

/**
 * Created by ZAFRA on 07/02/2015.
 */

import android.os.AsyncTask;

public class PostRestfull extends AsyncTask<Parametros,Void,String> {

    @Override
    protected String doInBackground(Parametros[] params) {
        String r=ClienteRestFul.post(params[0].url,params[0].jsonObject);
        return r;
    }


    @Override
    protected void onPostExecute(String r) {
        super.onPostExecute(r);
    }
}
