package com.example.qrinternet.Activities.notifications;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.qrinternet.Activities.utility.ErrorCodeDialogFragment;
import com.example.qrinternet.Activities.utility.ImageDetails;
import com.example.qrinternet.Activities.utility.ListAllQRCodesFromAPI;
import com.example.qrinternet.R;
import com.example.qrinternet.databinding.FragmentNotificationsBinding;

import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    ListAllQRCodesFromAPI listAllQRCodes;
    NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // ADDITIONS ADDED BETWEEN COMMENTS

        listAllQRCodes = new ListAllQRCodesFromAPI();
        listAllQRCodes.execute();
        try {
            listAllQRCodes.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        if (listAllQRCodes.getResponseCode() == 200) {
            notificationsViewModel.setImagesFromAPI(listAllQRCodes.getImagesFromAPI());
            notificationsViewModel.setBitmapsOfQRCodes(new Vector<Bitmap>(5));

            for (int i = 0; i < notificationsViewModel.getImagesFromAPI().size(); i++) {
                ImageDetails image = notificationsViewModel.getImagesFromAPI().get(i);
                Bitmap bitmap = BitmapFactory.decodeFile(image.source);
                notificationsViewModel.getBitmapsOfQRCodes().add(bitmap);
            }
        }
        else {
            DialogFragment errorDialog = new ErrorCodeDialogFragment(listAllQRCodes.getResponseCode(), listAllQRCodes.getErrorDetails());
            errorDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Error Message");
        }




        GridView gridView = (GridView) root.findViewById(R.id.qrcode_gridView);
        gridView.setAdapter(new QRAdapter());



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                notificationsViewModel.setPositionOfGrid(position);
                Navigation.findNavController(root).navigate(R.id.action_navigation_notifications_to_navigation_viewSaved);
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

    public class QRAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return notificationsViewModel.getImagesFromAPI().size();
        }

        @Override
        public Object getItem(int position) {
            return notificationsViewModel.getImagesFromAPI().get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageDetails image = notificationsViewModel.getImagesFromAPI().get(position);

            LayoutInflater inflater = (LayoutInflater) binding.getRoot().getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View image_view = inflater.inflate(R.layout.image_qr_entry, null);

            ImageView image_imageView = image_view.findViewById(R.id.gridChild_imageView);
            image_imageView.setImageBitmap(notificationsViewModel.getBitmapsOfQRCodes().get(position));

            TextView image_textView = image_view.findViewById(R.id.gridChild_textView);
            int lastIndex = image.source.lastIndexOf('/');
            String name = image.source.substring(lastIndex + 1);
            image_textView.setText(name);

            return image_view;
        }
    }
}