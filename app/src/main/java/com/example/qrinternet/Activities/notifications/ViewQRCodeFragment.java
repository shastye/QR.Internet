package com.example.qrinternet.Activities.notifications;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.qrinternet.Activities.utility.Tags;
import com.example.qrinternet.R;
import com.example.qrinternet.databinding.FragmentViewQrCodeBinding;

import java.util.Vector;

public class ViewQRCodeFragment extends Fragment {
    private FragmentViewQrCodeBinding binding;
    NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentViewQrCodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textViewSaved;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // ADDITIONS ADDED BETWEEN COMMENTS

        ImageView viewSavedQRCode = (ImageView) root.findViewById(R.id.ViewSavedQRCode_imageView);
        viewSavedQRCode.setImageBitmap(notificationsViewModel.getBitmapsOfQRCodes().get(notificationsViewModel.getPositionOfGrid()));
        TextView viewSavedText = (TextView) root.findViewById(R.id.savedFilename_textView);
        int lastIndex = notificationsViewModel.getImagesFromAPI().get(notificationsViewModel.getPositionOfGrid()).source.lastIndexOf('/');
        String name = notificationsViewModel.getImagesFromAPI().get(notificationsViewModel.getPositionOfGrid()).source.substring(lastIndex + 1);
        viewSavedText.setText(name);

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
