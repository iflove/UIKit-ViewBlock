package android.uikit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

interface ActivityLifeCycle {

    void onCreate(@Nullable Bundle savedInstanceState);

    void onCreateView();

    void onNewIntent(Intent intent);

    void onSaveInstanceState(Bundle outState);

    void onRestoreInstanceState(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onRestart();

    void onDestroy();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onBackPressed();

}
