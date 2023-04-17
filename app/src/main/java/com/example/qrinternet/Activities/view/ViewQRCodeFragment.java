package com.example.qrinternet.Activities.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.qrinternet.Activities.utility.DeleteImageFromAPI;
import com.example.qrinternet.Activities.utility.ErrorCodeDialogFragment;
import com.example.qrinternet.Activities.utility.ImageDeletedDialogFragment;
import com.example.qrinternet.Activities.utility.ImageDetails;
import com.example.qrinternet.Activities.utility.Tags;
import com.example.qrinternet.R;
import com.example.qrinternet.databinding.FragmentViewQrCodeBinding;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class ViewQRCodeFragment extends Fragment {
    private FragmentViewQrCodeBinding binding;
    ViewAndDeleteViewModel viewAndDeleteViewModel;

    DeleteImageFromAPI deleteQRCode;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewAndDeleteViewModel = new ViewModelProvider(this).get(ViewAndDeleteViewModel.class);

        binding = FragmentViewQrCodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textViewSaved;
        viewAndDeleteViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // ADDITIONS ADDED BETWEEN COMMENTS

        ImageView viewSavedQRCode = (ImageView) root.findViewById(R.id.ViewSavedQRCode_imageView);
        viewSavedQRCode.setImageBitmap(viewAndDeleteViewModel.getBitmapsOfQRCodes().get(viewAndDeleteViewModel.getPositionOfGrid()));
        TextView viewSavedText = (TextView) root.findViewById(R.id.savedFilename_textView);
        int lastIndex = viewAndDeleteViewModel.getImagesFromAPI().get(viewAndDeleteViewModel.getPositionOfGrid()).source.lastIndexOf('/');
        String name = viewAndDeleteViewModel.getImagesFromAPI().get(viewAndDeleteViewModel.getPositionOfGrid()).source.substring(lastIndex + 1);
        viewSavedText.setText(name);

        Button deleteQRButton = (Button) root.findViewById(R.id.deleteQRCode_button);

        deleteQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tags.NUM_SAVED_QRCODES = Tags.NUM_SAVED_QRCODES - 1;

                ImageDetails qrCode = viewAndDeleteViewModel.getImagesFromAPI().get(viewAndDeleteViewModel.getPositionOfGrid());
                deleteQRCode = new DeleteImageFromAPI(qrCode);
                deleteQRCode.execute();
                try {
                    deleteQRCode.get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                if (deleteQRCode.getResponseCode() == 204) {
                    DialogFragment savedImage = new ImageDeletedDialogFragment();
                    savedImage.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Image Deleted Message");

                    Navigation.findNavController(root).navigate(R.id.action_navigation_viewSaved_to_navigation_notifications);
                } else {
                    Tags.NUM_SAVED_QRCODES = Tags.NUM_SAVED_QRCODES + 1;

                    DialogFragment errorDialog = new ErrorCodeDialogFragment(deleteQRCode.getResponseCode(), deleteQRCode.getErrorDetails());
                    errorDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Error Message");
                }
            }
        });

        //

        // END ADDITIONS


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
