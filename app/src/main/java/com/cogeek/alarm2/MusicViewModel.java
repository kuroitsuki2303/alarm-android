package com.cogeek.alarm2;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.File;

public class MusicViewModel extends AndroidViewModel {
    private MutableLiveData<File> mSelectedFile = new MutableLiveData<>();
    public MusicViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<File> getSelectedFile() {
        return mSelectedFile;
    }

    public void setSelectedFile(File selectedFile) {
        this.mSelectedFile.setValue(selectedFile);
    }
}
