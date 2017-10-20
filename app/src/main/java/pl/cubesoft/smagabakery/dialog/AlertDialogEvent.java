package pl.cubesoft.smagabakery.dialog;

import android.os.Bundle;

import java.util.List;

public class AlertDialogEvent {
    public final int requestId;
    public final Button button;
    public final List<AlertDialogFragment.Item> items;
    public final Bundle args;
    public enum Button {
        POSITIVE,
        NEUTRAL,
        NEGATIVE
    }



    public AlertDialogEvent(int requestId, Button button, List<AlertDialogFragment.Item> items, Bundle args) {
        this.requestId = requestId;
        this.button = button;
        this.items = items;
        this.args = args;
    }
}
