package com.example.wifitoggletest;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.provider.Settings;
import android.view.accessibility.AccessibilityNodeInfo;

public class WifiAccessibilityService extends AccessibilityService {

    private static WifiAccessibilityService instance;

    private final android.os.Handler handler =
            new android.os.Handler();

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        instance = this;
    }

    public static boolean openSoundSettings() {

        if (instance == null) return false;

        Intent intent =
                new Intent(Settings.ACTION_SOUND_SETTINGS);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        instance.startActivity(intent);

        instance.handler.postDelayed(
                () -> instance.inspectRingVolume(),
                1500);

        return true;
    }

    private void inspectRingVolume() {

        for (android.view.accessibility.AccessibilityWindowInfo window
                : getWindows()) {

            AccessibilityNodeInfo root =
                    window.getRoot();

            if (root != null) {

                if (findRingVolume(root)) {
                    root.recycle();
                    return;
                }

                root.recycle();
            }
        }
    }

    private boolean findRingVolume(
            AccessibilityNodeInfo node) {

        if (node == null) return false;

        CharSequence text = node.getText();
        CharSequence description =
                node.getContentDescription();

        String t = text == null ? "" : text.toString();
        String d = description == null
                ? ""
                : description.toString();

        boolean isRingVolume =
                (t.equalsIgnoreCase("Ring volume") ||
                 d.equalsIgnoreCase("Ring volume")) &&
                "android.widget.SeekBar".equals(
                        node.getClassName());

        if (isRingVolume) {

            StringBuilder result =
                    new StringBuilder();

            result.append("RING VOLUME\n\n");

            AccessibilityNodeInfo.RangeInfo range =
                    node.getRangeInfo();

            if (range != null) {

                result.append("MIN: ")
                        .append(range.getMin())
                        .append("\n");

                result.append("MAX: ")
                        .append(range.getMax())
                        .append("\n");

                result.append("CURRENT: ")
                        .append(range.getCurrent())
                        .append("\n\n");
            }

            result.append("SUPPORTED ACTIONS:\n\n");

            for (AccessibilityNodeInfo.AccessibilityAction action
                    : node.getActionList()) {

                result.append("ID: ")
                        .append(action.getId())
                        .append("\n");

                CharSequence label =
                        action.getLabel();

                if (label != null) {

                    result.append("LABEL: ")
                            .append(label)
                            .append("\n");
                }

                result.append("\n");
            }

            Intent intent =
                    new Intent(
                            this,
                            DiagnosticActivity.class);

            intent.putExtra(
                    "diagnostic",
                    result.toString());

            intent.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

            return true;
        }

        for (int i = 0;
                i < node.getChildCount();
                i++) {

            AccessibilityNodeInfo child =
                    node.getChild(i);

            if (child != null) {

                if (findRingVolume(child)) {
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
        super.onDestroy();
    }
}