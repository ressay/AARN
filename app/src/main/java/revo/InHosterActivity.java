package revo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pre.omc.aarn.GesturesConverter;
import com.pre.omc.aarn.GesturesFactory;
import com.pre.omc.aarn.Predictor;
import com.pre.omc.aarn.R;

import java.net.InetAddress;

public class InHosterActivity extends AppCompatActivity {

    ListView myList;
    TextView hostName;
    NetworkScanHost.Host host = null;
    String[] players = new String[0];
    Activity act = this;
    NetworkManager net = NetworkManager.getInstance();
    HostListener listener;
    TextView tV;
    int[] drawables = new int[5];
    ImageView[] updatableViews = new ImageView[4];
    GesturesFactory factory = new GesturesFactory();
    Predictor predictor;
    GesturesConverter converter = new GesturesConverter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_hoster);
        initDrawables();
        myList = (ListView) findViewById(R.id.list);
        hostName = (TextView) findViewById(R.id.hostName);
        tV = (TextView) findViewById(R.id.tV);
        tV.setText("");
        host = ScanActivity.host;
        hostName.setText(host.getName());


        Button reset=(Button) findViewById(R.id.resetH);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tV.setText("");
            }
        });
        predictor = new Predictor(getResources());


    }


    protected void initDrawables()
    {
        drawables[0] = R.drawable.pointer;
        drawables[1] = R.drawable.two;
        drawables[2] = R.drawable.flat;
        drawables[3] = R.drawable.fist;
        drawables[4] = R.drawable.grab;

        updatableViews[0] = (ImageView)findViewById(R.id.imgg6);
        updatableViews[1] = (ImageView)findViewById(R.id.imgg7);
        updatableViews[2] = (ImageView)findViewById(R.id.imgg8);
        updatableViews[3] = (ImageView)findViewById(R.id.imgg9);
        for (int i = 0; i < 4; i++) {
            updatableViews[i].setAlpha((float)0);
        }
    }

    protected void setmLayoutContent(int[] content)
    {
//        mLayout.removeAllViews();
        for (int i = 0; i < content.length; i++) {
            if(content[i] > 0)
                setImage(drawables[content[i]-1],i);
            else
                setImage(-1,i);
        }
    }

    protected void setImage(int drawable,int index)
    {
        if(drawable > 0) {
            updatableViews[index].setAlpha((float) 1);
            updatableViews[index].setImageDrawable(getResources().getDrawable(drawable, null));
        }
        else
            updatableViews[index].setAlpha((float) 0);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        net.sendMessageToHost("connect:"+MainActivity.getNickName(),host);
        listener = new HostListener();
        listener.execute();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        listener.cancel(true);
        net.sendMessage("disconnect:"+MainActivity.getNickName(),NetworkScanHost.players_port,host.getAddress());

    }

    @Override
    protected void onStop()
    {
        super.onStop();
        listener.cancel(true);
        net.sendMessage("disconnect:"+MainActivity.getNickName(),NetworkScanHost.players_port,host.getAddress());
    }




    class HostListener extends AsyncTask<Void, String, Void>
    {
        boolean end = false;
        String text = "";
        void endActivity()
        {
            end = true;
        }

        NetworkManager.ListenerCallBack action = null;
        NetworkManager.DListenerCallBack dAction = null;
        @Override
        protected Void doInBackground(Void... params)
        {

            action = net.listen(NetworkManager.servers_port, new NetworkManager.ListenerCallBack() {
                @Override
                public void listeningAction(String receivedMessage, InetAddress sender) {

                    String[] ps = new String[0];
                    if(receivedMessage.matches("players:((.|\n)*)")) {
                        ps = NetworkScanHost.parsePlayers(receivedMessage);
                        if (!compare(ps, players)) {
                            players = ps;
//                            publishProgress();
                        }
                    }
                    else if(receivedMessage.matches("disconnect:(.*)") && host.getAddress().getHostAddress().equals(sender.getHostAddress())) {
                        endActivity();
                    }
                    else if(!receivedMessage.matches("connect:(.*)"))
                    {
                        Log.e("ClientA",": "+receivedMessage);
                        int gesture = predictor.predictClass(factory.getGesture(Integer.parseInt(receivedMessage)-1));
                        String s = converter.addGesture(gesture);
                        if(s != null) {
                            text += s;
                        }
                        publishProgress();


                    }
                }
            });

            dAction = net.listenD(NetworkManager.servers_Dport, new NetworkManager.DListenerCallBack() {
                @Override
                public void listeningAction(double[] receivedMessage, InetAddress sender) {
                    Log.e("ClientA","receiving : ");
                    int gesture = predictor.predictClass(receivedMessage);
                    String s = converter.addGesture(gesture);
                    if(s != null) {
                        text += s;
                    }
                    publishProgress();
                }
            });

            while(true)
            {
                if(isCancelled() || end)
                {
                    net.cancelActionInPort(NetworkManager.servers_port,action);
                    return null;
                }

            }

        }

        boolean compare(String[] p1, String[] p2)
        {
            if(p1.length != p2.length) return false;
            for(int i=0;i<p1.length;i++)
                if(!p1[i].equals(p2[i]))
                    return false;
            return true;
        }
        @Override
        protected void onProgressUpdate(String... progress)
        {
//            ArrayAdapter adapter = new ArrayAdapter<String>(act,
//                    R.layout.activity_listview, players);
//            myList.setAdapter(adapter);
            tV.setText(text);
            setmLayoutContent(converter.currentGesture);
        }


        @Override
        protected void onPostExecute(Void result)
        {
            if(end) {
                Toast.makeText(act, "host ended!", Toast.LENGTH_SHORT).show();
                act.finish();
            }
            super.onPostExecute(result);
        }

    }

}
