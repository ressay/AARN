package revo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.pre.omc.aarn.R;
import com.pre.omc.aarn.ScrollingActivity;

import java.io.IOException;
import java.net.InetAddress;

public class MainActivity extends Activity {

	EditText editTextName;
	Button buttonConnect, strtServer;
    static String nickName = "tester";
    static InetAddress broadCastAd = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        try {
            broadCastAd = NetworkManager.getBroadcastAddress(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        editTextName = (EditText) findViewById(R.id.NameEditText);
		buttonConnect = (Button) findViewById(R.id.connectButton);
		strtServer = (Button) findViewById(R.id.serve);
//        strtServer.setEnabled(false);
//        buttonConnect.setEnabled(false);
		final MainActivity act = this;
		strtServer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
                Intent intent;
                intent = new Intent(act, ScrollingActivity.class);
                startActivity(intent);
			}
		});

		buttonConnect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				Intent intent;
                intent = new Intent(act, ScanActivity.class);
				startActivity(intent);
			}
		});

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(editTextName.getText().length() > 0)
                {
                    buttonConnect.setEnabled(true);
                    strtServer.setEnabled(true);
                }
                else
                {
//                    buttonConnect.setEnabled(false);
//                    strtServer.setEnabled(false);
                }
                setNickName(editTextName.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });


	}

    public static String getNickName()
    {
        return nickName;
    }

    public static void setNickName(String nickName)
    {
        MainActivity.nickName = nickName;
    }
}
