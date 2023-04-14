package com.example.qrinternet.Activities.utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class SavedLimitReachedDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
        builder.setMessage(
                "Too many save requests.\n\n" +
                "Your limit of saved QR Codes is 5 images.\n" +
                "Please delete a previously saved image and try again."
        );

        // Create the AlertDialog object and return it
        return builder.create();
    }
}