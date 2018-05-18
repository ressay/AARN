package com.pre.omc.aarn;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ressay on 11/05/18.
 */

public class Predictor
{
    MultiLayerNetwork net;
    public Predictor(Resources resources)
    {
        InputStream is = resources.openRawResource(R.raw.tempmodel);
        net = null;
        try {
            net = ModelSerializer.restoreMultiLayerNetwork(is);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("appYass", "Predictor: "+e.getMessage() );
        }
        if(net == null)
            Log.e("appYass", "Predictor: net = null" );
    }

    public int predictClass(double[] vectorDouble)
    {
        INDArray array = predict(vectorDouble);
        double max = -1;
        int maxIndex = 0;
        for (int i = 0; i < array.columns(); i++) {
//            Log.e("appYass",""+array.getDouble(i));
            if(max < array.getDouble(i))
            {
                max = array.getDouble(i);
                maxIndex = i;
            }
        }
        return maxIndex+1;
    }

    public INDArray predict(double[] vectorDouble)
    {
//        double[] vectorDouble = new double[]{54.26387995406979,71.4667761378817,-64.807708780882,76.8956347751343,42.462499897063,-72.78054518675891,36.6212291601289,81.6805569287795,-52.91927237269711,85.2322638852917,67.7492195028673,-73.684130041833,59.1885757027887,10.6789364098231,-71.2977813147725,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
        INDArray rowVector = Nd4j.create(vectorDouble);
        Log.e("appYass",""+net.output(rowVector));
        return net.output(rowVector);
    }

}
