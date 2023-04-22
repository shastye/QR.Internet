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

import com.example.qrinternet.Activities.utility.Tags;
import com.example.qrinternet.R;

public class SendEmailDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.send_email_dialog, null);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Send Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Spinner spinner = view.findViewById(R.id.spinner);
                        EditText editText = view.findViewById(R.id.editText);

                        // acquired from https://stackoverflow.com/questions/2197741/how-to-send-emails-from-my-android-application
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("message/rfc822");
                        intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{Tags.EMAIL});
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Message from User for QR.Internet: " + spinner.getSelectedItem().toString());
                        intent.putExtra(Intent.EXTRA_TEXT   , "User Message: " + editText.getText().toString());
                        try {
                            startActivity(Intent.createChooser(intent, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });



        return builder.create();
    }
}
