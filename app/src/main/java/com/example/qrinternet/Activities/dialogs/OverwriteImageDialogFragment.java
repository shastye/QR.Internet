package com.example.qrinternet.Activities.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.qrinternet.Activities.create.CreateAndSaveViewModel;
import com.example.qrinternet.Activities.utility.Image;
import com.example.qrinternet.Activities.utility.Tags;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.Objects;

public class OverwriteImageDialogFragment extends DialogFragment {
    private String filename;
    private Image image;

    public OverwriteImageDialogFragment(String _filename, Image _image) {
        filename = _filename;
        image = _image;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
            .setPositiveButton("Overwrite Image", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("users")
                        .document(Objects.requireNonNull(Objects.requireNonNull(Tags.AUTH.getCurrentUser()).getEmail()))
                            .collection("images")
                            .document(filename)
                                .set(image);
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            })
            .setMessage("The image " + filename + " already exists.\n\n" +
                "Would you like to overwrite the image? ");

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
