package com.e16din.alertmanager;

/**
 * Created by e16din on 21.09.15.
 */
public abstract class AlertDialogCallback<T> {

    public abstract void onPositive(T... data);

    public void onNegative(T... data) {
    }

    public void onNeutral(T... data) {
    }
}
