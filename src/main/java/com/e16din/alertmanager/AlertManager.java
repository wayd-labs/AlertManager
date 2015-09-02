package com.e16din.alertmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager.BadTokenException;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

public class AlertManager {

    private static final int INVALID_VALUE = -100500;
    public static final int MILLISECONDS_IN_THE_DAY = 86400;

    public static void setCustomPostfix(String customPostfix) {
        AlertManager.customPostfix = customPostfix;
    }

    private static class Holder {
        public static final AlertManager HOLDER_INSTANCE = new AlertManager();
    }

    public static AlertManager manager(Context context) {
        Holder.HOLDER_INSTANCE.setContext(context);
        return Holder.HOLDER_INSTANCE;
    }

    private static int customAlertTitle = R.string.title_alert;
    private static int customErrorTitle = R.string.title_error;

    private static String customPostfix = "";


    public static String getCustomPostfix() {
        return customPostfix;
    }

    public static int getCustomAlertTitle() {
        return customAlertTitle;
    }

    public static void setCustomAlertTitle(int customAlertTitle) {
        AlertManager.customAlertTitle = customAlertTitle;
    }

    public static int getCustomErrorTitle() {
        return customErrorTitle;
    }

    public static void setCustomErrorTitle(int customErrorTitle) {
        AlertManager.customErrorTitle = customErrorTitle;
    }

    private Context context = null;
    private ArrayList<String> displayedAlerts = new ArrayList<>();

    public boolean isAlertDisplayed(String message) {
        return displayedAlerts.contains(message);
    }

    private void setContext(Context context) {
        this.context = context;
    }

    public void showAlert(String message, boolean isCancelable) {
        showAlert(message, customAlertTitle, isCancelable, null);
    }

    public void showErrorAlert(String message, boolean isCancelable) {
        showAlert(message, customErrorTitle, isCancelable, null);
    }

    public void showErrorAlert(String message, boolean isCancelable, DialogInterface.OnClickListener listener) {
        showAlert(message, customErrorTitle, isCancelable, listener);
    }

    public void showAlert(String message) {
        showAlert(message, customAlertTitle, false, null);
    }

    public void showAlert(int message) {
        showAlert(message, customAlertTitle, false, null);
    }

    public void showAlert(String message, DialogInterface.OnClickListener listener) {
        showAlert(message, customAlertTitle, false, listener);
    }

    public void showAlert(int message, DialogInterface.OnClickListener listener) {
        showAlert(message, customAlertTitle, false, listener);
    }

    public void showErrorAlert(String message) {
        showAlert(message, customErrorTitle, false, null);
    }

    public void showErrorAlert(String message, DialogInterface.OnClickListener listener) {
        showAlert(message, customErrorTitle, false, listener);
    }

