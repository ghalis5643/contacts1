package edu.andrews.cptr252.lisabeth.contacts.login;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.andrews.cptr252.lisabeth.contacts.data.LoginRepository;
import edu.andrews.cptr252.lisabeth.contacts.data.Result;
import edu.andrews.cptr252.lisabeth.contacts.data.model.LoggedInUser;
import edu.andrews.cptr252.lisabeth.contacts.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
            Log.i("Success login", loginResult.toString());
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
            Log.i("Failed login", loginResult.toString());
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            Log.e("Invalid username", username);
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null, null));
        } else if (!isPasswordValid(password)) {
            Log.e("Invalid password", password);
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password, null));
        } else {
            Log.e("LoginForm", password);
            loginFormState.setValue(new LoginFormState(true));
        }


    }

    public void registerDataChanged(String username, String password, String confirmPassword) {
        Log.e("Password_Comparison",  password+" "+confirmPassword);

        if (!isUserNameValid(username)) {
            Log.e("Register_Invalid username", username);
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null, null));
        } else if (!isPasswordValid(password)) {
            Log.e("Register_Invalid password", password);
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password, null));
        } else if(!isConfirmPasswordValid(password, confirmPassword)){
            Log.e("Register_LoginForm",  password+" "+confirmPassword);
            loginFormState.setValue(new LoginFormState(null, null, R.string.password_miss_match));
        }else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {

        Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
        if (username == null) {
            return false;
        }

        Matcher matcher = EMAIL_PATTERN.matcher(username);

        if (matcher.matches()) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            Log.e("UsernameNOMatch", username);
            return false;
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isConfirmPasswordValid(String password, String confirmPassword) {

        if(password.equals(confirmPassword)){
            return password != null && password.trim().length() > 5;
        }else{
            return false;
        }

    }

    public void register(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.register(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
            Log.i("Success login", loginResult.toString());
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
            Log.i("Failed login", loginResult.toString());
        }
    }


}