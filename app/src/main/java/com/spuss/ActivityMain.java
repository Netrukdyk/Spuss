package com.spuss;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.animation.Animation;
        import android.view.animation.RotateAnimation;
        import android.widget.Button;
        import android.widget.CompoundButton;
        import android.widget.ImageButton;
        import android.widget.Switch;
        import android.widget.Toast;

public class ActivityMain extends AppCompatActivity implements View.OnClickListener{
    private DataUpdateReceiver dataUpdateReceiver;

    ImageButton btnArm, btnTemp;
    Switch switchPet, switchSilent;
    Button btnLogs;

    Boolean armed = false;
    Boolean pet = false;
    Boolean silent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnArm = (ImageButton) findViewById(R.id.btnArm);
        btnTemp = (ImageButton) findViewById(R.id.btnTemp);
        switchPet = (Switch) findViewById(R.id.switchPet);
        switchSilent = (Switch) findViewById(R.id.switchSilent);
        btnLogs = (Button) findViewById(R.id.btnLogs);

        btnArm.setOnClickListener(this);
        btnTemp.setOnClickListener(this);
        btnLogs.setOnClickListener(this);

        switchPet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pet = isChecked;
            }
        });

        switchSilent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                silent = isChecked;
            }
        });

        // TODO Get startup values from settings
        btnArm.setImageResource((armed) ? R.drawable.main1 : R.drawable.main0);
        armAnimation();
        switchPet.setChecked(pet);
        switchSilent.setChecked(silent);


        sendBroadcast(new Intent(SpussService.MESSAGE_DATA_INTENT));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dataUpdateReceiver == null) dataUpdateReceiver = new DataUpdateReceiver();
        IntentFilter intentFilter = new IntentFilter(SpussService.REFRESH_DATA_INTENT);
        registerReceiver(dataUpdateReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dataUpdateReceiver != null) unregisterReceiver(dataUpdateReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_logs:
                Intent intent = new Intent(this, ActivityLogs.class);
                startActivity(intent);
                break;
            case R.id.action_devices:
                break;
            case R.id.action_activity:
                break;
            case R.id.action_users:
                break;
            case R.id.action_settings:
                break;
            case R.id.action_notifications:
                break;
            case R.id.action_about:
                break;
            case R.id.action_tips:
                break;
        }
        makeToast(item.getTitle().toString());

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnArm:
                toggleState();


                break;
            case R.id.btnTemp:
                makeToast("TEMP pressed");
                break;
            case R.id.btnLogs:
                Intent intent = new Intent(this, ActivityLogs.class);
                startActivity(intent);
                break;
        }
    }

    private void makeToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void toggleState(){
        armed = !armed;
        armAnimation();
    }

    private void armAnimation(){
        btnArm.setImageResource( (armed) ? R.drawable.main1 : R.drawable.main0 );
        int start = (armed) ? 180 : 0;
        RotateAnimation ra = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setFillAfter(true);
        ra.setFillBefore(true);
        ra.setDuration(1000);
        btnArm.startAnimation(ra);
    }
}


class DataUpdateReceiver extends BroadcastReceiver {
    final String TAG = "MAIN Receiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SpussService.REFRESH_DATA_INTENT)) {
            // Do stuff - maybe update my view based on the changed DB contents
            Log.e(TAG, "Got info from Service");
        }
    }
}