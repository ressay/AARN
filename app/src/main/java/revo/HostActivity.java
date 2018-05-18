package revo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.pre.omc.aarn.R;

public class HostActivity extends AppCompatActivity {

    ListView myList;
    Button start;
    Hoster hoster = null;
    final HostActivity apt = this;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myList = (ListView) findViewById(R.id.list);
        start = (Button) findViewById(R.id.Start);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String s = parent.getAdapter().getItem(position).toString();
            }
        });

        start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


            }
        });


    }

    @Override
    public void onResume()
    {
        super.onResume();
        hoster = new Hoster(myList,this);
        hoster.execute();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(hoster != null)
            hoster.cancel(true);
        hoster = null;
        myList.setAdapter(new ArrayAdapter<Player>(this,
                R.layout.activity_listview, new Player[0]));
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if(hoster != null)
            hoster.cancel(true);
        hoster = null;
        myList.setAdapter(new ArrayAdapter<Player>(this,
                R.layout.activity_listview, new Player[0]));
    }

}
