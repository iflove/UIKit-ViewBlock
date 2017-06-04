package android.uikit.demo.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.uikit.demo.R;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by lazy on 2017/4/24.
 */

public class MainFragment extends ListFragment {
    private static final String TAG = "MainFragment";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListAdapter(new MyListAdapter());
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.d(TAG, "onBackStackChanged: ");
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.push_up_in, R.anim.push_up_out, R.anim.push_up_in, R.anim.push_up_out);
        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(new Bundle());
        fragmentTransaction.add(R.id.activity_main, fragment, "tag");
        fragmentTransaction.add(R.id.main, new ItemFragment(), "tag");
        fragmentTransaction.addToBackStack(null);
//		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        fragmentTransaction.commit();

        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                Fragment tag = getFragmentManager().findFragmentByTag("tag");
                Log.d(TAG, "onListItemClick: tag" + tag);
            }
        }, 3000);

    }


    class MyListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return ('Z' - 'A') + 1;
        }

        @Override
        public String getItem(int position) {
            return String.valueOf((char) (position + 'A'));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new TextView(parent.getContext());
            }
            TextView textView = (TextView) convertView;
            textView.setGravity(Gravity.RIGHT);
            textView.setText(getItem(position));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                textView.setPaddingRelative(0, 20, 20, 15);
            }
            return convertView;
        }
    }


}
