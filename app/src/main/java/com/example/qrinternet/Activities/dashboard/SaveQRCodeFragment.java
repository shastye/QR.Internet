package com.example.qrinternet.Activities.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
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

import com.example.qrinternet.Activities.notifications.NotificationsViewModel;
import com.example.qrinternet.Activities.utility.ErrorCodeDialogFragment;
import com.example.qrinternet.Activities.utility.ImageSavedDialogFragment;
import com.example.qrinternet.Activities.utility.Methods;
import com.example.qrinternet.Activities.utility.SavedLimitReachedDialogFragment;
import com.example.qrinternet.Activities.utility.Tags;
import com.example.qrinternet.Activities.utility.UploadQRCodesToAPI;
import com.example.qrinternet.R;
import com.example.qrinternet.databinding.FragmentSaveQrCodeBinding;
import com.example.qrinternet.databinding.FragmentViewQrCodeBinding;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class SaveQRCodeFragment extends Fragment {
    private FragmentSaveQrCodeBinding binding;
    DashboardViewModel dashboardViewModel;

    UploadQRCodesToAPI uploadQRcode;

    ImageView qrCode;
    TextView fn_tv;
    EditText fn_et;
    Button saveQRbutton;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentSaveQrCodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSave;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // ADDITIONS ADDED BETWEEN COMMENTS

        Tags.NUM_SAVED_QRCODES = Methods.CountNumberOfSavedImages(Tags.SAVE_PATH);

        qrCode = (ImageView) root.findViewById(R.id.ViewQRCode_imageView);
        fn_tv = (TextView) root.findViewById(R.id.filename_textView);
        fn_et = (EditText) root.findViewById(R.id.filename_editText);
        fn_et.setText("");
        saveQRbutton = (Button) root.findViewById(R.id.SaveQRCode_button);

        qrCode.setImageBitmap(dashboardViewModel.getBitmap());

        saveQRbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Tags.NUM_SAVED_QRCODES = Tags.NUM_SAVED_QRCODES + 1;

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

                if (Tags.NUM_SAVED_QRCODES <= 5) {
                    boolean saved = Methods.SaveBitmapAsPNGToDevice(filename, dashboardViewModel.getBitmap());
                    if (saved) {
                        uploadQRcode = new UploadQRCodesToAPI(filename, dashboardViewModel.getBinaryData());
                        uploadQRcode.execute();
                        try {
                            uploadQRcode.get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (uploadQRcode.getResponseCode() == 200) {
                            DialogFragment savedImage = new ImageSavedDialogFragment();
                            savedImage.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Image Saved Message");
                        } else {
                            Tags.NUM_SAVED_QRCODES = Tags.NUM_SAVED_QRCODES - 1;

                            DialogFragment errorDialog = new ErrorCodeDialogFragment(uploadQRcode.getResponseCode(), uploadQRcode.getErrorDetails());
                            errorDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Error Message");
                        }
                    }
                    else {
                        Tags.NUM_SAVED_QRCODES = Tags.NUM_SAVED_QRCODES - 1;

                        DialogFragment errorDialog = new ErrorCodeDialogFragment(100, null);
                        errorDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Error Message");
                    }
                }
                else {
                    Tags.NUM_SAVED_QRCODES = Tags.NUM_SAVED_QRCODES - 1;

                    DialogFragment saveLimitDialog = new SavedLimitReachedDialogFragment();
                    saveLimitDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Error Message");
                }
            }
        });

        //

        // END ADDITIONS


        return root;
    }
}
