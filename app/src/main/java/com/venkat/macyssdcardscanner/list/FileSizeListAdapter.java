package com.venkat.macyssdcardscanner.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.venkat.macyssdcardscanner.model.FileSize;
import com.venkat.macyssdcardscanner.R;

import java.util.ArrayList;

/**
 * Created by venkat on 4/28/18.
 * <p>
 * Adapter for file's size list
 */

public class FileSizeListAdapter extends RecyclerView.Adapter<FilesViewHolder> {

    private static final int MAX_ITEMS_TO_BE_DISPLAYED = 10;
    ArrayList<FileSize> fileSizeList = new ArrayList<>();

    public void swapAdapter(ArrayList<FileSize> files) {
        fileSizeList.clear();
        fileSizeList = files;
        notifyDataSetChanged();
    }

    @Override
    public FilesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_list_row_layout, parent, false);
        return new FilesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilesViewHolder holder, int position) {
        FileSize file = fileSizeList.get(position);
        holder.bindSize(file.getFileName(), file.getFileSize());
    }

    @Override
    public int getItemCount() {
        if (fileSizeList.size() > MAX_ITEMS_TO_BE_DISPLAYED) {
            return MAX_ITEMS_TO_BE_DISPLAYED;
        } else
            return fileSizeList.size();
    }
}
