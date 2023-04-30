package com.example.qrinternet.Activities.create;

import android.database.sqlite.SQLiteConstraintException;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.qrinternet.Activities.dialogs.ErrorCodeDialogFragment;
import com.example.qrinternet.Activities.dialogs.ImageSavedDialogFragment;
import com.example.qrinternet.Activities.utility.Methods;
import com.example.qrinternet.Activities.dialogs.OverwriteExistingImageDialogFragment;
import com.example.qrinternet.Activities.dialogs.SavedLimitReachedDialogFragment;
import com.example.qrinternet.Activities.dialogs.SendEmailDialogFragment;
import com.example.qrinternet.Activities.utility.Tags;
import com.example.qrinternet.Activities.api.UploadQRCodesToAPI;
import com.example.qrinternet.R;
import com.example.qrinternet.databinding.FragmentSaveQrCodeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class SaveQRCodeFragment extends Fragment {
    private FragmentSaveQrCodeBinding binding;
    CreateAndSaveViewModel createAndSaveViewModel;

    UploadQRCodesToAPI uploadQRcode;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        createAndSaveViewModel = new ViewModelProvider(this).get(CreateAndSaveViewModel.class);

        binding = FragmentSaveQrCodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSave;
        createAndSaveViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // ADDITIONS ADDED BETWEEN COMMENTS

        Tags.NUM_SAVED_QRCODES = Methods.CountNumberOfSavedImages(Tags.SAVE_PATH);

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

                if (new File(Tags.SAVE_PATH, finalFilename).exists()) {
                    DialogFragment overwriteDialog = new OverwriteExistingImageDialogFragment(finalFilename);
                    overwriteDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Overwrite Message");
                }
                else {
                    if (Tags.NUM_SAVED_QRCODES <= 5) {
                        boolean saved = Methods.SaveBitmapAsPNGToDevice(filename, CreateAndSaveViewModel.getBitmap());
                        if (saved) {
                            uploadQRcode = new UploadQRCodesToAPI(filename);
                            uploadQRcode.execute();
                            try {
                                uploadQRcode.get();
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (uploadQRcode.getResponseCode() == 200) {
                                DialogFragment savedImage = new ImageSavedDialogFragment();
                                savedImage.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Image Saved Message");

                                Tags.NUM_SAVED_QRCODES = Tags.NUM_SAVED_QRCODES + 1;
                            } else {
                                DialogFragment errorDialog = new ErrorCodeDialogFragment(uploadQRcode.getResponseCode(), uploadQRcode.getErrorDetails());
                                errorDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Error Message");
                            }
                        } else {
                            DialogFragment errorDialog = new ErrorCodeDialogFragment(100, null);
                            errorDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Error Message");
                        }
                    } else {
                        DialogFragment saveLimitDialog = new SavedLimitReachedDialogFragment();
                        saveLimitDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Error Message");
                    }
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

        return false;
    }
}
