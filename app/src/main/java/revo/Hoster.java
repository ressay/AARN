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
 * Created by ressay on 09/06/17.
 */

public class Hoster extends AsyncTask<Void, Player, Void> {

    ListView list;
    LinkedList<Player> players = new LinkedList<>();
    Activity act;
    NetworkScanHost net = new NetworkScanHost();

    Hoster(ListView list, Activity act) {
        this.list = list;
        this.act = act;
    }


    @Override
    protected Void doInBackground(Void... lists) {

        net.listenToPlayers();
        while (true)
        {
            if(isCancelled())
            {
                net.cancel();
                net.sendMessageToPlayers("disconnect:"+MainActivity.getNickName());
                return null;
            }
            Log.d("ClientActivity","going to send message");
            net.sendMessageToPlayers("connect:"+MainActivity.getNickName());
            net.sendPlayersList();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(net.isPlayersDirty())
            {
                net.setPlayersDirty(false);
                publishProgress();
            }
        }

    }


    @Override
    protected void onProgressUpdate(Player... progress) {
        Player[] players = new Player[net.getPlayers().size()];
        Iterator<Player> iterator = net.getPlayers().values().iterator();
        for(int i=0;iterator.hasNext();i++)
            players[i] = iterator.next();
        Log.d("ClientActivity","publishing: ");
        ArrayAdapter adapter = new ArrayAdapter<Player>(act,
                R.layout.activity_listview, players);
        list.setAdapter(adapter);
    }


    @Override
    protected void onPostExecute(Void result) {

        super.onPostExecute(result);
    }


}