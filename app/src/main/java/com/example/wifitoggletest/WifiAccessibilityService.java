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

        if (text != null &&
                (text.toString().equalsIgnoreCase("Vibration mode") ||
                 text.toString().equalsIgnoreCase("Silent mode"))) {

            result.append("\n===== SOUND TILE =====\n");

            result.append("TEXT: ")
                    .append(text)
                    .append("\n");

            result.append("NODE CLASS: ")
                    .append(node.getClassName())
                    .append("\n");

            result.append("NODE CLICKABLE: ")
                    .append(node.isClickable())
                    .append("\n");

            result.append("NODE CHECKABLE: ")
                    .append(node.isCheckable())
                    .append("\n");

            result.append("NODE CHECKED: ")
                    .append(node.isChecked())
                    .append("\n");

            AccessibilityNodeInfo parent =
                    node.getParent();

            if (parent != null) {

                result.append("\n--- PARENT ---\n");

                result.append("CLASS: ")
                        .append(parent.getClassName())
                        .append("\n");

                result.append("CLICKABLE: ")
                        .append(parent.isClickable())
                        .append("\n");

                result.append("CHECKABLE: ")
                        .append(parent.isCheckable())
                        .append("\n");

                result.append("CHECKED: ")
                        .append(parent.isChecked())
                        .append("\n");

                result.append("SELECTED: ")
                        .append(parent.isSelected())
                        .append("\n");

                result.append("ENABLED: ")
                        .append(parent.isEnabled())
                        .append("\n");

                result.append("FOCUSED: ")
                        .append(parent.isFocused())
                        .append("\n");

                result.append("VISIBLE: ")
                        .append(parent.isVisibleToUser())
                        .append("\n");

                parent.recycle();
            }

            result.append("====================\n");
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
