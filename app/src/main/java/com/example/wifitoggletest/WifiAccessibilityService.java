package com.example.wifitoggletest;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

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
                GLOBAL_ACTION_QUICK_SETTINGS
        );
    }

    /*
     * Diagnostic method.
     * Reads everything Accessibility can currently see.
     */
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

                result.append("===== WINDOW =====\n");

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

        if (node == null) return;

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
