package com.example.qrinternet.Activities.create;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.qrinternet.Activities.dialogs.ErrorCodeDialogFragment;
import com.example.qrinternet.Activities.dialogs.SendEmailDialogFragment;
import com.example.qrinternet.Activities.dialogs.StringDialogFragment;
import com.example.qrinternet.Activities.utility.Image;
import com.example.qrinternet.Activities.utility.Tags;
import com.example.qrinternet.R;
import com.example.qrinternet.databinding.FragmentSaveQrCodeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.json.JSONObject;

import java.util.Objects;

public class SaveQRCodeFragment extends Fragment {
    private FragmentSaveQrCodeBinding binding;
    CreateAndSaveViewModel createAndSaveViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        createAndSaveViewModel = new ViewModelProvider(this).get(CreateAndSaveViewModel.class);

        binding = FragmentSaveQrCodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSave;
        createAndSaveViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // ADDITIONS ADDED BETWEEN COMMENTS

        final FirebaseFirestore[] db = {FirebaseFirestore.getInstance()};
        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        CollectionReference query = db[0].collection("users").document(Objects.requireNonNull(email)).collection("images");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    AggregateQuerySnapshot snapshot = task.getResult();
                    Tags.NUM_SAVED_QRCODES = (int) snapshot.getCount();
                } else {
                    Tags.NUM_SAVED_QRCODES = 0;
                }
            }
        });

        ImageView qrCode = (ImageView) root.findViewById(R.id.ViewQRCode_imageView);
        EditText fn_et = (EditText) root.findViewById(R.id.filename_editText);
        fn_et.setText("");
        Button saveQRbutton = (Button) root.findViewById(R.id.SaveQRCode_button);

        qrCode.setImageBitmap(CreateAndSaveViewModel.getBitmap());

        setHasOptionsMenu(true);

        saveQRbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                String filename = fn_et.getText().toString();
                filename = filename.trim();
                if (filename.equals("")) {
                    filename = "qrcode_" + Tags.NUM_SAVED_QRCODES + ".png";
                }
                else if (!filename.endsWith(".png")) {
                    int index = filename.indexOf('.');
                    if (index == -1) {
                        index = filename.length();
                    }
                    filename = filename.subSequence(0, index).toString();
                    filename = filename + ".png";
                }
                String finalFilename = filename;

                final boolean[] imageExists = {false};
                db[0] = FirebaseFirestore.getInstance();
                DocumentReference image = query.document(finalFilename);
                image.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.exists()) {
                                imageExists[0] = true;
                            }
                        }
                    }
                });

                if (imageExists[0]) {
                    // TODO: change to actually be able to overwrite.

                    DialogFragment df = new StringDialogFragment("The file " + finalFilename + "already exists.\n\n" +
                            "Please rename your image or delete the already saved one.");
                    df.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Overwrite Message");
                }
                else {
                    Image qrCode = new Image(finalFilename, CreateAndSaveViewModel.getBinaryData());
                    image.set(qrCode)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    DialogFragment df = new StringDialogFragment("Image saved successfully.");
                                    df.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Image Saved Message");

                                    Tags.NUM_SAVED_QRCODES = Tags.NUM_SAVED_QRCODES + 1;
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    String json = "{\"detail\":\"" + e.getMessage() + "\"}";
                                    JSONObject errorDetails = null;
                                    try {
                                        errorDetails = new JSONObject(json);
                                        Log.e("JSON", errorDetails.toString());
                                    } catch (Throwable t) {
                                        Log.e("JSONObject", "Could not parse JSON");
                                    }

                                    DialogFragment errorDialog = new ErrorCodeDialogFragment(100, errorDetails);
                                    errorDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Error Message");
                                }
                            });
                }
            }
        });

        //

        // END ADDITIONS


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        BottomNavigationView navBar = Objects.requireNonNull(getActivity()).findViewById(R.id.nav_view);
        navBar.setVisibility(View.VISIBLE);

        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolar_instructions) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_save_to_navigation_instructions);
            return true;
        }
        else if (item.getItemId() == R.id.toolbar_contactCustomerSupport) {
            DialogFragment sendEmailDialog = new SendEmailDialogFragment();
            sendEmailDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Error Message");
            return true;
        }
        else if (item.getItemId() == R.id.toolbar_signOut) {
            Tags.AUTH.signOut();
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_save_to_navigation_login);
            return true;
        }

        return false;
    }
}
