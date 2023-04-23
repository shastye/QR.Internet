package com.example.qrinternet.Activities.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class AccountAlreadyExistsDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
        builder.setMessage("The username provided already has an account\n" +
                "in our records.\n\n" +
                "Please, log in or try a different username.");

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
