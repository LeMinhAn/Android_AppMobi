package vn.appsmobi.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import net.steamcrafted.loadtoast.LoadToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import vn.appsmobi.R;
import vn.appsmobi.adapter.AppManageAdapter;
import vn.appsmobi.adapter.AppManageSectionAdapter;
import vn.appsmobi.model.DataCardItem;
import vn.appsmobi.utils.LocalAppManager;

import static vn.appsmobi.utils.UIUtils.getScreenHeight;

/**
 * Created by tobrother on 04/04/2016.
 */
public class HistorySettingFragment extends Fragment {
    // View
    View view;
    // init value
    ArrayList<DataCardItem> itemAppManages;
    List<AppManageSectionAdapter.Section> sections;
    AppManageAdapter appManageAdapter;
    AppManageSectionAdapter appManageSectionAdapter;
    Context context ;
    // image loader library
    DisplayImageOptions options;
    // init view
    RecyclerView rvAppManage;
    int sectionOfset = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_app_manage, container, false);
        context = getActivity();
        getValues();
        initViews();
        // initValue();
        initAction();
        // get Section
        sections = new ArrayList<>();
        new LoadApplications().execute();
        return view;
    }
    public void getValues() {
        // packageManager
        // Image loader
    }

    public void initViews() {
        rvAppManage = (RecyclerView) view.findViewById(R.id.rvAppManage);
        rvAppManage.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvAppManage.setLayoutManager(linearLayoutManager);
    }

    public void initValue() {
        AppManageSectionAdapter.Section[] dummy = new AppManageSectionAdapter.Section[sections.size()];
        appManageAdapter = new AppManageAdapter(context, itemAppManages);
        appManageSectionAdapter = new AppManageSectionAdapter(context, appManageAdapter, rvAppManage);
        appManageSectionAdapter.setSections(sections.toArray(dummy));
        rvAppManage.setAdapter(appManageSectionAdapter);
    }

    public void initAction() {

    }

    private void displayAboutDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("App loading .....");
        builder.setMessage("Đang lấy danh sách app đã cài đặt ");
        builder.setPositiveButton("Know More", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
            }
        });
        builder.setNegativeButton("No Thanks!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    // sync task for load data
    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        LoadToast lt = new LoadToast(getActivity());

        @Override
        protected Void doInBackground(Void... params) {
            itemAppManages = new ArrayList<>();
            if (DataCardItem.listCache.getAppDownloading().size() != 0) {
                itemAppManages.addAll(DataCardItem.listCache.getAppDownloading());
                sections.add(new AppManageSectionAdapter.Section(0, "Đang tải"));
                sectionOfset = sectionOfset + DataCardItem.listCache.getAppDownloading().size();
            }
            sections.add(new AppManageSectionAdapter.Section(sectionOfset, "Ứng dụng đã cài"));
            itemAppManages.addAll(LocalAppManager.getManager().getInstalledApp());
            sectionOfset = sectionOfset + LocalAppManager.getManager().getInstalledApp().size();
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            AppManageSectionAdapter.Section[] dummy = new AppManageSectionAdapter.Section[sections.size()];
            appManageAdapter = new AppManageAdapter(context, itemAppManages);
            appManageSectionAdapter = new AppManageSectionAdapter(context, appManageAdapter, rvAppManage);
            appManageSectionAdapter.setSections(sections.toArray(dummy));
            rvAppManage.setAdapter(appManageSectionAdapter);
            lt.success();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            lt.setText("Detecting");
            lt.setProgressColor(getResources().getColor(R.color.main_color));
            lt.show();
            lt.setTranslationY(getScreenHeight(context) / 2);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    public static long getFolderSize(File dir) {
        long size = 0;
        for (File file : dir.listFiles()) {
            Log.e("urlFile", file.getAbsolutePath());
        }
        return size;
    }

}

