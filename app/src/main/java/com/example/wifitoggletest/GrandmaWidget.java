package com.example.wifitoggletest;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.widget.RemoteViews;

public class GrandmaWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(
            Context context,
            AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {

        WifiManager wifiManager =
                (WifiManager) context.getApplicationContext()
                        .getSystemService(Context.WIFI_SERVICE);

        boolean wifiOn =
                wifiManager != null &&
                wifiManager.isWifiEnabled();

        for (int appWidgetId : appWidgetIds) {

            RemoteViews views =
                    new RemoteViews(
                            context.getPackageName(),
                            R.layout.grandma_widget);

            if (wifiOn) {
                views.setImageViewResource(
                        R.id.wifi_widget_image,
                        R.drawable.wifi_on);
            } else {
                views.setImageViewResource(
                        R.id.wifi_widget_image,
                        R.drawable.wifi_off);
            }

            Intent intent =
                    new Intent(context, MainActivity.class);

            PendingIntent pendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT |
                            PendingIntent.FLAG_IMMUTABLE);

            views.setOnClickPendingIntent(
                    R.id.wifi_widget_image,
                    pendingIntent);

            appWidgetManager.updateAppWidget(
                    appWidgetId,
                    views);
        }
    }
}
