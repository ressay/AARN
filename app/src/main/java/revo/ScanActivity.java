package revo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.pre.omc.aarn.R;
import com.pre.omc.aarn.ScrollingActivity;

public class ScanActivity extends AppCompatActivity {

    String[] mobileArray = {};
    ListView myList;
    Button scan;
    Scanner scanner = null;
    final ScanActivity act = this;
    static NetworkScanHost.Host host = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        myList = (ListView) findViewById(R.id.list);
        scan = (Button) findViewById(R.id.scanner);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NetworkScanHost.Host s = (NetworkScanHost.Host) parent.getAdapter().getItem(position);
                if (scanner != null)
                    scanner.selectHost(s);
                host = s;
                Intent intent;
                intent = new Intent(act, InHosterActivity.class);
                startActivity(intent);
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        scanner = new Scanner(myList, this);
        scanner.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (scanner != null)
            scanner.cancel(true);
        myList.setAdapter(new ArrayAdapter<Player>(this,
                R.layout.activity_listview, new Player[0]));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (scanner != null)
            scanner.cancel(true);
        myList.setAdapter(new ArrayAdapter<Player>(this,
                R.layout.activity_listview, new Player[0]));
    }
}