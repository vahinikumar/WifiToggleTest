package com.example.wifitoggletest;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

public class WifiAccessibilityService extends AccessibilityService {

    private static WifiAccessibilityService instance;

    private final Handler handler = new Handler();

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        instance = this;
    }

    public static boolean openSoundSettings() {

        if (instance == null) return false;

        try {
            android.content.Intent intent =
                    new android.content.Intent(
                            android.provider.Settings.ACTION_SOUND_SETTINGS);

            intent.addFlags(
                    android.content.Intent.FLAG_ACTIVITY_NEW_TASK);

            instance.startActivity(intent);

            instance.handler.postDelayed(
                    () -> instance.inspectScreen(),
                    1500);

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private void inspectScreen() {

        StringBuilder result = new StringBuilder();

        for (android.view.accessibility.AccessibilityWindowInfo window
                : getWindows()) {

            AccessibilityNodeInfo root = window.getRoot();

            if (root != null) {

                inspectNode(root, result);

                root.recycle();
            }
        }

        Toast.makeText(
                this,
                result.length() == 0
                        ? "No accessibility nodes found"
                        : result.toString(),
                Toast.LENGTH_LONG
        ).show();
    }

    private void inspectNode(
            AccessibilityNodeInfo node,
            StringBuilder result) {

        if (node == null) return;

        CharSequence text = node.getText();
        CharSequence description =
                node.getContentDescription();

        String t = text == null ? "" : text.toString();
        String d = description == null
                ? ""
                : description.toString();

        if (!t.isEmpty() || !d.isEmpty()) {

            result.append(
                    "TEXT: " + t +
                    "\nDESC: " + d +
                    "\nCLASS: " +
                    node.getClassName() +
                    "\nCLICK: " +
                    node.isClickable() +
                    "\nFOCUS: " +
                    node.isFocusable() +
                    "\n\n"
            );
        }

        for (int i = 0;
                i < node.getChildCount();
                i++) {

            AccessibilityNodeInfo child =
                    node.getChild(i);

            if (child != null) {

                inspectNode(child, result);

                child.recycle();
            }
        }
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