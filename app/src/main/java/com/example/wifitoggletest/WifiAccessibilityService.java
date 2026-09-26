package com.example.wifitoggletest;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

public class WifiAccessibilityService extends AccessibilityService {

    private final Handler handler = new Handler();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event == null) return;

        AccessibilityNodeInfo root =
                getRootInActiveWindow();

        if (root == null) return;

        // Show the controls found on the screen
        showNodes(root);
    }

    private void showNodes(AccessibilityNodeInfo node) {

        if (node == null) return;

        CharSequence text = node.getText();
        CharSequence description =
                node.getContentDescription();

        if (text != null && text.length() > 0) {

            Toast.makeText(
                    this,
                    "TEXT: " + text,
                    Toast.LENGTH_SHORT
            ).show();

        }

        if (description != null &&
                description.length() > 0) {

            Toast.makeText(
                    this,
                    "DESC: " + description,
                    Toast.LENGTH_SHORT
            ).show();

        }

        for (int i = 0; i < node.getChildCount(); i++) {

            AccessibilityNodeInfo child =
                    node.getChild(i);

            if (child != null) {

                showNodes(child);
                child.recycle();
            }
        }
    }

    @Override
    public void onInterrupt() {
    }
}
