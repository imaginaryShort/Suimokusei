package com.kiterette.mochi;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends Activity implements MainFragment.OnFragmentInteractionListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new MainFragment();
        fragmentTransaction.replace(R.id.mainFragment, fragment);
        fragmentTransaction.addToBackStack(null); // 戻るボタンでreplace前に戻る
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
