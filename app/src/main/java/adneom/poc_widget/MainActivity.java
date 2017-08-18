package adneom.poc_widget;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RemoteViews;

import org.joda.time.DateTime;

import adneom.poc_widget.notification.NotificationTest;

public class MainActivity extends AppCompatActivity {

    private NotificationManager notificationManager;
    private RemoteViews remoteViews;

    private boolean isSimple;

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
        //simpleNotif();
        bitNotif();

    }

    private void simpleNotif() {
        isSimple = true;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(remoteViews);

        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(1, notification);
    }

    private void bitNotif(){
        isSimple = false;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setCustomBigContentView(remoteViews)
                .setSmallIcon(R.mipmap.ic_launcher);

        Notification notification = builder.build();
        builder.setStyle(new NotificationCompat.BigTextStyle(builder));
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);
    }

    private void createViewNotif(){
        remoteViews = new RemoteViews(getPackageName(), R.layout.layout_widget_notification_big);

        String msg = "fkdljfdldkfj;ldaksjfkladj;flja;lkjdfljadslfjaddfdsfafjdfadfdl;akjf;lkdf;lkaj;flkjda;lkfjadljflk" +
                ";adsjfladjflk;dfjlkdjflakdfjdaffjdlfjdjjjadjflkjadlkfjad;lkfjad;sljf;ladkjajlkfjad;lksfjl;akdjf;lkdsajf;";


        remoteViews.setTextViewText(R.id.big_txt,msg);
        remoteViews.setOnClickPendingIntent(R.id.big_txt,intentAlert(1));
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
