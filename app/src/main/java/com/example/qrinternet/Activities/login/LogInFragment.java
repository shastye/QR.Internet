package com.example.qrinternet.Activities.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.qrinternet.Activities.dialogs.StringDialogFragment;
import com.example.qrinternet.Activities.utility.Tags;
import com.example.qrinternet.R;
import com.example.qrinternet.databinding.FragmentLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LogInFragment extends Fragment {
    private FragmentLoginBinding binding;
    LogInViewModel logInViewModel;

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
                LogInViewModel.setUsername(un_et.getText().toString());
                LogInViewModel.setPassword(pw_et.getText().toString());

                Tags.AUTH.signInWithEmailAndPassword(LogInViewModel.getUsername(), LogInViewModel.getPassword())
                        .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Tags.USER = Tags.AUTH.getCurrentUser();

                                    DialogFragment df = new StringDialogFragment("Logged in successfully.");
                                    df.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Log in successful Message");

                                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_login_to_navigation_create);
                                } else {
                                    DialogFragment df = new StringDialogFragment("Log in attempt failed.\n\nCheck the email and password provided\nand try again.");
                                    df.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Account Invalid Message");
                                }
                            }
                        });
            }
        });

        fp_cet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_login_to_navigation_resetPassword);
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

    @Override
    public void onStart() {
        super.onStart();

        BottomNavigationView navBar = Objects.requireNonNull(getActivity()).findViewById(R.id.nav_view);
        navBar.setVisibility(View.INVISIBLE);

        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).hide();

        FirebaseUser currentUser = Tags.AUTH.getCurrentUser();
        if (currentUser != null){
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_login_to_navigation_create);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
