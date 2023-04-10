package com.example.qrinternet.Activities.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.qrinternet.Activities.utility.GetQRCodeFromAPI;
import com.example.qrinternet.R;
import com.example.qrinternet.databinding.FragmentDashboardBinding;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    ImageView qrCode;
    Button createQRbutton;
    Button saveQRbutton;
    TextView ssid_tv;
    EditText ssid_et;
    TextView pw_tv;
    EditText pw_et;
    TextView sec_tv;
    Spinner sec_s;
    TextView hid_tv;
    CheckBox hid_cb;
    TextView fn_tv;
    EditText fn_et;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        // ADDITIONS ADDED BETWEEN COMMENTS


        //////////////////////////
        //  Generating QR Code  //
        //////////////////////////

        qrCode = (ImageView) root.findViewById(R.id.ViewQRCode_imageView);
        ssid_tv = (TextView) root.findViewById(R.id.ssid_textView);
        ssid_et = (EditText) root.findViewById(R.id.ssid_editText);
        pw_tv = (TextView) root.findViewById(R.id.password_textView);
        pw_et = (EditText) root.findViewById(R.id.password_editText);
        sec_tv = (TextView) root.findViewById(R.id.security_textView);
        sec_s = (Spinner) root.findViewById(R.id.security_spinner);
        hid_tv = (TextView) root.findViewById(R.id.hidden_textView);
        hid_cb = (CheckBox) root.findViewById(R.id.hidden_checkBox);
        fn_tv = (TextView) root.findViewById(R.id.filename_textView);
        fn_et = (EditText) root.findViewById(R.id.filename_editText);
        createQRbutton = (Button) root.findViewById(R.id.CreateQRCode_button);
        saveQRbutton = (Button) root.findViewById(R.id.SaveQRCode_button);

        showScreen1();

        createQRbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String ssid = ssid_et.getText().toString();
                String password = pw_et.getText().toString();
                String security = sec_s.getSelectedItem().toString();
                if (security.equals("None")) {
                    security = "nopass";
                    password = "";
                }
                String hidden = "false";
                if (hid_cb.isChecked()) {
                    hidden = "true";
                }
                String finalHidden = hidden;

                if ((!ssid.equals("") && !password.equals("")) || (!ssid.equals("") && security.equals("nopass"))) {
                    GetQRCodeFromAPI temp = new GetQRCodeFromAPI(ssid, password, security, finalHidden);
                    temp.execute();
                    try {
                        temp.get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (temp.getResponseCode() == 200) {
                        Bitmap bitmap = temp.getBitmap();
                        qrCode.setImageBitmap(bitmap);

                        showScreen2();
                    }
                    else {
                        DialogFragment errorDialog = new ErrorCodeDialogFragment(temp.getResponseCode(), temp.getResponseDetails());
                        errorDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Error Message");
                    }
                }

            }
        });


        //////////////////////
        //  Saving QR Code  //
        //////////////////////


        saveQRbutton.setOnClickListener(new View.OnClickListener()
        {
              @Override
              public void onClick(View v) {

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

    private void showScreen1() {
        qrCode.setVisibility(View.INVISIBLE);
        ssid_tv.setVisibility(View.VISIBLE);
        ssid_et.setVisibility(View.VISIBLE);
        pw_tv.setVisibility(View.VISIBLE);
        pw_et.setVisibility(View.VISIBLE);
        sec_tv.setVisibility(View.VISIBLE);
        sec_s.setVisibility(View.VISIBLE);
        hid_tv.setVisibility(View.VISIBLE);
        hid_cb.setVisibility(View.VISIBLE);
        fn_tv.setVisibility(View.INVISIBLE);
        fn_et.setVisibility(View.INVISIBLE);
        createQRbutton.setVisibility(View.VISIBLE);
        saveQRbutton.setVisibility(View.INVISIBLE);
    }
    private void showScreen2() {
        qrCode.setVisibility(View.VISIBLE);
        ssid_tv.setVisibility(View.INVISIBLE);
        ssid_et.setVisibility(View.INVISIBLE);
        pw_tv.setVisibility(View.INVISIBLE);
        pw_et.setVisibility(View.INVISIBLE);
        sec_tv.setVisibility(View.INVISIBLE);
        sec_s.setVisibility(View.INVISIBLE);
        hid_tv.setVisibility(View.INVISIBLE);
        hid_cb.setVisibility(View.INVISIBLE);
        fn_tv.setVisibility(View.VISIBLE);
        fn_et.setVisibility(View.VISIBLE);
        createQRbutton.setVisibility(View.INVISIBLE);
        saveQRbutton.setVisibility(View.VISIBLE);
    }
}