package revo;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pre.omc.aarn.R;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by ressay on 06/06/17.
 */

public class Scanner extends AsyncTask<Void, String, Void> {

    ListView list;
    LinkedList<String> servers = new LinkedList<>();
    NetworkScanHost net = new NetworkScanHost();
    Activity act;
    NetworkScanHost.Host selectedHost = null;
    Scanner(ListView list, Activity act)
    {
        this.list = list;
        this.act = act;
    }

    public void selectHost(NetworkScanHost.Host host)
    {
        selectedHost = host;
    }

    @Override
    protected Void doInBackground(Void... lists)
    {
        net.listenToHosts();
        while (true)
        {
            if(selectedHost != null)
            {
                //net.sendMessageToHost("connect:"+MainActivity.getNickName(),selectedHost);
                cancel(true);
                net.cancel();
                return null;
            }
            if(isCancelled()) {
                net.cancel();
                return null;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(net.isHostsDirty()) {
                publishProgress();
                net.setHostsDirty(false);
            }
        }
    }


    @Override
    protected void onProgressUpdate(String... progress)
    {
        NetworkScanHost.Host[] hosts = new NetworkScanHost.Host[net.getHosts().size()];
        Iterator<NetworkScanHost.Host> iterator = net.getHosts().values().iterator();
        for(int i=0;iterator.hasNext();i++)
            hosts[i] = iterator.next();
        Log.d("ClientActivity","publishing: ");
        ArrayAdapter adapter = new ArrayAdapter<NetworkScanHost.Host>(act,
                R.layout.activity_listview, hosts);
        list.setAdapter(adapter);
    }


    @Override
    protected void onPostExecute(Void result)
    {
        super.onPostExecute(result);
    }
}
