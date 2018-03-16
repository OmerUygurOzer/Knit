package com.omerozer.knit;

import android.content.Intent;

/**
 * Created by omerozer on 3/9/18.
 */

public interface NativeViewCallbacks {
    void onViewStart();
    void onViewResume();
    void onViewPause();
    void onViewStop();
    void onViewResult(int requestCode, int resultCode, Intent data);
    void onReturnToView();
}
