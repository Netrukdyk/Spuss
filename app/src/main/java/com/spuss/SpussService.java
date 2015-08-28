package com.spuss;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class SpussService extends Service {

    private static final String TAG = "SpussService";
    static final String REFRESH_DATA_INTENT = "Refresh";
    static final String MESSAGE_DATA_INTENT = "Message";
    private boolean isRunning = false;

    Server server;
    Handler serverHandler;

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");
        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service onStartCommand");

        startServer();

        if (isRunning) {
            Log.i(TAG, "Service running");
            sendBroadcast(new Intent(SpussService.REFRESH_DATA_INTENT));
        }

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        killServer();
        Log.i(TAG, "Service onDestroy");
    }

    private void startServer() {
        if (server == null) {
            server = new Server(uiHandler);
            server.start();
        }
    }

    private void killServer() {
        if (server != null && server.getStatus() == 1) {
            server.kill();
            server = null;
        }
    }

    private void reconnect() {
        killServer();
        startServer();
    }

    public void showNotification(Context context, String title, String text) {

        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // intent triggered, you can add other intent for other actions
        Intent intent = new Intent(context, ActivityMain.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = new Notification.Builder(context)

                .setContentTitle(title).setContentText(text).setSmallIcon(R.drawable.log5).setContentIntent(pIntent).setSound(soundUri).setLights(Color.RED, 300, 300)
                .addAction(R.drawable.log3, "View", pIntent).addAction(0, "Remind", pIntent).setPriority(Notification.PRIORITY_MAX).build();
        mNotification.defaults = 0;
        mNotification.defaults |= Notification.DEFAULT_LIGHTS;
        mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
        mNotification.defaults |= Notification.DEFAULT_VIBRATE;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // If you want to hide the notification after it was selected, do the code below
        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, mNotification);
    }

    // Apdoroja žinutes, gautas iš serverio
    @SuppressLint("HandlerLeak")
    public Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            C.Type type = C.Type.values()[msg.what];

            switch (type) {
                case STATUS:
//                    if (msg.arg1 == 1)
//                        setServerStatus(1);
//                    else
//                        setServerStatus(0);
                    break;
                case INFO:

                    break;
                case OTHER:
                    if (msg.getData().get("Server") == "Disconnected")
                        reconnect();
                    else {
                        String json = msg.getData().getString("Server");
                        Log.e(TAG, json);
                        if (json.contains("ping")) break;

//                        list.setText(json + '\n' + list.getText());
                        parse(json);
                    }
                    break;
            }
        };
    };

    private void parse(String jsonText) {
        if (jsonText.equals("{}"))
            return;
        try {
            JSONObject FullJson = new JSONObject(jsonText);

            for (int i = 0; i < FullJson.names().length(); i++) {
                String id = FullJson.names().getString(i);
                JSONObject json = FullJson.getJSONObject(id);
                String type = json.getString("type");
                Log.v("JSON", json.toString());
                if (type.equals("device")) {
                    String alias = json.getString("alias");
                    ;
                    if (id.charAt(0) == 'R') {
                        Boolean relay = json.getInt("relay") == 1;
                        //addSocket(new Socket(id, alias, relay));
                    } else if (id.charAt(0) == 'T') {
                        String temp = json.getString("temp");
                        //addTemp(new Temp(id, alias, temp));
                    }

                } else if (type.equals("del_device")) {
                    //removeDevice(id);
                } else if (type.equals("changed")) {

                    String sensor = "";
                    String who = "";
                    if (id.charAt(0) == 'D') {
                        who = "Durys";
                        try{
                            Boolean vibr = json.getInt("vibr") == 1;
                            Boolean mic = json.getInt("mic") == 1;
                            sensor = (vibr) ? "Vibration" : (mic)? "Mic" : "";
                        } catch (JSONException e){}

                        //Switch switch1 = (Switch) devList.findViewWithTag(id).findViewById(R.id.switch1);
                        //switch1.setChecked(relay);

                    } else if (id.charAt(0) == 'L') {
                        who = "Langas";
                        //String temp = json.getString("temp");
                        //TextView value = (TextView) devList.findViewWithTag(id).findViewById(R.id.value);
                        // value.setText(temp);
                        sensor = "Vibration";
                    } else if (id.charAt(0) == 'S') {
                        who = "Stalas";
                        //String temp = json.getString("temp");
                        //TextView value = (TextView) devList.findViewWithTag(id).findViewById(R.id.value);
                        // value.setText(temp);
                        sensor = "Vibration";
                    }

                    showNotification(this, who, sensor);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

