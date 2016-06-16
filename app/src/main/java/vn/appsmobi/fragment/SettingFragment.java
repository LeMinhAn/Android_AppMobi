package vn.appsmobi.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import vn.appsmobi.R;

public class SettingFragment extends Fragment {

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        initView();

        return view;
    }


    public void initView() {
        getActivity().findViewById(R.id.myToolbar).animate()
                .translationY(0)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(100)
                .start();
    }
}

