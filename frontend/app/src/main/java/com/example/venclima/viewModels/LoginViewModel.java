package com.example.venclima.viewModels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.venclima.BR;
import com.example.venclima.models.User;
import com.example.venclima.network.RetrofitInstance;
import com.example.venclima.network.repositories.AuthRepository;

public class LoginViewModel extends BaseObservable {

    private User user;

    public LoginViewModel() {
        this.user = new User("", "");
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

    public void onLoginButtonClicked() {
        if(!isValid())
            return;
        AuthRepository.login(this.user);
    }

    public boolean isValid(){
        return !this.user.getEmail().isEmpty() && !this.user.getPassword().isEmpty();
    }

}
