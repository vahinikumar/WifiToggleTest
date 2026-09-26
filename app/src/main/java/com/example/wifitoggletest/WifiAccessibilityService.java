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

    public static boolean openNotificationPanel() {

        if (instance == null) {
            return false;
        }

        boolean opened =
                instance.performGlobalAction(
                        GLOBAL_ACTION_NOTIFICATIONS
                );

        if (opened) {
            instance.findVolumeControl();
        }

        return opened;
    }

    private void findVolumeControl() {

        handler.postDelayed(() -> {

            for (android.view.accessibility.AccessibilityWindowInfo window
                    : getWindows()) {

                AccessibilityNodeInfo root = window.getRoot();

                if (root != null) {

                    if (findAndIncreaseVolume(root)) {
                        root.recycle();
                        return;
                    }

                    root.recycle();
                }
            }

        }, 1000);
    }

    private boolean findAndIncreaseVolume(
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

        /*
         * Look for Vivo's volume control.
         */
        if (combined.contains("volume") ||
                combined.contains("ring")) {

            if (node.isClickable()) {

                return node.performAction(
                        AccessibilityNodeInfo.ACTION_CLICK
                );
            }
        }

        for (int i = 0;
                i < node.getChildCount();
                i++) {

            AccessibilityNodeInfo child =
                    node.getChild(i);

            if (child != null) {

                if (findAndIncreaseVolume(child)) {

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
