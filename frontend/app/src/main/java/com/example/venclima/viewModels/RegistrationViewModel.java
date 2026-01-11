package com.example.venclima.viewModels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.venclima.BR;
import com.example.venclima.models.RegisterUser;
import com.example.venclima.network.Callbacks.AuthCallback;
import com.example.venclima.network.repositories.AuthRepository;

public class RegistrationViewModel extends BaseObservable {

    private RegisterUser user;
    private String confirmPassword;
    private AuthCallback authCallback;
    private String errorMsg;


    public RegistrationViewModel() {
        this.user = new RegisterUser("", "", "", "");
    }

    @Bindable
    public String getEmail() {
        return this.user.getEmail();
    }

    public void setEmail(String email) {
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
            authCallback.onError(this.errorMsg);
        else
            AuthRepository.registerUser(this.user, authCallback);
    }

    public void setAuthCallback(AuthCallback callback) {
        this.authCallback = callback;
    }

    public boolean isValid() {
        boolean res = !this.user.getEmail().isEmpty() && !this.user.getPassword().isEmpty() && !this.user.getName().isEmpty() && !this.user.getSurname().isEmpty();
        if(!res)
            this.errorMsg = "Tutti i campi devono essere compilati";
        else {
            res = this.confirmPassword.equals(this.user.getPassword());
            if(!res)
                this.errorMsg = "Le password non corrispondono";
        }
        return res;
    }
}
