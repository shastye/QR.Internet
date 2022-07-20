package com.example.qrinternet.Activities.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.qrinternet.Activities.utility.RetrieveFromAPI;
import com.example.qrinternet.R;
import com.example.qrinternet.databinding.FragmentDashboardBinding;

import java.util.concurrent.ExecutionException;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // ADDITIONS ADDED BETWEEN COMMENTS

        Button button = (Button) root.findViewById(R.id.CreateQRCode_button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RetrieveFromAPI temp = new RetrieveFromAPI();
                temp.execute();
                try {
                    temp.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Bitmap bitmap = temp.getBitmap();

                ImageView image = root.findViewById(R.id.ViewQRCode_imageView);
                image.setImageBitmap(bitmap);
            }
        });


        // END ADDITIONS

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}