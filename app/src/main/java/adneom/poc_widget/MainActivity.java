package adneom.poc_widget;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import adneom.poc_widget.notification.NotificationTest;

public class MainActivity extends AppCompatActivity {

    private NotificationManager notificationManager;
    private RemoteViews remoteViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotification();
    }

    private void createNotification(){
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        //build notification :
        createViewNotif();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(remoteViews);

        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(1, notification);

    }

    private void createViewNotif(){
        remoteViews = new RemoteViews(getPackageName(), R.layout.layout_widget_test);

        remoteViews.setOnClickPendingIntent(R.id.button_one,intentAlert(1));
    }

    private PendingIntent intentAlert(int vaalue) {
        Intent intent = new Intent(MainActivity.this, NotificationTest.class);
        switch (vaalue) {
            case 1 :
                intent.putExtra("alert",vaalue);
                break;
        }
        PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this, (int)System.currentTimeMillis(),intent,0);
        return pIntent;
    }
}
