package com.example.qrinternet.Activities.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.qrinternet.Activities.dialogs.ErrorCodeDialogFragment;
import com.example.qrinternet.Activities.utility.Image;
import com.example.qrinternet.Activities.utility.Methods;
import com.example.qrinternet.Activities.dialogs.SendEmailDialogFragment;
import com.example.qrinternet.Activities.utility.Tags;
import com.example.qrinternet.R;
import com.example.qrinternet.databinding.FragmentViewAllQrCodesBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.util.Objects;
import java.util.Vector;

public class ViewAllQRCodesFragment extends Fragment {

    private FragmentViewAllQrCodesBinding binding;
    ViewAndDeleteViewModel viewAndDeleteViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewAndDeleteViewModel = new ViewModelProvider(this).get(ViewAndDeleteViewModel.class);

        binding = FragmentViewAllQrCodesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        viewAndDeleteViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // ADDITIONS ADDED BETWEEN COMMENTS

        setHasOptionsMenu(true);

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

        ViewAndDeleteViewModel.setImages(new Vector<Image>(Tags.NUM_SAVED_QRCODES));
        ViewAndDeleteViewModel.setBitmaps(new Vector<Bitmap>(Tags.NUM_SAVED_QRCODES));
        GridView gridView = (GridView) root.findViewById(R.id.qrcode_gridView);

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Image temp = document.toObject(Image.class);
                                ViewAndDeleteViewModel.getImages().add(temp);
                                ViewAndDeleteViewModel.getBitmaps().add(Methods.convertToBitmap(temp.getRawData()));
                            }

                            gridView.setAdapter(new QRAdapter());
                        } else {
                            String json = "{\"detail\":\"" + Objects.requireNonNull(task.getException()).getMessage() + "\"}";
                            JSONObject errorDetails = null;
                            try {
                                errorDetails = new JSONObject(json);
                                Log.e("JSON", errorDetails.toString());
                            } catch (Throwable t) {
                                Log.e("JSONObject", "Could not parse JSON");
                            }

                            DialogFragment df = new ErrorCodeDialogFragment(102, errorDetails);
                            df.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Error Message");
                        }

                    }
                });



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ViewAndDeleteViewModel.setPositionOfGrid(position);
                Navigation.findNavController(root).navigate(R.id.action_navigation_viewAll_to_navigation_viewSaved);
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
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_viewAll_to_navigation_instructions);
            return true;
        }
        else if (item.getItemId() == R.id.toolbar_contactCustomerSupport) {
            DialogFragment sendEmailDialog = new SendEmailDialogFragment();
            sendEmailDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Error Message");
            return true;
        }
        else if (item.getItemId() == R.id.toolbar_signOut) {
            Tags.AUTH.signOut();
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_viewAll_to_navigation_login);
            return true;
        }

        return false;
    }

    public class QRAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return ViewAndDeleteViewModel.getImages().size();
        }

        @Override
        public Object getItem(int position) {
            return ViewAndDeleteViewModel.getImages().get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Image image = ViewAndDeleteViewModel.getImages().get(position);

            LayoutInflater inflater = (LayoutInflater) binding.getRoot().getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View image_view = inflater.inflate(R.layout.image_qr_entry, null);

            ImageView image_imageView = image_view.findViewById(R.id.gridChild_imageView);
            image_imageView.setImageBitmap(ViewAndDeleteViewModel.getBitmaps().get(position));

            TextView image_textView = image_view.findViewById(R.id.gridChild_textView);
            image_textView.setText(image.getSource());

            return image_view;
        }
    }
}