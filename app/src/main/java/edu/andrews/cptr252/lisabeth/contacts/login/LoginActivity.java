package edu.andrews.cptr252.lisabeth.contacts.login;


import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.andrews.cptr252.lisabeth.contacts.DAOLogin;
import edu.andrews.cptr252.lisabeth.contacts.MainActivity;
import edu.andrews.cptr252.lisabeth.contacts.R;
import edu.andrews.cptr252.lisabeth.contacts.data.LoginDataSource;
import edu.andrews.cptr252.lisabeth.contacts.data.LoginRepository;

import edu.andrews.cptr252.lisabeth.contacts.databinding.ActivityLoginBinding;
public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    private DAOLogin helper;
    private Group loginForm;
    private Group registrationForm;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(LoginRepository.getInstance(new LoginDataSource(this))))
                .get(LoginViewModel.class);


        loginForm = binding.loginForm;
        registrationForm = binding.registrationForm;


        final EditText usernameEditText = binding.loginEmail;
        final EditText passwordEditText = binding.loginPassword;
        final Button loginButton        = binding.loginButton;

        final EditText register_email            = binding.registerEmail;
        final EditText register_password         = binding.registerPassword;
        final EditText register_confirm_password = binding.registerConfirmPassword;
        final Button register_button              = binding.registerButton;

        final Button showRegisterForm   = binding.showRegisterForm;
        final Button showLoginForm      = binding.showLoginForm;

        showRegisterForm.setOnClickListener(v -> toggleForms(false));
        showLoginForm.setOnClickListener(v -> toggleForms(true));


        final ProgressBar loadingProgressBar = binding.loading;


        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            Log.e("loginFormState", loginFormState.toString());




            if(isLoginFormVisible()) {

                if (loginFormState == null) return;

                binding.loginButton.setEnabled(loginFormState.isDataValid());

                if (loginFormState.getUsernameError() != null) {
                    binding.loginEmail.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    binding.loginPassword.setError(getString(loginFormState.getPasswordError()));
                }

            }else if(isRegistrationFormVisible()){

                if (loginFormState == null) return;

                binding.registerButton.setEnabled(loginFormState.isDataValid());

                if (loginFormState.getUsernameError() != null) {
                    binding.registerEmail.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    binding.registerPassword.setError(getString(loginFormState.getPasswordError()));
                }
                if (loginFormState.getConfirmPasswordError() != null) {
                    binding.registerConfirmPassword.setError(getString(loginFormState.getConfirmPasswordError()));
                }

            }else{
                Log.i("NoForm", "true");
            }
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) return;
            binding.loading.setVisibility(View.GONE);
            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {
                updateUiWithUser(loginResult.getSuccess());
                setResult(RESULT_OK);
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                if(isLoginFormVisible()) {
                    Log.i("isLoginFormVisible", "true");
                    loginViewModel.loginDataChanged(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                }else if(isRegistrationFormVisible()){
                    Log.i("isRegistrationFormVisible", "true");

                    loginViewModel.registerDataChanged(register_email.getText().toString(), register_password.getText().toString(), register_confirm_password.getText().toString());
                }else{
                    Log.i("NoForm", "true");

                }
            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        register_email.addTextChangedListener(afterTextChangedListener);
        register_password.addTextChangedListener(afterTextChangedListener);
        register_confirm_password.addTextChangedListener(afterTextChangedListener);


        if (loginButton != null) {
            loginButton.setOnClickListener(v -> {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            });
        } else {
            Log.e("LoginActivity", "loginButton is null");
        }

        if (register_button != null) {
            register_button.setOnClickListener(v -> {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.register(register_email.getText().toString(), passwordEditText.getText().toString());
            });
        } else {
            Log.e("LoginActivity", "loginButton is null");
        }

    }

    private void updateUiWithUser(LoggedInUserView model) {
        navigateToMainActivity();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        //Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
        TextView textView = findViewById(R.id.textErrorMessage);
        textView.setText(errorString.toString());
        Log.i("showLoginFailed", errorString.toString());
    }

    private void toggleForms(boolean showLoginForm) {
        if (showLoginForm) {
            binding.loginForm.setVisibility(View.VISIBLE);
            binding.registrationForm.setVisibility(View.GONE);
        } else {
            binding.loginForm.setVisibility(View.GONE);
            binding.registrationForm.setVisibility(View.VISIBLE);
        }
    }


    private boolean isLoginFormVisible() {

        if(loginForm != null){
            return loginForm.getVisibility() == View.VISIBLE;
        }else{
            return false;
        }

    }
    private boolean isRegistrationFormVisible() {

        if(registrationForm != null){
            return registrationForm.getVisibility() == View.VISIBLE;
        }else{
            return false;
        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish LoginActivity so the user can't go back to it with the back button
    }

}
