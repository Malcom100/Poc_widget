package adneom.poc_widget.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.Random;

import adneom.poc_widget.R;

/**
 * Created by gtshilombowanticale on 10-08-17.
 */

public class TestWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        /**final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            String number = String.format("%03d", (new Random().nextInt(900) + 100));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget_test);


        }*/
    }
}
