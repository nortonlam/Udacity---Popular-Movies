package com.nortonlam.popularmovies.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by norton
 *
 * Created date: 10/14/15.
 */
public class MessageDialog extends DialogFragment {
    public MessageDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String title = args.getString("title", "");
        String message = args.getString("message", "");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .create();
    }

    public static MessageDialog getInstance(String title, String message) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);

        MessageDialog dialog = new MessageDialog();
        dialog.setArguments(args);

        return dialog;
    }
}