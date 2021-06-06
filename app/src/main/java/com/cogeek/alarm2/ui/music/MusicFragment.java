package com.cogeek.alarm2.ui.music;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.cogeek.alarm2.MusicViewModel;
import com.cogeek.alarm2.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MusicFragment extends Fragment {

    private MusicAdapter adapter;
    private ArrayList<String> songs;
    private ArrayList<File> musics;
    private ListView listView;
    private MusicViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        listView = view.findViewById(R.id.listView);
        getMusic();
        viewModel = new ViewModelProvider(requireActivity()).get(MusicViewModel.class);
        return view;
    }

    public void getMusic() {
        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                musics = findMusicFiles(Environment.getExternalStorageDirectory());
                songs = new ArrayList<>();
                for (int i = 0; i < musics.size(); i++) {
                    songs.add(musics.get(i).getName());
                }

                adapter = new MusicAdapter(getActivity(),R.layout.item,songs);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        viewModel.setSelectedFile(musics.get(position));
                        NavHostFragment.findNavController(MusicFragment.this).navigate(R.id.action_SecondFragment_to_FirstFragment);
                    }
                });
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    private ArrayList<File> findMusicFiles(File file) {
        ArrayList<File> musicFiles = new ArrayList<>();
        File [] files = file.listFiles();

        for (File current : files) {
            if (current.isDirectory() && !current.isHidden()) {
                musicFiles.addAll(findMusicFiles(current));
            } else {
                if (current.getName().endsWith(".mp3") || current.getName().endsWith(".mp4a") || current.getName().endsWith(".wav")) {
                    musicFiles.add(current);
                }
            }
        }
        return  musicFiles;
    }

}