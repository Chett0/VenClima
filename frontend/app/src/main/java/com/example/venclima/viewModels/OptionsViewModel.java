package com.example.venclima.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class OptionsViewModel extends ViewModel  {

    private MutableLiveData<Boolean> isNotificationEnabled = new MutableLiveData<>();

    public LiveData<Boolean> getIsNotificationEnabled() {
        return isNotificationEnabled;
    }

    public void disableNotification() {
        isNotificationEnabled.setValue(false);
    }

    public void enableNotification() {
        isNotificationEnabled.setValue(true);
    }

}
