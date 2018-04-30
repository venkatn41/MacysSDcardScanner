package com.venkat.macyssdcardscanner;

import com.venkat.macyssdcardscanner.model.Details;

/**
 * Created by venkat on 4/28/18.
 */
// Interface to be implemented by client to receive the results after scan is completed.
public interface OnScanCompleteListener {
    void onScanCompleted(Details fileDetails);
}
