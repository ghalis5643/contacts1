package edu.andrews.cptr252.lisabeth.contacts;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import edu.andrews.cptr252.lisabeth.contacts.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View Fragment1View = inflater.inflate(R.layout.fragment_first, container, false);
        EditText editName = Fragment1View.findViewById(R.id.editUserName);
        EditText editPassword = Fragment1View.findViewById(R.id.editPassword);
        Button btnConfrim = Fragment1View.findViewById(R.id.btnLogin);
        Button btnNewUser = Fragment1View.findViewById(R.id.btnNewUser);
        EditText editConfirmPassword = Fragment1View.findViewById(R.id.editConfrimPass);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editConfirmPassword.setVisibility(View.INVISIBLE);

        btnConfrim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Retrieve entered username and password
                    String username = editName.getText().toString();
                    String password = editPassword.getText().toString();
                    editor.apply();

                    // Implement authentication logic here
                    if (username.equals("Admin") && password.equals("123")) {
                        // Successful login
                        Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
                    } else {
                        // Failed login
                        Toast.makeText(getActivity(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
        });

        btnNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editConfirmPassword.setVisibility(View.VISIBLE);
                editor.apply();
            }
        });


        return Fragment1View;

    }

}