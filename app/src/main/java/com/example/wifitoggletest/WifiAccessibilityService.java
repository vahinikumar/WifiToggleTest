package com.example.wifitoggletest;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.view.accessibility.AccessibilityNodeInfo;

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
            Intent intent =
                    new Intent(Settings.ACTION_SOUND_SETTINGS);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            instance.startActivity(intent);

            instance.handler.postDelayed(
                    () -> instance.inspectScreen(),
                    1500
            );

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

        final String output = result.length() == 0
                ? "No accessibility nodes found."
                : result.toString();

        handler.post(() -> showResult(output));
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
                    "\nCLASS: " + node.getClassName() +
                    "\nCLICKABLE: " + node.isClickable() +
                    "\nFOCUSABLE: " + node.isFocusable() +
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

    private void showResult(String output) {

        Intent intent =
                new Intent(this, DiagnosticActivity.class);

        intent.putExtra("diagnostic", output);

        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
        );

        startActivity(intent);
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