package com.venkat.macyssdcardscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.venkat.macyssdcardscanner.model.FileExt;
import com.venkat.macyssdcardscanner.list.FileExtListAdapter;
import com.venkat.macyssdcardscanner.list.FileSizeListAdapter;
import com.venkat.macyssdcardscanner.model.FileSize;
import com.venkat.macyssdcardscanner.utils.Constatnts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by venkat on 4/28/18.
 * <p>
 * Results are displayed in toe lists in the same screen.
 */

public class FileListFragment extends Fragment {

    @BindView(R.id.biggest_files_list)
    RecyclerView biggestFileList;

    @BindView(R.id.freq_extensions_list)
    RecyclerView extensionFileList;

    @BindView(R.id.avg_size)
    TextView avgSizeTextView;

    @BindView(R.id.share_button)
    Button shareButton;

    FileSizeListAdapter fileSizeListAdapter;
    FileExtListAdapter fileExtListAdapter;

    //Map to store the format type counts
    HashMap<String, Integer> extMap = new HashMap<>();
    //Map to store the file name and size
    HashMap<String, Float> sizeMap = new HashMap<>();

    //List's to hold the extensions and size counts.
    ArrayList<FileExt> fileExtList = new ArrayList<>();
    ArrayList<FileSize> filesSizeList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.file_list_fragment, container, false);

        ButterKnife.bind(this, view);

        shareButton.setOnClickListener(new clickListener());

        fileSizeListAdapter = new FileSizeListAdapter();
        fileExtListAdapter = new FileExtListAdapter();

        Bundle bundle = getArguments();

        extMap = (HashMap<String, Integer>) bundle.getSerializable(Constatnts.FRAGMENT.EXTENSION_MAP);
        sizeMap = (HashMap<String, Float>) bundle.getSerializable(Constatnts.FRAGMENT.SIZE_MAP);

        float avgSize = bundle.getFloat(Constatnts.FRAGMENT.AVG_SIZE);
        avgSizeTextView.setText("Average File Size :" + Float.toString(avgSize) + " MB");

        //Adding data to adapter
        swapAdapterForExtFilesList(extMap);
        swapAdapterForSizeFilesList(sizeMap);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        biggestFileList.setLayoutManager(mLayoutManager);

        RecyclerView.LayoutManager mExtLayoutManager = new LinearLayoutManager(getContext());
        extensionFileList.setLayoutManager(mExtLayoutManager);

        //TODO : Getting resource not found. Should debug further.
        //biggestFileList.addItemDecoration(new SimpleDividerItemDecoration(getContext()));

        extensionFileList.setAdapter(fileExtListAdapter);
        biggestFileList.setAdapter(fileSizeListAdapter);

        return view;
    }

    //Adding the data to the adapter for Size list
    public void swapAdapterForSizeFilesList(HashMap<String, Float> sizeFilesMap) {
        for (Map.Entry<String, Float> entry : sizeFilesMap.entrySet()) {
            FileSize file = new FileSize();
            file.setFileSize(entry.getKey(), entry.getValue());
            filesSizeList.add(file);
            Collections.sort(filesSizeList);
        }
        fileSizeListAdapter.swapAdapter(filesSizeList);
    }

    //Adding the data to the adapter for Extension list
    public void swapAdapterForExtFilesList(HashMap<String, Integer> extFilesMap) {
        for (Map.Entry<String, Integer> entry : extFilesMap.entrySet()) {
            FileExt file = new FileExt();
            file.setFileSize(entry.getKey(), entry.getValue());
            fileExtList.add(file);
            Collections.sort(fileExtList);
        }
        fileExtListAdapter.swapAdapter(fileExtList);
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = avgSizeTextView.getText().toString();
            String shareSub = "Average Size of sd card contents";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        }
    }
}


