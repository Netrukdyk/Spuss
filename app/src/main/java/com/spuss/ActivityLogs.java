package com.spuss;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivityLogs extends AppCompatActivity {

    LinearLayout logLayout;
    LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        logLayout = (LinearLayout) findViewById(R.id.logLayout);
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        insertLog(1, "Bedroom");
        insertLog(2, "Bedroom");
        insertLog(3, "Bedroom");
        insertLog(4, "Bedroom");
        insertLog(5, "");
        insertLog(1, "Living room");
        insertLog(1, "Bedroom");
        insertLog(2, "Kitchen");

    }

    private void insertLog(int logType, String logPlace){

        View myView = mInflater.inflate(R.layout.log, null);

        ImageView icon = (ImageView) myView.findViewById(R.id.icon);
        icon.setImageResource(Spuss.logTypesIcon[logType - 1]);

        TextView type = (TextView) myView.findViewById(R.id.activityType);
        type.setText(Spuss.logTypesText[logType-1]);
        if(logType == 3) type.setTextColor(Color.RED);
        if(logType == 2) type.setTextColor(Color.parseColor("#FFA500"));


        TextView place = (TextView) myView.findViewById(R.id.activityPlace);
        place.setText(logPlace);
        TextView time = (TextView) myView.findViewById(R.id.activityTime);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        time.setText("Logged at: " + df.format(Calendar.getInstance().getTime()));
        logLayout.addView(myView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_logs, menu);
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
