package com.e16din.alertmanager;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;

public class AlertManager {

    public static final String TAG_CALENDAR_DIALOG = "CalendarDialog";

    private AlertManager() {
    }

    private static final int INVALID_VALUE = -100500;
    public static final int MILLISECONDS_IN_THE_DAY = 86400;

    private static class Holder {
        public static final AlertManager HOLDER_INSTANCE = new AlertManager();
    }

    public static AlertManager manager(@NonNull Context context) {
        Holder.HOLDER_INSTANCE.setContext(context);
        return Holder.HOLDER_INSTANCE;
    }

    private static int customAlertTitle = R.string.title_alert;
    private static int customErrorTitle = R.string.title_error;

    private Context context = null;
    private ArrayList<String> displayedAlerts = new ArrayList<>();

    private void setContext(@NonNull Context context) {
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
                               @NonNull final DialogInterface.OnClickListener yesListener) {
        try {
            AlertDialogWrapper.Builder builder = createAlertBuilder();

            final String customAlertTitle = context.getString(AlertManager.customAlertTitle);

            if (!TextUtils.isEmpty(customAlertTitle))
                builder.setTitle(customAlertTitle);

            builder.setMessage(message)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            yesListener.onClick(dialog, which);

                            displayedAlerts.remove(message);
                        }
                    }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    displayedAlerts.remove(message);
                }
            }).show();
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
                               final android.app.DatePickerDialog.OnDateSetListener onDateSetListener) {
        final android.widget.DatePicker datePicker = new android.widget.DatePicker(context);
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
                               final android.app.DatePickerDialog.OnDateSetListener onDateSetListener) {
        showDatePicker(title, year, month, day, DateTime.now().plusYears(1).getMillis(),
                onDateSetListener);
    }

    public void showDatePicker(final int year, final int month, final int day,
                               final android.app.DatePickerDialog.OnDateSetListener onDateSetListener) {
        showDatePicker(context.getString(R.string.check_date), year, month, day,
                DateTime.now().plusYears(1).getMillis(), onDateSetListener);
    }

    public void showBirthDatePicker(final String title, final int year, final int month,
                                    final int day,
                                    final android.app.DatePickerDialog.OnDateSetListener onDateSetListener) {
        showDatePicker(title, year, month, day, DateTime.now().getMillis(), onDateSetListener);
    }

    public void showBirthDatePicker(final int year, final int month, final int day,
                                    final android.app.DatePickerDialog.OnDateSetListener onDateSetListener) {
        showDatePicker(context.getString(R.string.check_date), year, month, day,
                DateTime.now().getMillis(), onDateSetListener);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showCalendarPicker(@NonNull FragmentManager fm,
                                   boolean showYearPickerFirst,
                                   boolean dismissOnPause,
                                   @NonNull final AlertDialogCallback<DateTime> callback) {

        final Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        DateTime dateTime = new DateTime(year, monthOfYear, dayOfMonth, 0, 0);
                        callback.onPick(dateTime);

                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.showYearPickerFirst(showYearPickerFirst);
        dpd.dismissOnPause(dismissOnPause);

        dpd.show(fm, TAG_CALENDAR_DIALOG);
    }

    public void showMessageEditor(String message, final AlertDialogCallback<String> callback) {
        showMessageEditor(context.getString(customAlertTitle), null, message, -1, false, callback);
    }

    public void showMessageEditor(String message, int inputType, final AlertDialogCallback<String> callback) {
        showMessageEditor(context.getString(customAlertTitle), null, message, inputType, false, callback);
    }

    public void showMessageEditor(String hint, String message, final AlertDialogCallback<String> callback) {
        showMessageEditor(context.getString(customAlertTitle), hint, message, -1, false, callback);
    }

    public void showSingleLineMessageEditor(String message, final AlertDialogCallback<String> callback) {
        showMessageEditor(context.getString(customAlertTitle), null, message, -1, true, callback);
    }

    public void showSingleLineMessageEditor(String message, int inputType, final AlertDialogCallback<String> callback) {
        showMessageEditor(context.getString(customAlertTitle), null, message, inputType, true, callback);
    }

    public void showSingleLineMessageEditor(String hint, String message, final AlertDialogCallback<String> callback) {
        showMessageEditor(context.getString(customAlertTitle), hint, message, -1, true, callback);
    }

    public void showTextPasswordEditor(String message, final AlertDialogCallback<String> callback) {
        showMessageEditor(context.getString(customAlertTitle), context.getString(R.string.enter_password), message,
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD, true, callback);
    }

    public void showTextPasswordEditor(String message, String hint, final AlertDialogCallback<String> callback) {
        showMessageEditor(context.getString(customAlertTitle), hint, message,
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD, true, callback);
    }

    public void showNumberPasswordEditor(String message, final AlertDialogCallback<String> callback) {
        showMessageEditor(context.getString(customAlertTitle), context.getString(R.string.enter_password), message,
                InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD, true, callback);
    }

    public void showNumberPasswordEditor(String message, String hint, final AlertDialogCallback<String> callback) {
        showMessageEditor(context.getString(customAlertTitle), hint, message,
                InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD, true, callback);
    }

    //TODO: create Builder: setTitle, setMessage, setListener

    public void showMessageEditor(String title, String hint, String message, int inputType, boolean singleLine,
                                  final AlertDialogCallback<String> callback) {
        final View customView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_message,
                null);

        final TextInputLayout tilMessage = (TextInputLayout) customView.findViewById(R.id.tilMessage);
        final EditText etMessage = (EditText) customView.findViewById(R.id.etMessage);

        //etMessage.setHint(hint);
        etMessage.setText(message);
        etMessage.setSelection(etMessage.length());

        tilMessage.setHint(hint);

        if (inputType >= 0)
            etMessage.setInputType(inputType);

        if (singleLine)
            etMessage.setSingleLine();


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
