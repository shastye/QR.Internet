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

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.qrinternet.Activities.utility.ErrorCodeDialogFragment;
import com.example.qrinternet.Activities.utility.GetQRCodeFromAPI;
import com.example.qrinternet.R;
import com.example.qrinternet.databinding.FragmentDashboardBinding;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    GetQRCodeFromAPI getQRcode;

    Button createQRbutton;
    TextView ssid_tv;
    EditText ssid_et;
    TextView pw_tv;
    EditText pw_et;
    TextView sec_tv;
    Spinner sec_s;
    TextView hid_tv;
    CheckBox hid_cb;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        // ADDITIONS ADDED BETWEEN COMMENTS

        ssid_tv = (TextView) root.findViewById(R.id.ssid_textView);
        ssid_et = (EditText) root.findViewById(R.id.ssid_editText);
        ssid_et.setText("");
        pw_tv = (TextView) root.findViewById(R.id.password_textView);
        pw_et = (EditText) root.findViewById(R.id.password_editText);
        pw_et.setText("");
        sec_tv = (TextView) root.findViewById(R.id.security_textView);
        sec_s = (Spinner) root.findViewById(R.id.security_spinner);
        hid_tv = (TextView) root.findViewById(R.id.hidden_textView);
        hid_cb = (CheckBox) root.findViewById(R.id.hidden_checkBox);
        createQRbutton = (Button) root.findViewById(R.id.CreateQRCode_button);

        //////////////////////////
        //  Generating QR Code  //
        //////////////////////////

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
                    getQRcode = new GetQRCodeFromAPI(ssid, password, security, finalHidden);
                    getQRcode.execute();
                    try {
                        getQRcode.get();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (getQRcode.getResponseCode() == 200) {
                        dashboardViewModel.setBitmap(getQRcode.getBitmap());
                        dashboardViewModel.setBinaryData(getQRcode.getBinaryData());

                        Navigation.findNavController(root).navigate(R.id.action_navigation_dashboard_to_navigation_save);
                    }
                    else {
                        DialogFragment errorDialog = new ErrorCodeDialogFragment(getQRcode.getResponseCode(), getQRcode.getErrorDetails());
                        errorDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Error Message");
                    }
                }

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