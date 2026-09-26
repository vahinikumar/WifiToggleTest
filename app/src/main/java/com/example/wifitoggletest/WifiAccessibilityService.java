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

                instance.clickModeWithRetry(
                        "Vibration mode",
                        0
                );

            }, 1000);
        }

        return opened;
    }

    private void clickModeWithRetry(
            String mode,
            int attempt) {

        if (attempt >= 10) {
            return;
        }

        if (clickMode(mode)) {

            if (mode.equals("Vibration mode")) {

                handler.postDelayed(() -> {

                    clickModeWithRetry(
                            "Silent mode",
                            0
                    );

                }, 1000);
            }

            return;
        }

        handler.postDelayed(() -> {

            clickModeWithRetry(
                    mode,
                    attempt + 1
            );

        }, 500);
    }

    private boolean clickMode(String mode) {

        for (android.view.accessibility.AccessibilityWindowInfo window
                : getWindows()) {

            AccessibilityNodeInfo root =
                    window.getRoot();

            if (root != null) {

                if (findAndClickMode(root, mode)) {

                    root.recycle();

                    return true;
                }

                root.recycle();
            }
        }

        return false;
    }

    private boolean findAndClickMode(
            AccessibilityNodeInfo node,
            String mode) {

        if (node == null) {
            return false;
        }

        CharSequence text =
                node.getText();

        if (text != null &&
                text.toString().trim()
                        .equalsIgnoreCase(mode)) {

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

                if (findAndClickMode(
                        child,
                        mode)) {

                    child.recycle();

                    return true;
                }

                child.recycle();
            }
        }

        return false;
    }

    public static String inspectWindows() {

        if (instance == null) {
            return "Accessibility Service is not connected.";
        }

        StringBuilder result =
                new StringBuilder();

        for (android.view.accessibility.AccessibilityWindowInfo window
                : instance.getWindows()) {

            AccessibilityNodeInfo root =
                    window.getRoot();

            if (root != null) {

                result.append(
                        "===== WINDOW =====\n"
                );

                instance.inspectNode(
                        root,
                        result
                );

                root.recycle();
            }
        }

        return result.toString();
    }

    private void inspectNode(
            AccessibilityNodeInfo node,
            StringBuilder result) {

        if (node == null) {
            return;
        }

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

        if (!t.isEmpty() || !d.isEmpty()) {

            result.append("TEXT: ")
                    .append(t)
                    .append("\n");

            result.append("DESC: ")
                    .append(d)
                    .append("\n");

            result.append("CLASS: ")
                    .append(node.getClassName())
                    .append("\n");

            result.append("CLICKABLE: ")
                    .append(node.isClickable())
                    .append("\n");

            result.append("CHECKABLE: ")
                    .append(node.isCheckable())
                    .append("\n");

            result.append("CHECKED: ")
                    .append(node.isChecked())
                    .append("\n\n");
        }

        for (int i = 0;
                i < node.getChildCount();
                i++) {

            AccessibilityNodeInfo child =
                    node.getChild(i);

            if (child != null) {

                inspectNode(
                        child,
                        result
                );

                child.recycle();
            }
        }
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
