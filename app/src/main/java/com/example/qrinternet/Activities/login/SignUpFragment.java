package com.example.qrinternet.Activities.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.qrinternet.Activities.dialogs.AccountAlreadyExistsDialogFragment;
import com.example.qrinternet.Activities.dialogs.AccountCreatedDialogFragment;
import com.example.qrinternet.Activities.dialogs.AccountInvalidDialogFragment;
import com.example.qrinternet.R;
import com.example.qrinternet.databinding.FragmentSignupBinding;

import java.util.Objects;

public class SignUpFragment extends Fragment {
    private FragmentSignupBinding binding;
    LogInViewModel logInViewModel;

    String username;
    String password;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        logInViewModel = new ViewModelProvider(this).get(LogInViewModel.class);

        binding = FragmentSignupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textLogin;
        logInViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // ADDITIONS ADDED BETWEEN COMMENTS

        EditText un_et = (EditText) root.findViewById(R.id.signupUsername_editText);
        EditText pw_et = (EditText) root.findViewById(R.id.signupPassword_editText);
        Button li_b = (Button) root.findViewById(R.id.signup_button);
        TextView su_cet = (TextView) root.findViewById(R.id.changeToLogIn_clickableTextView);

        li_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = un_et.getText().toString();
                password = pw_et.getText().toString();

                Object temp = new Object();

                if (temp == null) {




                    DialogFragment savedImage = new AccountCreatedDialogFragment();
                    savedImage.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Account Created Message");

                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_signup_to_navigation_login);
                }
                else {
                    DialogFragment savedImage = new AccountAlreadyExistsDialogFragment();
                    savedImage.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Account Exists Message");
                }
            }
        });

        su_cet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_signup_to_navigation_login);
            }
        });

        //

        // END ADDITIONS

        return root;
    }
}
