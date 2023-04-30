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

import com.example.qrinternet.Activities.dialogs.AccountAlreadyExistsDialogFragment;
import com.example.qrinternet.Activities.dialogs.AccountCreatedDialogFragment;
import com.example.qrinternet.Activities.dialogs.AccountInvalidDialogFragment;
import com.example.qrinternet.Activities.utility.Tags;
import com.example.qrinternet.Activities.utility.User;
import com.example.qrinternet.R;
import com.example.qrinternet.databinding.FragmentSignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        li_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = un_et.getText().toString();
                password = pw_et.getText().toString();

                Tags.AUTH.createUserWithEmailAndPassword(username, password)
                        .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User user = new User(username, password);
                                    db.collection("users").document(username)
                                            .set(user.getHashMap())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    DialogFragment savedImage = new AccountCreatedDialogFragment();
                                                    savedImage.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Account Created Message");

                                                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_signup_to_navigation_login);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // TODO: CREATE CORRECT DIALOG
                                                    //      new database entry not made

                                                    DialogFragment savedImage = new AccountInvalidDialogFragment();
                                                    savedImage.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Account Exists Message");
                                                }
                                            });
                                } else {
                                    // TODO: CREATE CORRECT DIALOG
                                    //      new auth not made

                                    DialogFragment savedImage = new AccountAlreadyExistsDialogFragment();
                                    savedImage.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "Account Exists Message");
                                }
                            }
                        });
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

    @Override
    public void onStart() {
        super.onStart();

        BottomNavigationView navBar = Objects.requireNonNull(getActivity()).findViewById(R.id.nav_view);
        navBar.setVisibility(View.INVISIBLE);

        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).hide();
    }
}
