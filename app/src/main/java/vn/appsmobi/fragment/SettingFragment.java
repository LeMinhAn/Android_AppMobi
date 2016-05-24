package vn.appsmobi.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import vn.appsmobi.R;

/**
 * Created by tobrother on 04/04/2016.
 */
public class SettingFragment extends Fragment {
    // View
    View view;

    // ___________LOADING_____________ //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        getData();
        initView();
        initValues();
        initActions();
        return view;
    }

    public void getData() {
        // get data for header menu

    }

    public void initView() {
        // getActivity().getActionBar().show();
        getActivity().findViewById(R.id.myToolbar).animate()
                .translationY(0)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(100)
                .start();

    }

    public void initValues() {

    }

    public void initActions() {


    }
}

