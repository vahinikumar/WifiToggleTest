package com.example.wifitoggletest;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.RemoteViews;

public class GrandmaWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(
            Context context,
            AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {

        updateWidgets(
                context,
                appWidgetManager,
                appWidgetIds
        );
    }

    public static void updateAllWidgets(Context context) {

        AppWidgetManager manager =
                AppWidgetManager.getInstance(context);

        ComponentName componentName =
                new ComponentName(
                        context,
                        GrandmaWidget.class
                );

        int[] widgetIds =
                manager.getAppWidgetIds(
                        componentName
                );

        updateWidgets(
                context,
                manager,
                widgetIds
        );
    }

    private static void updateWidgets(
            Context context,
            AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {

        AudioManager audioManager =
                (AudioManager)
                        context.getSystemService(
                                Context.AUDIO_SERVICE
                        );

        boolean ringerOn = false;

        if (audioManager != null) {

            int ringMode =
                    audioManager.getRingerMode();

            int currentVolume =
                    audioManager.getStreamVolume(
                            AudioManager.STREAM_RING
                    );

            int maxVolume =
                    audioManager.getStreamMaxVolume(
                            AudioManager.STREAM_RING
                    );

            ringerOn =
                    ringMode ==
                            AudioManager.RINGER_MODE_NORMAL
                    &&
                    currentVolume == maxVolume;
        }

        for (int appWidgetId : appWidgetIds) {

            RemoteViews views =
                    new RemoteViews(
                            context.getPackageName(),
                            R.layout.grandma_widget
                    );

            if (ringerOn) {

                views.setImageViewResource(
                        R.id.wifi_widget_image,
                        R.drawable.ringer_on
                );

            } else {

                views.setImageViewResource(
                        R.id.wifi_widget_image,
                        R.drawable.ringer_off
                );
            }

            Intent intent =
                    new Intent(
                            context,
                            MainActivity.class
                    );

            PendingIntent pendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT |
                            PendingIntent.FLAG_IMMUTABLE
                    );

            views.setOnClickPendingIntent(
                    R.id.wifi_widget_image,
                    pendingIntent
            );

            appWidgetManager.updateAppWidget(
                    appWidgetId,
                    views
            );
        }
    }

    @Override
    public void onReceive(
            Context context,
            Intent intent) {

        super.onReceive(
                context,
                intent
        );

        if (AudioManager.RINGER_MODE_CHANGED_ACTION
                .equals(intent.getAction())) {

            updateAllWidgets(context);
        }
    }
}