    public void showAlert(final String message, final int title, final boolean isCancelable,
                          final DialogInterface.OnClickListener listener) {

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    AlertDialogWrapper.Builder builder = createAlertBuilder();
                    builder.setTitle(context.getString(title) + " " + customPostfix).setMessage(message)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (listener != null)
                                        listener.onClick(dialog, which);

                                    displayedAlerts.remove(message);
                                }
                            }).setCancelable(isCancelable).setOnKeyListener(new OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK || event.getAction() == KeyEvent.ACTION_UP) {
                                if (listener != null)
                                    listener.onClick(dialog, INVALID_VALUE);
                            }

                            displayedAlerts.remove(message);
                            return false;
                        }
                    }).show();
                    displayedAlerts.add(message);
                } catch (BadTokenException e) {
                    Log.e("debug", "error: ", e);
                }
            }
        }, 500);
    }

    private AlertDialogWrapper.Builder createAlertBuilder() {
        return new AlertDialogWrapper.Builder(context);
    }

    public void showAlert(final int message, int title, boolean isCancelable,
                          final DialogInterface.OnClickListener listener) {
        showAlert(context.getString(message), title, isCancelable, listener);
    }

    public void showAlert(final String message, boolean isCancelable, final DialogInterface.OnClickListener listener) {
        try {
            AlertDialogWrapper.Builder builder = createAlertBuilder();
            builder.setTitle(context.getString(customAlertTitle) + " " + customPostfix).setMessage(message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (listener != null)
                                listener.onClick(dialog, which);
                            displayedAlerts.remove(message);
                        }
                    }).setCancelable(isCancelable).setOnKeyListener(new OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK || event.getAction() == KeyEvent.ACTION_UP) {
                        if (listener != null)
                            listener.onClick(dialog, INVALID_VALUE);
                        displayedAlerts.remove(message);
                    }

                    return false;
                }
            }).show();
            displayedAlerts.add(message);
        } catch (BadTokenException e) {
            Log.e("debug", "error: ", e);
        }
    }

    public void showAlertYesNo(final String message, final DialogInterface.OnClickListener yesListener) {
        try {
            AlertDialogWrapper.Builder builder = createAlertBuilder();
            builder.setTitle(context.getString(customAlertTitle) + " " + customPostfix).setMessage(message)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (yesListener != null)
                                yesListener.onClick(dialog, which);

                            displayedAlerts.remove(message);
                        }
                    }).setNegativeButton(android.R.string.no, null).show();
            displayedAlerts.add(message);
        } catch (BadTokenException e) {
            Log.e("debug", "error: ", e);
        }
    }

    public void showDialogList(final String title, final CharSequence[] items, final TextView tv,
                               final DialogInterface.OnClickListener listener) {
        try {
            AlertDialogWrapper.Builder builder = createAlertBuilder();
            builder.setTitle(title).setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (tv != null)
                        tv.setText(items[which]);
                    if (listener != null)
                        listener.onClick(dialog, which);

                    displayedAlerts.remove(title);
                }
            }).show();
            displayedAlerts.add(title);
        } catch (BadTokenException e) {
            Log.e("debug", "error: ", e);
        }
    }

    public void showRadioList(final String title, final CharSequence[] items, final TextView tv,
                              final DialogInterface.OnClickListener listener) {
        new MaterialDialog.Builder(context)
                .title(title)
                .items(items)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/

                        if (tv != null)
                            tv.setText(items[which]);

                        if (listener != null)
                            listener.onClick(dialog, which);

                        dialog.dismiss();
                        dialog.cancel();
                        return true;
                    }
                })
                .alwaysCallSingleChoiceCallback()
                .positiveText(android.R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    public void showDialogList(final CharSequence[] items, final TextView tv, DialogInterface.OnClickListener listener) {
        showDialogList(context.getString(customAlertTitle) + " " + customPostfix, items, tv, listener);
    }

    public void showTimePicker(int hours, int minutes, final TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        final TimePicker timePicker = new TimePicker(context);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(hours);
        timePicker.setCurrentMinute(minutes);

        AlertDialogWrapper.Builder builder = createAlertBuilder();
        builder.setTitle(context.getString(R.string.set_time))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onTimeSetListener.onTimeSet(timePicker, timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                    }
                }).setView(timePicker).show();
    }

    public void showDatePicker(final DatePickerDialog.OnDateSetListener onDateSetListener) {
        showDatePicker(-1, -1, -1, onDateSetListener);
    }

    public void showDatePicker(final int year, final int month, final int day, final DatePickerDialog.OnDateSetListener onDateSetListener) {
        final DatePicker datePicker = new DatePicker(context);
        datePicker.updateDate(year, month - 1, day);
        datePicker.setCalendarViewShown(false);
        final AlertDialogWrapper.Builder builder = createAlertBuilder();
        builder.setTitle(context.getString(R.string.check_date))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onDateSetListener.onDateSet(datePicker,
                                datePicker.getYear(),
                                datePicker.getMonth() + 1,
                                datePicker.getDayOfMonth());
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setView(datePicker).show();
    }

    public void showCalendarPicker(final MaterialDialog.ButtonCallback callback) {
        new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_calendar, false)
                .positiveText("Ok")
                .negativeText("Отмена")
                .callback(callback).show();
    }
}
