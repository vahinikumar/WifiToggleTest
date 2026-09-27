package com.example.wifitoggletest;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class WifiAccessibilityService extends AccessibilityService {

    private static WifiAccessibilityService instance;

    private final android.os.Handler handler =
            new android.os.Handler();

    @Override
    protected void onServiceConnected() {

        super.onServiceConnected();

        instance = this;
    }

    public static boolean openSoundSettings() {

        if (instance == null) {
            return false;
        }

        Intent intent =
                new Intent(
                        Settings.ACTION_SOUND_SETTINGS);

        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK);

        instance.startActivity(intent);

        instance.handler.postDelayed(
                () -> instance.setRingVolumeToMaximum(),
                1500);

        return true;
    }

    private void setRingVolumeToMaximum() {

        for (android.view.accessibility
                .AccessibilityWindowInfo window
                : getWindows()) {

            AccessibilityNodeInfo root =
                    window.getRoot();

            if (root != null) {

                if (findRingVolume(root)) {

                    root.recycle();

                    handler.postDelayed(
                            () -> {

                                GrandmaWidget.updateAllWidgets(
                                        getApplicationContext()
                                );

                            },
                            2000);

                    return;
                }

                root.recycle();
            }
        }
    }

    private boolean findRingVolume(
            AccessibilityNodeInfo node) {

        if (node == null) return false;

        CharSequence text =
                node.getText();

        CharSequence description =
                node.getContentDescription();

        String t =
                text == null
                        ? ""
                        : text.toString();

        String d =
                description == null
                        ? ""
                        : description.toString();

        boolean isRingVolume =
                (t.equalsIgnoreCase("Ring volume") ||
                 d.equalsIgnoreCase("Ring volume"))
                &&
                "android.widget.SeekBar".equals(
                        node.getClassName());

        if (isRingVolume) {

            Bundle arguments =
                    new Bundle();

            arguments.putFloat(
                    "android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE",
                    15f);

            return node.performAction(
                    16908349,
                    arguments);
        }

        for (int i = 0;
                i < node.getChildCount();
                i++) {

            AccessibilityNodeInfo child =
                    node.getChild(i);

            if (child != null) {

                if (findRingVolume(child)) {

                    child.recycle();

                    return true;
                }

                child.recycle();
            }
        }

        return false;
    }

    @Override
    public void onAccessibilityEvent(
            AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onDestroy() {

        instance = null;

        handler.removeCallbacksAndMessages(null);

        super.onDestroy();
    }
}
