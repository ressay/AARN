package com.pre.omc.aarn;

import android.content.res.Resources;
import android.os.AsyncTask;

/**
 * Created by ressay on 11/05/18.
 */

public class PredictorLoader extends AsyncTask<Resources,String,Predictor>
{
    @Override
    protected Predictor doInBackground(Resources... resources) {
        Resources resource = resources[0];
        Predictor predictor = new Predictor(resource);
//        predictor.predict();
        return predictor;
    }
}
