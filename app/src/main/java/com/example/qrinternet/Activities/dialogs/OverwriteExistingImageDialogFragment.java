package com.example.qrinternet.Activities.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.qrinternet.Activities.create.CreateAndSaveViewModel;
import com.example.qrinternet.R;

public class OverwriteExistingImageDialogFragment extends DialogFragment {
    private String filename;

    public OverwriteExistingImageDialogFragment(String _filename) {
        filename = _filename;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        builder.setMessage("The file " + filename + "already exists.\n\n" +
                "Please rename your image or delete the already saved one."
        );



        // Create the AlertDialog object and return it
        return builder.create();
    }
}
