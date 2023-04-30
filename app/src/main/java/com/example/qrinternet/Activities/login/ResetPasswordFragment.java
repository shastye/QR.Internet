package com.example.qrinternet.Activities.login;

import android.os.Bundle;
import android.util.Log;
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
import androidx.navigation.Navigation;

import com.example.qrinternet.Activities.dialogs.ErrorCodeDialogFragment;
import com.example.qrinternet.Activities.dialogs.StringDialogFragment;
import com.example.qrinternet.Activities.utility.Tags;
import com.example.qrinternet.Activities.utility.User;
import com.example.qrinternet.R;
import com.example.qrinternet.databinding.FragmentResetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.Objects;

public class ResetPasswordFragment extends Fragment {
    private FragmentResetPasswordBinding binding;
    LogInViewModel logInViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = binding.getRoot();

        // ADDITIONS ADDED BETWEEN COMMENTS


        EditText un_et = (EditText) root.findViewById(R.id.resetPasswordUsername_editText);
        Button rp_b = (Button) root.findViewById(R.id.resetPassword_button);
        TextView li_cet = (TextView) root.findViewById(R.id.resetPassword_changeToLogIn_clickableTextView);

        rp_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogInViewModel.username = un_et.getText().toString();

                Tags.AUTH.sendPasswordResetEmail(LogInViewModel.username)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    DialogFragment df = new StringDialogFragment("An email has been sent for you to reset your password.");
                                    df.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Reset Password Message");

                                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_resetPassword_to_navigation_login);
                                }
                                else {
                                    DialogFragment df = new StringDialogFragment("An error occurred and email was not sent.");
                                    df.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Reset Password Message");
                                }
                            }
                        });
            }
        });

        li_cet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_resetPassword_to_navigation_login);
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}