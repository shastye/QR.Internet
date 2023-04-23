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

import com.example.qrinternet.Activities.dialogs.AccountInvalidDialogFragment;
import com.example.qrinternet.Activities.dialogs.ImageDeletedDialogFragment;
import com.example.qrinternet.Activities.dialogs.IncorrectPasswordDialogFragment;
import com.example.qrinternet.Activities.dialogs.LogInSuccessfulDialogFragment;
import com.example.qrinternet.Activities.roomsupplies.AppDatabase;
import com.example.qrinternet.Activities.roomsupplies.User;
import com.example.qrinternet.R;
import com.example.qrinternet.databinding.FragmentLoginBinding;

import java.util.Objects;

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

                final User[] temp = new User[1];
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        temp[0] = AppDatabase.getInstance(root.getContext()).userDao().getUser(username);
                    }
                });
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (temp[0] != null) {
                    if (password.equals(temp[0].password)) {
                        t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                LogInViewModel.setImages(AppDatabase.getInstance(root.getContext()).imageDao().findImagesFromUser(username));
                                LogInViewModel.setCurrentUser(temp[0]);
                            }
                        });
                        t.start();
                        try {
                            t.join();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        DialogFragment savedImage = new LogInSuccessfulDialogFragment();
                        savedImage.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Log in successful Message");
                    }
                    else {
                        DialogFragment savedImage = new IncorrectPasswordDialogFragment();
                        savedImage.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Incorrect Password Message");
                    }
                }
                else {
                    DialogFragment savedImage = new AccountInvalidDialogFragment();
                    savedImage.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Account Invalid Message");
                }
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
