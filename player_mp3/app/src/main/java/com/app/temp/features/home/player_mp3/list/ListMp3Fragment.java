package com.app.temp.features.home.player_mp3.list;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.temp.R;
import com.app.temp.base.fragment.BaseFragment;
import com.app.temp.features.home.HomeActivity;
import com.app.temp.features.home.player_mp3.detail.DetailMp3Fragment;
import com.app.temp.pojo.Mp3File;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class ListMp3Fragment extends BaseFragment {

    @BindView(R.id.rc_list)
    RecyclerView rcList;

    public static ListMp3Fragment newInstance() {
        return new ListMp3Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_mp3, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isExternalStorageReadable()) {
            List<Mp3File> mApkFiles = mp3Reader(Environment.getExternalStorageDirectory());
            setUpAdapter(mApkFiles);
        }
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    List<Mp3File> mp3Reader(File root) {
        List<Mp3File> a = new ArrayList<>();

        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                a.addAll(mp3Reader(file));
            } else {
                if (file.getName().toLowerCase().endsWith(".mp3")) {
                    a.add(new Mp3File(file.getPath()));
                }
            }
        }
        return a;
    }

    private void setUpAdapter(List<Mp3File> mp3Files) {
        if (rcList != null) {
            rcList.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            rcList.setLayoutManager(mLayoutManager);

            Mp3Adapter mAdapter = new Mp3Adapter(mp3Files, file -> ((HomeActivity) Objects.requireNonNull(getActivity())).transactionFragment(DetailMp3Fragment.newInstance().setMp3File(file)));
            rcList.setAdapter(mAdapter);
        }
    }
}
