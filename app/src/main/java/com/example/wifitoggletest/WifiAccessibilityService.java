package com.example.wifitoggletest;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

public class WifiAccessibilityService extends AccessibilityService {

    private static StringBuilder allControls =
            new StringBuilder();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event == null) return;

        allControls.setLength(0);

        // Check every interactive window
        for (AccessibilityWindowInfo window :
                getWindows()) {

            AccessibilityNodeInfo root =
                    window.getRoot();

            if (root != null) {

                allControls.append(
                        "\n===== WINDOW =====\n"
                );

                collectNodes(root);

                root.recycle();
            }
        }
    }

    private void collectNodes(
            AccessibilityNodeInfo node) {

        if (node == null) return;

        CharSequence text =
                node.getText();

        CharSequence description =
                node.getContentDescription();

        if ((text != null && text.length() > 0) ||
                (description != null &&
                        description.length() > 0)) {

            allControls.append("\n");

            if (text != null &&
                    text.length() > 0) {

                allControls.append("TEXT: ");
                allControls.append(text);
                allControls.append("\n");
            }

            if (description != null &&
                    description.length() > 0) {

                allControls.append("DESC: ");
                allControls.append(description);
                allControls.append("\n");
            }

            allControls.append(
                    "CLASS: " +
                    node.getClassName() +
                    "\n"
            );

            allControls.append(
                    "CLICKABLE: " +
                    node.isClickable() +
                    "\n"
            );

            allControls.append(
                    "CHECKABLE: " +
                    node.isCheckable() +
                    "\n"
            );

            allControls.append(
                    "CHECKED: " +
                    node.isChecked() +
                    "\n"
            );

            allControls.append(
                    "----------------\n"
            );
        }

        for (int i = 0;
             i < node.getChildCount();
             i++) {

            AccessibilityNodeInfo child =
                    node.getChild(i);

            if (child != null) {

                collectNodes(child);

                child.recycle();
            }
        }
    }

    public static String getAllControls() {
        return allControls.toString();
    }

    @Override
    public void onInterrupt() {
    }
}
