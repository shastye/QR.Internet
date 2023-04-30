package com.example.qrinternet.Activities.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.qrinternet.Activities.api.DeleteImageFromAPI;
import com.example.qrinternet.Activities.dialogs.ErrorCodeDialogFragment;
import com.example.qrinternet.Activities.dialogs.StringDialogFragment;
import com.example.qrinternet.Activities.utility.ImageDetails;
import com.example.qrinternet.Activities.dialogs.SendEmailDialogFragment;
import com.example.qrinternet.Activities.utility.Tags;
import com.example.qrinternet.R;
import com.example.qrinternet.databinding.FragmentViewQrCodeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        setHasOptionsMenu(true);

        // ADDITIONS ADDED BETWEEN COMMENTS

        ImageView viewSavedQRCode = (ImageView) root.findViewById(R.id.ViewSavedQRCode_imageView);
        viewSavedQRCode.setImageBitmap(ViewAndDeleteViewModel.getBitmapsOfQRCodes().get(ViewAndDeleteViewModel.getPositionOfGrid()));
        TextView viewSavedText = (TextView) root.findViewById(R.id.savedFilename_textView);
        int lastIndex = ViewAndDeleteViewModel.getImagesFromAPI().get(ViewAndDeleteViewModel.getPositionOfGrid()).source.lastIndexOf('/');
        String name = ViewAndDeleteViewModel.getImagesFromAPI().get(ViewAndDeleteViewModel.getPositionOfGrid()).source.substring(lastIndex + 1);
        viewSavedText.setText(name);

        Button deleteQRButton = (Button) root.findViewById(R.id.deleteQRCode_button);

        deleteQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDetails qrCode = ViewAndDeleteViewModel.getImagesFromAPI().get(ViewAndDeleteViewModel.getPositionOfGrid());
                deleteQRCode = new DeleteImageFromAPI(qrCode);
                deleteQRCode.execute();
                try {
                    deleteQRCode.get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                if (deleteQRCode.getResponseCode() == 204) {
                    DialogFragment df = new StringDialogFragment("Image deleted successfully.");
                    df.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Image Deleted Message");

                    Tags.NUM_SAVED_QRCODES = Tags.NUM_SAVED_QRCODES - 1;

                    Navigation.findNavController(root).navigate(R.id.action_navigation_viewSaved_to_navigation_notifications);
                } else {
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
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_viewSaved_to_navigation_instructions);
            return true;
        }
        else if (item.getItemId() == R.id.toolbar_contactCustomerSupport) {
            DialogFragment sendEmailDialog = new SendEmailDialogFragment();
            sendEmailDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Error Message");
            return true;
        }
        else if (item.getItemId() == R.id.toolbar_signOut) {
            Tags.AUTH.signOut();
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_viewSaved_to_navigation_login);
            return true;
        }

        return false;
    }
}
