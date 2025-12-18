package com.example.venclima.viewModels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.venclima.BR;
import com.example.venclima.models.RegisterUser;
import com.example.venclima.network.RetrofitInstance;
import com.example.venclima.network.repositories.AuthCallback;
import com.example.venclima.network.repositories.AuthRepository;
import com.example.venclima.network.services.AuthService;

import org.maplibre.android.log.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationViewModel extends BaseObservable {

    private RegisterUser user;
    private String confirmPassword;
    private AuthCallback authCallback;


    public RegistrationViewModel() {
        this.user = new RegisterUser("", "", "", "");
    }

    @Bindable
    public String getEmail() {
        return this.user.getEmail();
    }

    public void setEmail(String email) {
        this.user.setEmail(email);
        this.user.setEmail(email);
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return this.user.getPassword();
    }

    public void setPassword(String password) {
        this.user.setPassword(password);
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getName() {
        return this.user.getName();
    }

    public void setName(String name) {
        this.user.setName(name);
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getSurname() {
        return this.user.getSurname();
    }

    public void setSurname(String surname) {
        this.user.setSurname(surname);
        notifyPropertyChanged(BR.surname);
    }

    @Bindable
    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        notifyPropertyChanged(BR.confirmPassword);
    }

    public void onRegistrationButtonClicked() {
        if(!isValid())
            return;
        AuthRepository.registerUser(this.user, authCallback);
    }

    public void setAuthCallback(AuthCallback callback) {
        this.authCallback = callback;
    }

    public boolean isValid() {
        return !this.user.getEmail().isEmpty() && !this.user.getPassword().isEmpty() && !this.user.getName().isEmpty() && !this.user.getSurname().isEmpty() && this.confirmPassword.equals(this.user.getPassword());
    }
}
