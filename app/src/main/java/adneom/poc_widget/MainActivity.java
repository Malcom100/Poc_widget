package adneom.poc_widget;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import adneom.poc_widget.fragments.FragmentMap;
import adneom.poc_widget.interfaces.ISnapHotGoogleMap;
import adneom.poc_widget.notification.NotificationTest;

public class MainActivity extends AppCompatActivity implements ISnapHotGoogleMap{

    public static double lat = 50.8537638;
    public static double lng = 4.3147165;

    private NotificationManager notificationManager;
    private RemoteViews remoteViews;
    private Bitmap bitmap;
    public static final String KEY_VALUE = "value";
    private Notification notification;
    private final int NOTIFICATION_ID = 0;
    private final int ZOOM_MAP = 12;
    private final String TYPE_MAP = "roadmap";
    private double latA = 50.8894283;
    private double lngA = 4.3243892;
    private double latB = 50.8277026;
    private double lngB = 4.353329;

    private boolean isSimple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotification();

        //FragmentMap fragmentMap = new FragmentMap();
        //getSupportFragmentManager().beginTransaction().add(R.id.container,fragmentMap,"Fragment").commit();
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
                .setContentText("My text")
                .setContentTitle("My Title")
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher);

        notification = builder.build();
        builder.setStyle(new NotificationCompat.BigTextStyle(builder));
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(NOTIFICATION_ID, notification);

        //getMapUrl(lat,lng,400,400);
        getMapUrlWithTrace(600,600);
    }

    private void createViewNotif(){
        remoteViews = new RemoteViews(getPackageName(), R.layout.layout_widget_notification_big);


        remoteViews.setImageViewBitmap(R.id.snapshot,this.bitmap);
        //remoteViews.setTextViewText(R.id.big_txt,msg);
        remoteViews.setOnClickPendingIntent(R.id.big_txt,intentAlert(1));
        remoteViews.setOnClickPendingIntent(R.id.snapshot,intentAlert(2));
        remoteViews.setOnClickPendingIntent(R.id.header_notification,intentAlert(3));
    }

    private PendingIntent intentAlert(int vaalue) {
        Intent intent = new Intent(MainActivity.this, NotificationTest.class);
        switch (vaalue) {
            case 1 :
                intent.putExtra(KEY_VALUE,vaalue);
                break;
            case 2 :
                intent.putExtra(KEY_VALUE,vaalue);
                break;
            case 3 :

                break;
        }
        PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this, (int)System.currentTimeMillis(),intent,PendingIntent.FLAG_CANCEL_CURRENT);
        return pIntent;
    }

    private void getMapUrl(Double lat, Double lon, int width, int height) {
        final String coordPair = lat + "," + lon;
        String urlMap =  "http://maps.googleapis.com/maps/api/staticmap?"
                + "&zoom="+ZOOM_MAP
                + "&size=" + width + "x" + height
                + "&maptype="+TYPE_MAP+"&sensor=true"
                + "&center=" + coordPair
                + "&markers=color:black|" + coordPair;

        Picasso.with(MainActivity.this).load(Uri.parse(urlMap)).into(remoteViews,R.id.snapshot,NOTIFICATION_ID,notification);
    }

    private void getMapUrlWithTrace(int width, int height) {
        final String coordPair = lat + "," + lng;
        final String coordPairA = latA + "," + lngA;
        final String coordPairB = latB + "," + lngB;
        String urlMap =  "http://maps.googleapis.com/maps/api/staticmap?"
                + "&zoom="+ZOOM_MAP
                + "&size=" + width + "x" + height
                + "&maptype="+TYPE_MAP+"&sensor=true"
                + "&center=" + coordPair
                + "&markers=color:black|" + coordPair
                + "&markers=color:blue|label:A|" + coordPairA
                + "&markers=color:green|label:B|"+coordPairB
                + "&path=color:0x0000ff|weight:5|"+coordPairA+"|"+coordPairB;
        Picasso.with(MainActivity.this).load(Uri.parse(urlMap)).into(remoteViews,R.id.snapshot,NOTIFICATION_ID,notification);
    }

    @Override
    public void senSnapshot(Bitmap bitmap) {
        //this.bitmap = bitmap;
        //createNotification();
    }
}
