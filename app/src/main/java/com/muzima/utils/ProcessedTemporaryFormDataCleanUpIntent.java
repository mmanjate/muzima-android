/*
 * Copyright (c) The Trustees of Indiana University, Moi University
 * and Vanderbilt University Medical Center. All Rights Reserved.
 *
 * This version of the code is licensed under the MPL 2.0 Open Source license
 * with additional health care disclaimer.
 * If the user is an entity intending to commercialize any application that uses
 * this code in a for-profit venture, please contact the copyright holder.
 */

package com.muzima.utils;

import android.content.Context;
import com.muzima.view.SyncIntent;

import static com.muzima.utils.Constants.DataSyncServiceConstants.CLEAN_UP_PROCESSED_TEMPORARY_FORM_DATA;
import static com.muzima.utils.Constants.DataSyncServiceConstants.SYNC_TYPE;

public class ProcessedTemporaryFormDataCleanUpIntent extends SyncIntent {
    public ProcessedTemporaryFormDataCleanUpIntent(Context context){
        super(context);
        putExtra(SYNC_TYPE, CLEAN_UP_PROCESSED_TEMPORARY_FORM_DATA);
    }
}
