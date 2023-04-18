package com.example.qrinternet.Activities.create;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.qrinternet.Activities.utility.ErrorCodeDialogFragment;
import com.example.qrinternet.Activities.utility.GetQRCodeFromAPI;
import com.example.qrinternet.R;
import com.example.qrinternet.databinding.FragmentCreateQrCodeBinding;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class CreateQRCodeFragment extends Fragment {

    private FragmentCreateQrCodeBinding binding;

    GetQRCodeFromAPI getQRcode;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CreateAndSaveViewModel createAndSaveViewModel =
                new ViewModelProvider(this).get(CreateAndSaveViewModel.class);

        binding = FragmentCreateQrCodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        createAndSaveViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        // ADDITIONS ADDED BETWEEN COMMENTS

        SwitchMaterial ulw_sw = (SwitchMaterial) root.findViewById(R.id.useLocalWifi_switch);
        EditText ssid_et = (EditText) root.findViewById(R.id.ssid_editText);
        ssid_et.setText("");
        EditText pw_et = (EditText) root.findViewById(R.id.password_editText);
        pw_et.setText("");
        Spinner sec_s = (Spinner) root.findViewById(R.id.security_spinner);
        CheckBox hid_cb = (CheckBox) root.findViewById(R.id.hidden_checkBox);
        Button createQRbutton = (Button) root.findViewById(R.id.CreateQRCode_button);

        //////////////////////////
        //  Generating QR Code  //
        //////////////////////////
        sec_s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sec_s.getSelectedItem().toString().equals("None")) {
                    pw_et.setEnabled(false);
                    pw_et.setText("");
                }
                else {
                    pw_et.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ulw_sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ulw_sw.isChecked()) {
                    WifiManager wifiManager = (WifiManager) root.getContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();

                    ssid_et.setText(wifiInfo.getSSID());

                    int sec = wifiInfo.getCurrentSecurityType();
                    switch (sec) {
                        case WifiInfo.SECURITY_TYPE_OPEN:
                            sec_s.setSelection(2);
                            break;
                        case WifiInfo.SECURITY_TYPE_WEP:
                            sec_s.setSelection(1);
                            break;
                        case WifiInfo.SECURITY_TYPE_EAP:
                        case WifiInfo.SECURITY_TYPE_EAP_WPA3_ENTERPRISE:
                        case WifiInfo.SECURITY_TYPE_EAP_WPA3_ENTERPRISE_192_BIT:
                        case WifiInfo.SECURITY_TYPE_OWE:
                        case WifiInfo.SECURITY_TYPE_PASSPOINT_R1_R2:
                        case WifiInfo.SECURITY_TYPE_PASSPOINT_R3:
                        case WifiInfo.SECURITY_TYPE_PSK:
                        case WifiInfo.SECURITY_TYPE_SAE:
                        case WifiInfo.SECURITY_TYPE_UNKNOWN:
                        case WifiInfo.SECURITY_TYPE_WAPI_CERT:
                        case WifiInfo.SECURITY_TYPE_WAPI_PSK:
                        default:
                            sec_s.setSelection(0);
                            break;
                    }

                    hid_cb.setChecked(false);
                    if (wifiInfo.getHiddenSSID()) {
                        hid_cb.setChecked(true);
                    }

                    ssid_et.setEnabled(false);
                    sec_s.setEnabled(false);
                    hid_cb.setEnabled(false);
                }
                else {
                    ssid_et.setEnabled(true);
                    sec_s.setEnabled(true);
                    hid_cb.setEnabled(true);
                }

            }
        });

        createQRbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String ssid;
                String password;
                String security;
                String hidden;
                String finalHidden;


                ssid = ssid_et.getText().toString();
                password = pw_et.getText().toString();
                security = sec_s.getSelectedItem().toString();
                if (security.equals("None")) {
                    security = "nopass";
                    password = "";
                }
                hidden = "false";
                if (hid_cb.isChecked()) {
                    hidden = "true";
                }
                finalHidden = hidden;


                if ((!ssid.equals("") && !password.equals("")) || (!ssid.equals("") && security.equals("nopass"))) {
                    getQRcode = new GetQRCodeFromAPI(ssid, password, security, finalHidden);
                    getQRcode.execute();
                    try {
                        getQRcode.get();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (getQRcode.getResponseCode() == 200) {
                        createAndSaveViewModel.setBitmap(getQRcode.getBitmap());
                        createAndSaveViewModel.setBinaryData(getQRcode.getBinaryData());

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