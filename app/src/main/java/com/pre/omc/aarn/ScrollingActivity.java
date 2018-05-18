package com.pre.omc.aarn;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScrollingActivity extends AppCompatActivity {

    GridLayout mLayout;
    int[] drawables = new int[5];
    ImageView[] updatableViews = new ImageView[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        initDrawables();
        mLayout=(GridLayout) findViewById(R.id.linearUpdate);
//        mLayout.removeAllViews();
        final TextView textView=(TextView)findViewById(R.id.textView);
        Button reset=(Button) findViewById(R.id.reset);
        textView.setText("");
        ImageView[] btns = new ImageView[5];
        int[] resources = new int[5];
        resources[0] = R.id.img1;
        resources[1] = R.id.img2;
        resources[2] = R.id.img3;
        resources[3] = R.id.img4;
        resources[4] = R.id.img5;
        for (int i = 0; i < resources.length; i++) {
            btns[i] = (ImageView)findViewById(resources[i]);
        }
        final GesturesFactory factory = new GesturesFactory();
        final Predictor predictor = new Predictor(getResources());
        final GesturesConverter converter = new GesturesConverter();
        int count = 1;
        for(ImageView btn : btns)
        {
            final int cpt = count++;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int gesture = predictor.predictClass(factory.getGesture(cpt-1));
                    String s = converter.addGesture(gesture);
                    if(s != null)
                        textView.append(s);
                    setmLayoutContent(converter.currentGesture);
                }
            });
        }

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");
            }
        });

//        PredictorLoader loader = new PredictorLoader();
//        loader.execute(getResources());


//        for (int i = 0; i < 5; i++) {
//            textView.append(predictor.predictClass(factory.getGesture(i))+"");
//        }

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

    protected void addImageToLayout(int drawable)
    {
        ImageView imageView = new ImageView(mLayout.getContext());
        imageView.setImageDrawable(getResources().getDrawable(drawable,null));

//        imageView.getLayoutParams().height = 10;
        imageView.requestLayout();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30,30);
        imageView.setLayoutParams(layoutParams);
        mLayout.addView(imageView);

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

    protected void initDrawables()
    {
        drawables[0] = R.drawable.pointer;
        drawables[1] = R.drawable.two;
        drawables[2] = R.drawable.flat;
        drawables[3] = R.drawable.fist;
        drawables[4] = R.drawable.grab;

        updatableViews[0] = (ImageView)findViewById(R.id.img6);
        updatableViews[1] = (ImageView)findViewById(R.id.img7);
        updatableViews[2] = (ImageView)findViewById(R.id.img8);
        updatableViews[3] = (ImageView)findViewById(R.id.img9);
        for (int i = 0; i < 4; i++) {
            updatableViews[i].setAlpha((float)0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
