package com.example.qrinternet.Activities.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.qrinternet.R;
import com.example.qrinternet.databinding.FragmentLoginBinding;

public class LogInFragment extends Fragment {
    private FragmentLoginBinding binding;
    LogInViewModel logInViewModel;

    String username;
    String password;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        logInViewModel = new ViewModelProvider(this).get(LogInViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textLogin;
        logInViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // ADDITIONS ADDED BETWEEN COMMENTS

        EditText un_et = (EditText) root.findViewById(R.id.loginUsername_editText);
        EditText pw_et = (EditText) root.findViewById(R.id.loginPassword_editText);
        Button li_b = (Button) root.findViewById(R.id.login_button);
        TextView fp_cet = (TextView) root.findViewById(R.id.loginForgotPassword_textView);
        TextView su_cet = (TextView) root.findViewById(R.id.changeToSignUp_clickableTextView);

        li_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = un_et.getText().toString();
                password = pw_et.getText().toString();

                // TODO: Query database
                //      Search first for email to see if account exists (new dialog)
                //      Then make sure combination matches (second dialog)
            }
        });

        fp_cet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Open dialog or new fragment to change password
                //      then update database
            }
        });

        su_cet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_login_to_navigation_signup);
            }
        });

        //

        // END ADDITIONS

        return root;
    }
}
