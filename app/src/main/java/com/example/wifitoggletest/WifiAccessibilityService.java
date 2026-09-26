package com.example.wifitoggletest;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
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
            android.content.Intent intent =
                    new android.content.Intent(
                            android.provider.Settings.ACTION_SOUND_SETTINGS);

            intent.addFlags(
                    android.content.Intent.FLAG_ACTIVITY_NEW_TASK);

            instance.startActivity(intent);

            instance.handler.postDelayed(
                    () -> instance.findRingVolume(),
                    1500);

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private void findRingVolume() {

        for (android.view.accessibility.AccessibilityWindowInfo window
                : getWindows()) {

            AccessibilityNodeInfo root = window.getRoot();

            if (root != null) {

                if (findVolumeSlider(root)) {
                    root.recycle();
                    return;
                }

                root.recycle();
            }
        }
    }

    private boolean findVolumeSlider(
            AccessibilityNodeInfo node) {

        if (node == null) return false;

        CharSequence text = node.getText();
        CharSequence description =
                node.getContentDescription();

        String t = text == null ? "" : text.toString();
        String d = description == null
                ? ""
                : description.toString();

        String combined =
                (t + " " + d).toLowerCase();

        if (combined.contains("ring volume") ||
                combined.equals("ring")) {

            if (node.isFocusable() ||
                    node.isClickable()) {

                if (node.performAction(
                        AccessibilityNodeInfo.ACTION_FOCUS)) {

                    return true;
                }
            }
        }

        for (int i = 0;
                i < node.getChildCount();
                i++) {

            AccessibilityNodeInfo child =
                    node.getChild(i);

            if (child != null) {

                if (findVolumeSlider(child)) {
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