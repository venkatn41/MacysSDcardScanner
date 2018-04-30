package com.venkat.macyssdcardscanner.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.venkat.macyssdcardscanner.R;

import java.text.DecimalFormat;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by venkat on 2/3/18.
 */

public class FilesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.file_name)
    TextView fileNameTextView;
    @BindView(R.id.file_size)
    TextView fileSizeTextView;
    DecimalFormat df = new DecimalFormat("###.##");

    public FilesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    //Bind the Extension list values
    void bindExt(String name, Integer count) {
        fileNameTextView.setText(name);
        fileSizeTextView.setText(count.toString() + " count");
    }

    //Bind the size list vales
    void bindSize(String name, Float size) {
        fileNameTextView.setText(name);
        fileSizeTextView.setText(df.format(size).toString() + " MB");
    }
}
