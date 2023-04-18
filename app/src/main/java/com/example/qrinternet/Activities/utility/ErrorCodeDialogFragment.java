package com.example.qrinternet.Activities.utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import org.json.JSONObject;

public class ErrorCodeDialogFragment extends DialogFragment {
    private int code;
    private JSONObject details;

    public ErrorCodeDialogFragment(int _code, JSONObject _details) {
        code = _code;
        details = _details;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Send Email to Customer Support", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // acquired from https://stackoverflow.com/questions/2197741/how-to-send-emails-from-my-android-application
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"shastye.7x@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Error in QR.Internet");
                intent.putExtra(Intent.EXTRA_TEXT   , "Error Code: " + code + "\nError Message: " + details.toString());
                try {
                    startActivity(Intent.createChooser(intent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        String message = "";

        switch(code) {
            case 100:
                message = "Image could not be saved\n\nTry renaming the image.\n\n";
                break;
            case 104:
                message = "Image could not be deleted\n\n";
                break;
            case 400:
                message = "Invalid request sent.\n\n";
                break;
            case 404:
                message = "Image was not Found.\n\n";
                break;
            case 415:
                message = "Request content-type not supported or not specified.\n\n";
                break;
            case 422:
                message = "Information could not be validated.\n\n";
                break;
            case 424:
                message = "Image could not be downloaded correctly.\n\n";
                break;
            case 500:
                message = "An internal server error occurred.\n\n";
                break;
            default:
                message = "An unknown error occurred.\n\n";
                break;
        }
        message = message +
                "Please try again. If error persists contact " +
                "customer support with the following information:\n" +
                "    Error Code: " + code + "\n";

        if (code == 0) {
            message = message + "    Error Detail: Exception occurred during process with message: " +
                    details.toString();
        }
        if (code == 100 || code == 104) {
            message = message;
        }
        else {
            message = message + "    Error Detail: " + details.toString();
        }

        builder.setMessage(message);

        // Create the AlertDialog object and return it
        return builder.create();
    }
}

