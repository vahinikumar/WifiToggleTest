package com.example.wifitoggletest;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.view.accessibility.AccessibilityNodeInfo;

public class WifiAccessibilityService extends AccessibilityService {

    private static WifiAccessibilityService instance;

    private final Handler handler =
            new Handler();

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
            instance.findWifiTile();
        }

        return opened;
    }

    private void findWifiTile() {

        handler.postDelayed(() -> {

            for (android.view.accessibility
                    .AccessibilityWindowInfo window
                    : getWindows()) {

                AccessibilityNodeInfo root =
                        window.getRoot();

                if (root != null) {

                    if (searchWifi(root)) {

                        root.recycle();

                        /*
                         * Give the phone a moment to
                         * complete the Wi-Fi change.
                         */
                        handler.postDelayed(() -> {

                            /*
                             * Close the Control Panel.
                             */
                            performGlobalAction(
                                    GLOBAL_ACTION_BACK
                            );

                            /*
                             * Wait for the Wi-Fi state
                             * to update, then refresh
                             * the widget image.
                             */
                            handler.postDelayed(() -> {

                                GrandmaWidget.updateAllWidgets(
                                        getApplicationContext()
                                );

                            }, 1000);

                        }, 700);

                        return;
                    }

                    root.recycle();
                }
            }

        }, 1000);
    }

    private boolean searchWifi(
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

        if (t.toLowerCase().contains("wi-fi") ||
                t.toLowerCase().contains("wifi") ||
                d.toLowerCase().contains("wi-fi") ||
                d.toLowerCase().contains("wifi")) {

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

                if (searchWifi(child)) {

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

        handler.removeCallbacksAndMessages(null);

        super.onDestroy();
    }
}
