package com.example.wifitoggletest;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.accessibility.AccessibilityNodeInfo;

public class WifiAccessibilityService extends AccessibilityService {

    private static WifiAccessibilityService instance;

    private final Handler handler = new Handler();

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        instance = this;
    }

    public static boolean openSoundSettings() {

        if (instance == null) return false;

        try {

            Intent intent =
                    new Intent(Settings.ACTION_SOUND_SETTINGS);

            intent.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK);

            instance.startActivity(intent);

            instance.handler.postDelayed(
                    () -> instance.setRingVolumeToMaximum(),
                    1500);

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private void setRingVolumeToMaximum() {

        for (android.view.accessibility.AccessibilityWindowInfo window
                : getWindows()) {

            AccessibilityNodeInfo root =
                    window.getRoot();

            if (root != null) {

                if (findRingVolume(root)) {
                    root.recycle();
                    return;
                }

                root.recycle();
            }
        }
    }

    private boolean findRingVolume(
            AccessibilityNodeInfo node) {

        if (node == null) return false;

        CharSequence text = node.getText();
        CharSequence description =
                node.getContentDescription();

        String t = text == null ? "" : text.toString();
        String d = description == null
                ? ""
                : description.toString();

        boolean isRingVolume =
                (t.equalsIgnoreCase("Ring volume") ||
                 d.equalsIgnoreCase("Ring volume")) &&
                "android.widget.SeekBar".equals(
                        node.getClassName());

        if (isRingVolume) {

            Bundle arguments = new Bundle();

            arguments.putInt(
                    AccessibilityNodeInfo
                            .ACTION_ARGUMENT_PROGRESS_VALUE_INT,
                    100);

            return node.performAction(
                    AccessibilityNodeInfo.ACTION_SET_PROGRESS,
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
            android.view.accessibility.AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onDestroy() {
        instance = null;
        super.onDestroy();
    }
}