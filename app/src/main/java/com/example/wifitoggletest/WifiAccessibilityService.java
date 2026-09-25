package com.example.wifitoggletest;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class WifiAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event == null) return;

        AccessibilityNodeInfo root =
                getRootInActiveWindow();

        if (root == null) return;

        findAndClickWifiSwitch(root);
    }

    private boolean findAndClickWifiSwitch(
            AccessibilityNodeInfo node) {

        if (node == null) return false;

        CharSequence text = node.getText();
        CharSequence description =
                node.getContentDescription();

        String textValue =
                text != null ? text.toString() : "";

        String descriptionValue =
                description != null
                        ? description.toString()
                        : "";

        if (textValue.equalsIgnoreCase("Wi-Fi") ||
                descriptionValue.equalsIgnoreCase("Wi-Fi")) {

            if (node.isClickable()) {
                return node.performAction(
                        AccessibilityNodeInfo.ACTION_CLICK);
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {

            AccessibilityNodeInfo child =
                    node.getChild(i);

            if (child != null) {

                if (findAndClickWifiSwitch(child)) {
                    child.recycle();
                    return true;
                }

                child.recycle();
            }
        }

        return false;
    }

    @Override
    public void onInterrupt() {
    }
}
