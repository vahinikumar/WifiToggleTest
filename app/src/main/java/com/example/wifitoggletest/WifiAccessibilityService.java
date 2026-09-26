package com.example.wifitoggletest;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

public class WifiAccessibilityService extends AccessibilityService {

    private static WifiAccessibilityService instance;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        instance = this;
    }

    public static boolean openNotificationPanel() {

        if (instance == null) {
            return false;
        }

        return instance.performGlobalAction(
                GLOBAL_ACTION_NOTIFICATIONS
        );
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

        super.onDestroy();
    }
}
