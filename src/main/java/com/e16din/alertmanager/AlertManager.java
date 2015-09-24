package com.e16din.alertmanager;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class AlertManager {

    private AlertManager() {
    }

    private static final int INVALID_VALUE = -100500;
    public static final int MILLISECONDS_IN_THE_DAY = 86400;

    private static class Holder {
        public static final AlertManager HOLDER_INSTANCE = new AlertManager();
    }

    public static AlertManager manager(Context context) {
        Holder.HOLDER_INSTANCE.setContext(context);
        return Holder.HOLDER_INSTANCE;
    }

    private static int customAlertTitle = R.string.title_alert;
    private static int customErrorTitle = R.string.title_error;

    private Context context = null;
    private ArrayList<String> displayedAlerts = new ArrayList<>();

    private void setContext(Context context) {
        this.context = context;
    }

    private AlertDialogWrapper.Builder createAlertBuilder() {
        return new AlertDialogWrapper.Builder(context);
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

    public boolean isAlertDisplayed(String message) {
        return displayedAlerts.contains(message);
    }

    public void showAlert(String message, boolean isCancelable) {
        showAlert(message, customAlertTitle, isCancelable, null);
    }

    public void showErrorAlert(String message, boolean isCancelable) {
        showAlert(message, customErrorTitle, isCancelable, null);
    }

    public void showErrorAlert(String message, boolean isCancelable,
                               DialogInterface.OnClickListener listener) {
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

                    String updatedTitle = context.getString(title);
                    if (!TextUtils.isEmpty(context.getString(title)))
                        builder.setTitle(updatedTitle);

                    builder.setMessage(message)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (listener != null)
                                        listener.onClick(dialog, which);

                                    displayedAlerts.remove(message);
                                }
                            }).setCancelable(isCancelable).setOnKeyListener(
                            new DialogInterface.OnKeyListener() {
                                @Override
                                public boolean onKey(DialogInterface dialog, int keyCode,
                                                     KeyEvent event) {
                                    if (keyCode == KeyEvent.KEYCODE_BACK ||
                                            event.getAction() == KeyEvent.ACTION_UP) {
                                        if (listener != null)
                                            listener.onClick(dialog, INVALID_VALUE);
                                    }

                                    displayedAlerts.remove(message);
                                    return false;
                                }
                            }).show();
                    displayedAlerts.add(message);
                } catch (WindowManager.BadTokenException e) {
                    Log.e("debug", "error: ", e);
                }
            }
        }, 500);
    }

    public void showAlert(final int message, int title, boolean isCancelable,
                          final DialogInterface.OnClickListener listener) {
        showAlert(context.getString(message), title, isCancelable, listener);
    }

    public void showAlert(final String message, boolean isCancelable,
                          final DialogInterface.OnClickListener listener) {
        try {
            AlertDialogWrapper.Builder builder = createAlertBuilder();

            final String customAlertTitle = context.getString(AlertManager.customAlertTitle);

            if (!TextUtils.isEmpty(customAlertTitle))
                builder.setTitle(customAlertTitle);

            builder.setMessage(message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (listener != null)
                                listener.onClick(dialog, which);
                            displayedAlerts.remove(message);
                        }
                    }).setCancelable(isCancelable).setOnKeyListener(
                    new DialogInterface.OnKeyListener() {

                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK ||
                                    event.getAction() == KeyEvent.ACTION_UP) {
                                if (listener != null)
                                    listener.onClick(dialog, INVALID_VALUE);
                                displayedAlerts.remove(message);
                            }

                            return false;
                        }
                    }).show();
            displayedAlerts.add(message);
        } catch (WindowManager.BadTokenException e) {
            Log.e("debug", "error: ", e);
        }
    }

    public void showAlertYesNo(final String message,
                               final DialogInterface.OnClickListener yesListener) {
        try {
            AlertDialogWrapper.Builder builder = createAlertBuilder();

            final String customAlertTitle = context.getString(AlertManager.customAlertTitle);

            if (!TextUtils.isEmpty(customAlertTitle))
                builder.setTitle(customAlertTitle);

            builder.setMessage(message)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (yesListener != null)
                                yesListener.onClick(dialog, which);

                            displayedAlerts.remove(message);
                        }
                    }).setNegativeButton(android.R.string.no, null).show();
            displayedAlerts.add(message);
        } catch (WindowManager.BadTokenException e) {
            Log.e("debug", "error: ", e);
        }
    }

    public void showDialogList(final String title, final CharSequence[] items, final TextView tv,
                               final DialogInterface.OnClickListener listener) {
        try {
            AlertDialogWrapper.Builder builder = createAlertBuilder();

            if (!TextUtils.isEmpty(title))
                builder.setTitle(title);

            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (tv != null && which >= 0)
                        tv.setText(items[which]);
                    if (listener != null)
                        listener.onClick(dialog, which);

                    displayedAlerts.remove(title);
                }
            }).show();
            displayedAlerts.add(title);
        } catch (WindowManager.BadTokenException e) {
            Log.e("debug", "error: ", e);
        }
    }

    public void showRadioList(final String title, final CharSequence[] items,
                              final DialogInterface.OnClickListener listener) {
        showRadioList(title, items, null, listener);
    }

    public void showRadioList(final CharSequence[] items,
                              final DialogInterface.OnClickListener listener) {
        showRadioList(null, items, null, listener);
    }

    public void showRadioList(final CharSequence[] items, final TextView tv,
                              final DialogInterface.OnClickListener listener) {
        showRadioList(null, items, tv, listener);
    }

    public void showRadioList(final String title, final CharSequence[] items, final TextView tv,
                              final DialogInterface.OnClickListener listener) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);

        if (!TextUtils.isEmpty(title))
            builder.title(title);

        builder.items(items)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which,
                                               CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/

                        if (tv != null && which >= 0)
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

    public void showDialogList(final CharSequence[] items,
                               DialogInterface.OnClickListener listener) {
        showDialogList(context.getString(customAlertTitle), items, null, listener);
    }

    public void showDialogList(final CharSequence[] items, final TextView tv,
                               DialogInterface.OnClickListener listener) {
        showDialogList(context.getString(customAlertTitle), items, tv, listener);
    }

    public void showTimePicker(int hours, int minutes,
                               final TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        final TimePicker timePicker = new TimePicker(context);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(hours);
        timePicker.setCurrentMinute(minutes);

        AlertDialogWrapper.Builder builder = createAlertBuilder();

        final String title = context.getString(R.string.set_time);
        if (!TextUtils.isEmpty(title))
            builder.setTitle(title);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onTimeSetListener.onTimeSet(timePicker, timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());
            }
        })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setView(timePicker).show();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showDatePicker(final String title, final int year, final int month, final int day,
                               long maxDate,
                               final DatePickerDialog.OnDateSetListener onDateSetListener) {
        final DatePicker datePicker = new DatePicker(context);
        datePicker.updateDate(year, month - 1, day);
        datePicker.setCalendarViewShown(false);
        if (maxDate > 0)
            datePicker.setMaxDate(maxDate);

        AlertDialogWrapper.Builder builder = createAlertBuilder();

        if (!TextUtils.isEmpty(title))
            builder.setTitle(title);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("AlertManager", "year: " + datePicker.getYear() + " month: " +
                        datePicker.getMonth() + 1 + " day: " + datePicker.getDayOfMonth());
                onDateSetListener.onDateSet(datePicker,
                        datePicker.getYear(),
                        datePicker.getMonth() + 1,
                        datePicker.getDayOfMonth());
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setView(datePicker).show();
    }

    public void showDatePicker(final String title, final int year, final int month, final int day,
                               final DatePickerDialog.OnDateSetListener onDateSetListener) {
        showDatePicker(title, year, month, day, DateTime.now().plusYears(1).getMillis(),
                onDateSetListener);
    }

    public void showBirthDatePicker(final String title, final int year, final int month,
                                    final int day,
                                    final DatePickerDialog.OnDateSetListener onDateSetListener) {
        showDatePicker(title, year, month, day, DateTime.now().getMillis(), onDateSetListener);
    }

    public void showDatePicker(final int year, final int month, final int day,
                               final DatePickerDialog.OnDateSetListener onDateSetListener) {
        showDatePicker(context.getString(R.string.check_date), year, month, day,
                DateTime.now().plusYears(1).getMillis(), onDateSetListener);
    }

    public void showBirthDatePicker(final int year, final int month, final int day,
                                    final DatePickerDialog.OnDateSetListener onDateSetListener) {
        showDatePicker(context.getString(R.string.check_date), year, month, day,
                DateTime.now().getMillis(), onDateSetListener);
    }


    public void showCalendarPicker(final AlertDialogCallback<DateTime> callback) {
        new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_calendar, false)
                .positiveText("Ok")
                .negativeText("Отмена")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        if (callback != null) {
                            callback.onNegative();//todo return date
                        }
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        if (callback != null) {
                            callback.onNeutral();//todo return date
                        }
                    }

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        if (callback != null) {
                            callback.onPositive();//todo return date
                        }
                    }
                }).show();
    }

    public void showMessageEditor(String message, final AlertDialogCallback<String> callback) {
        showMessageEditor(null, message, -1, callback);
    }

    public void showMessageEditor(String message, int inputType, final AlertDialogCallback<String> callback) {
        showMessageEditor(null, message, inputType, callback);
    }

    public void showMessageEditor(String title, String message, final AlertDialogCallback<String> callback) {
        showMessageEditor(title, message, -1, callback);
    }

    public void showMessageEditor(String title, String message, int inputType,
                                  final AlertDialogCallback<String> callback) {
        final View customView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_message,
                null);
        final EditText etMessage = (EditText) customView.findViewById(R.id.etMessage);
        if (inputType >= 0)
            etMessage.setInputType(inputType);
        etMessage.setText(message);
        etMessage.setSelection(etMessage.length());

        new MaterialDialog.Builder(context)
                .title(title)
                .customView(customView, false)
                .positiveText("Готово")
                .negativeText("Отмена")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);

                        String text = etMessage.getText().toString();
                        if (callback != null)
                            callback.onPositive(text);
                    }
                }).show();
    }
}
