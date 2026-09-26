package com.example.wifitoggletest;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;
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
                        GLOBAL_ACTION_QUICK_SETTINGS
                );

        if (opened) {

            instance.handler.postDelayed(() -> {

                instance.clickSilentMode();

            }, 1000);
        }

        return opened;
    }

    private void clickSilentMode() {

        for (
                android.view.accessibility.AccessibilityWindowInfo window
                : getWindows()
        ) {

            AccessibilityNodeInfo root =
                    window.getRoot();

            if (root != null) {

                if (findAndClickSilentMode(root)) {

                    root.recycle();

                    return;
                }

                root.recycle();
            }
        }
    }

    private boolean findAndClickSilentMode(
            AccessibilityNodeInfo node) {

        if (node == null) {
            return false;
        }

        CharSequence text =
                node.getText();

        if (text != null &&
                text.toString()
                        .trim()
                        .equalsIgnoreCase("Silent mode")) {

            AccessibilityNodeInfo parent =
                    node.getParent();

            if (parent != null) {

                if (parent.isClickable()) {

                    boolean clicked =
                            parent.performAction(
                                    AccessibilityNodeInfo.ACTION_CLICK
                            );

                    parent.recycle();

                    return clicked;
                }

                parent.recycle();
            }
        }

        for (int i = 0;
                i < node.getChildCount();
                i++) {

            AccessibilityNodeInfo child =
                    node.getChild(i);

            if (child != null) {

                if (findAndClickSilentMode(child)) {

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

        super.onDestroy();
    }
}
