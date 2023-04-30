package com.example.qrinternet.Activities.view;

import android.os.Bundle;
import android.util.Log;
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

import com.example.qrinternet.Activities.dialogs.ErrorCodeDialogFragment;
import com.example.qrinternet.Activities.dialogs.StringDialogFragment;
import com.example.qrinternet.Activities.utility.Image;
import com.example.qrinternet.Activities.dialogs.SendEmailDialogFragment;
import com.example.qrinternet.Activities.utility.Tags;
import com.example.qrinternet.R;
import com.example.qrinternet.databinding.FragmentViewQrCodeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class ViewQRCodeFragment extends Fragment {
    private FragmentViewQrCodeBinding binding;
    ViewAndDeleteViewModel viewAndDeleteViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewAndDeleteViewModel = new ViewModelProvider(this).get(ViewAndDeleteViewModel.class);

        binding = FragmentViewQrCodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textViewSaved;
        viewAndDeleteViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        setHasOptionsMenu(true);

        // ADDITIONS ADDED BETWEEN COMMENTS

        final FirebaseFirestore[] db = {FirebaseFirestore.getInstance()};
        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        CollectionReference query = db[0].collection("users").document(Objects.requireNonNull(email)).collection("images");

        ImageView viewSavedQRCode = (ImageView) root.findViewById(R.id.ViewSavedQRCode_imageView);
        viewSavedQRCode.setImageBitmap(ViewAndDeleteViewModel.getBitmaps().get(ViewAndDeleteViewModel.getPositionOfGrid()));
        TextView viewSavedText = (TextView) root.findViewById(R.id.savedFilename_textView);
        int lastIndex = ViewAndDeleteViewModel.getImages().get(ViewAndDeleteViewModel.getPositionOfGrid()).getSource().lastIndexOf('/');
        String name = ViewAndDeleteViewModel.getImages().get(ViewAndDeleteViewModel.getPositionOfGrid()).getSource().substring(lastIndex + 1);
        viewSavedText.setText(name);

        Button deleteQRButton = (Button) root.findViewById(R.id.deleteQRCode_button);

        deleteQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Image qrCode = ViewAndDeleteViewModel.getImages().get(ViewAndDeleteViewModel.getPositionOfGrid());

                query.document(qrCode.getSource())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                DialogFragment df = new StringDialogFragment("Image deleted successfully.");
                                df.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Image Deleted Message");

                                Navigation.findNavController(root).navigate(R.id.action_navigation_viewSaved_to_navigation_notifications);
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

                                DialogFragment df = new ErrorCodeDialogFragment(104, errorDetails);
                                df.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Error Message");
                            }
                        });
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
