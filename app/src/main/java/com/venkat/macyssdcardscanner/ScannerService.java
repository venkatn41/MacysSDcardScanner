package com.venkat.macyssdcardscanner;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.venkat.macyssdcardscanner.model.Details;
import com.venkat.macyssdcardscanner.utils.Constatnts;

import java.io.File;
import java.util.HashMap;

/**
 * Created by venkat on 4/27/18.
 */

public class ScannerService extends Service implements Runnable {

    public static final File EXTRENAL = Environment.getExternalStorageDirectory();
    private final IBinder mBinder = new LocalBinder();

    //HashMap to hold the count of extensions and file sizes.
    HashMap<String, Integer> extensionMap = new HashMap<>();
    HashMap<String, Float> topFilesmap = new HashMap<>();

    //to keep a count of number of files
    int numberOfFiles = 0;

    //boolean to check if the scan is stopped by user
    boolean isStopped = false;

    private Thread scanThread;

    //Listener for informing the client about scan finished.
    private OnScanCompleteListener listener;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MACYS", "onBind of Service");
        return mBinder;
    }

    public void registerListener(OnScanCompleteListener listener) {
        this.listener = listener;
    }

    public void unregisterListener() {
        this.listener = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();

        if (action.equals(Constatnts.ACTION.STARTFOREGROUND_ACTION)) {
            Log.d("MACYS", "Setting as foreground while scanning");
            isStopped = false;

            //Building the notification to be displayed in status bar.
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Macy's SD card scanner")
                    .setOngoing(true)
                    .setContentText("Macy's SD card scan in progress")
                    .setSmallIcon(R.drawable.ic_launcher_background).build();
            startForeground(Constatnts.NOTIFICATION.SERVICE_ID, notification);

            scanThread = new Thread(this);
            scanThread.start();

        } else if (action.equals(Constatnts.ACTION.STOPFOREGROUND_ACTION)) {
            Log.d("MACYS", "Removing as foreground after scanning is completed");
            stopForeground(true);
            isStopped = true;
        }
        return START_STICKY;
    }

    @Override
    public void run() {
        scan(EXTRENAL);
    }

    public class LocalBinder extends Binder {
        ScannerService getService() {
            return ScannerService.this;
        }
    }

    /**
     * Scans for all the files in the given directory.
     * All the bytes are converted to MB
     *
     * @param dir
     */
    public void scan(File dir) {
        File[] listFile = dir.listFiles();
        if (listFile != null) {

            for (int i = 0; i < listFile.length; i++) {
                if (!isStopped) {
                    if (listFile[i].isDirectory()) {
                        scan(listFile[i]);
                    } else {
                        String fileName = listFile[i].getName();
                        topFilesmap.put(fileName, listFile[i].length() / (1024 * 1024f));
                        addFreqExtension(fileName);
                        numberOfFiles++;
                    }
                }
            }
            Details fileDetails = new Details();
            float avgSize = ((EXTRENAL.getTotalSpace() - EXTRENAL.getFreeSpace()) / (1024 * 1024f)) / numberOfFiles;
            fileDetails.setAvgSize(avgSize);

            fileDetails.setExtMap(extensionMap);
            fileDetails.setSizeMap(topFilesmap);

            listener.onScanCompleted(fileDetails);

            //After the scan is completed the service is stopped from running in foreground.
            //This will remove the notification from the status bar as well.
            stopForeground(true);
        }
    }

    //Keeps count of extensions by adding to Hashmap
    private void addFreqExtension(String fileName) {
        Log.d("MACYS", "FileName :" + fileName);
        String extension = getFileExtension(fileName);
        if (!extension.isEmpty()) {
            extensionMap.put(extension, extensionMap.get(extension) != null ? extensionMap.get(extension) + 1 : 1);
        }

    }

    //finds the extension of the file.
    String getFileExtension(String fileName) {
        if (fileName.isEmpty()) {
            return "";
        }
        int i = fileName.lastIndexOf('.');
        String ext = i > 0 ? fileName.substring(i + 1) : "";
        return ext;
    }
}