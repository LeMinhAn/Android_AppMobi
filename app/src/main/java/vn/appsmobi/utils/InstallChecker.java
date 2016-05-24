package vn.appsmobi.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import vn.appsmobi.R;
import vn.appsmobi.model.ApplicationItem;
import vn.appsmobi.model.DataCardItem;

/**
 * Created by tobrother on 17/02/2016.
 */
public class InstallChecker {
    private static boolean mIsNoEnoughSpaceDialogShowing = false;
    private static boolean mIsPermissionDialogShowing = false;
    private static String mPermissionString = "";
    static Dialog dialog = null;
    static TextView permission_name = null;
    static ListView permission_list = null;
    static Button permission_btn = null;
    static ProgressBar progress = null;
    static RelativeLayout permission_wrap = null;


    public static void checkAndInstall(DataCardItem appInfo, Activity activity) {
        if ((appInfo == null))
            return;
        if (!Utils.Network.isNetWorkConnected(AppMobiApplication._())) {
            Toast.makeText(AppMobiApplication._(), activity.getApplicationContext().getString(R.string.network_unavaliable), Toast.LENGTH_SHORT).show();
        } else {
            //    	if(DownloadInstallManager.getManager().isDownloadingOrInstalling(appInfo.getId()))
            //    	{
            //    		DownloadInstallManager.getManager().arrange(appInfo);
            //    	}
            //    	else
            //    	{
            //    		DownloadInstallManager.getManager().arrange(appInfo);
            //    		/*
            //    		if(LoginManager.getManager().hasLogin())
            //        	{
            //      		  DownloadInstallManager.getManager().arrange(appInfo);
            //      	  	}
            //        	else
            //      	  	{
            //      		//FragmentManager fm = ((Fragment) activity).getFragmentManager();
            //      		  InstallChecker.showLoginDownload(activity, appInfo);
            //      		//NeedLoginDialog dialog =  new NeedLoginDialog(appInfo);
            //      		//dialog.show(fm, "install_downlad");
            //      	  	}
            //      	  	*/
            //}
            Log.e("InstallChecker_click", "true");
            DownloadInstallManager.getManager().setActivityContext(activity);
            DownloadInstallManager.getManager().arrange(appInfo);

        }

    }

    /*
    public static void showLoginDownload(Context ctx, final ApplicationItem appInfo) {

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.app_alert_dialog, null);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                ctx);
        alertDialog.setView(layout);
        // Setting Dialog Title
        //alertDialog.setTitle(ctx.getString(R.string.result_notice));
        // Setting Dialog Message
        alertDialog.setMessage(ctx.getString(R.string.login_download));


        alertDialog.setPositiveButton(ctx.getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoginCallback mLoginCallBack = new LoginCallback() {
                    @Override
                    public void onLoginSucceed(Account mAccount) {
                        DownloadInstallManager.getManager().arrange(appInfo);

                    }

                    @Override
                    public void onLoginFailed() {
                        DownloadInstallManager.getManager().arrange(appInfo);

                    }
                };
                LoginManager.getManager().login(mLoginCallBack);

            }
        });

        alertDialog.setNegativeButton(ctx.getString(R.string.Skip), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                DownloadInstallManager.getManager().arrange(appInfo);
            }
        });
        // Setting OK Button

        alertDialog.create();
        //.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();


    }
    */
    public static void showNoEnoughSpaceDialog(Context ctx) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                ctx).create();

        // Setting Dialog Title
        alertDialog.setTitle(ctx.getString(R.string.Error));
        // Setting Dialog Message
        alertDialog.setMessage("Tải xuống không thành công. Dung lượng lưu trữ hiện tại không đủ.");
        alertDialog.setButton(ctx.getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
    }


    public static void showPermissionDialog(Context context, final ApplicationItem appinfo, final boolean isInstall) {
        //Log.v("showPermissionDialog");
        //if(mIsPermissionDialogShowing)
        //	return;


        // custom dialog
        dialog = new Dialog(context, R.style.dialog);
        dialog.requestWindowFeature(dialog.getWindow().FEATURE_NO_TITLE);

        //dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_title_bar);

        //Log.v("showPermissionDialog1");
        dialog.setContentView(R.layout.app_permission);

			/*LayoutParams lp=dialog.getWindow().getAttributes();
             lp.x=100;lp.y=100;lp.width=100;lp.height=200;lp.gravity=Gravity.TOP | Gravity.LEFT;
	         lp.dimAmount=0;
	         lp.flags=LayoutParams.FLAG_LAYOUT_NO_LIMITS | LayoutParams.FLAG_NOT_TOUCH_MODAL;
	         dialog.getWindow().setAttributes(lp);
*/
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        //Log.v("showPermissionDialog3");
        dialog.setTitle(context.getString(R.string.app_permission));
        permission_wrap = (RelativeLayout) dialog.findViewById(R.id.permission_wrap);
        // set the custom dialog components - text, image and button
        permission_name = (TextView) dialog.findViewById(R.id.permission_name);
        permission_name.setText(Html.fromHtml(context.getResources().getString(R.string.app_permission_needaccess, appinfo.getName())));
        permission_list = (ListView) dialog.findViewById(R.id.permission_list);
        permission_btn = (Button) dialog.findViewById(R.id.permission_btn);
        progress = (ProgressBar) dialog.findViewById(R.id.progress);
        // if button is clicked, close the custom dialog
        permission_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isInstall)
                    DownloadInstallManager.getManager().arrange(appinfo); // install application
            }
        });
        //Log.v("showPermissionDialog 4");
        // PermissionTask task = new PermissionTask(context, appinfo);
        // task.execute();
    }

    /*
    static class PermissionTask extends AsyncTask<String, Void, String> {
        Context mContext;
        ApplicationItem appinfo;
        LinkedHashMap<String, String> list = new LinkedHashMap<String, String>();

        public PermissionTask(Context context, ApplicationItem appinfo) {
            mContext = context;
            this.appinfo = appinfo;
            //Log.v("showPermissionDialog 5");
            //Log.v(this.appinfo.getName());
        }


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            InstallChecker.mPermissionString = "";
            permission_wrap.setVisibility(View.GONE);
            //Log.v("showPermissionDialog 6");
            dialog.show();
        }


        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            DefaultHttpClient client = new DefaultHttpClient();
            //Log.v("showPermissionDialog 7");
            HttpGet httpGet = new HttpGet(String.format(Constants.GET_PERMISSION_URL, this.appinfo.getId()));
            try {
                //Log.v("showPermissionDialog 8");
                HttpResponse execute = client.execute(httpGet);
                InputStream content = execute.getEntity().getContent();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
                //Log.v("PermissionTask:" + response);
                if (response != null && !TextUtils.isEmpty(response)) {
                    //Log.v("showPermissionDialog 9");
                    InstallChecker.mPermissionString = response;
                    //Log.v("showPermissionDialog 10");

                    // Log.v("showPermissionDialog 11");
                    CharSequence csPermissionGroupLabel;
                    CharSequence csPermissionLabel;

                    PackageManager pm = AppMobiApplication._().getPackageManager();
                    List<PermissionGroupInfo> lstGroups = pm.getAllPermissionGroups(0);
                    for (PermissionGroupInfo pgi : lstGroups) {
                        csPermissionGroupLabel = pgi.loadLabel(pm);
                        String pemissionDesc = pgi.loadDescription(pm).toString();
                        try {
                            List<PermissionInfo> lstPermissions = pm.queryPermissionsByGroup(pgi.name, 0);
                            for (PermissionInfo pi : lstPermissions) {
                                csPermissionLabel = pi.loadLabel(pm);

                                if (InstallChecker.mPermissionString.contains(pi.name)) {
                                    //Log.v("perm" +  pgi.name + ": " + csPermissionGroupLabel.toString() + ":"+ pemissionDesc);
                                    if (list.get(csPermissionGroupLabel.toString()) != null) {
                                        String val = list.get(csPermissionGroupLabel.toString());
                                        val = val + ", " + csPermissionLabel.toString();
                                        list.put(csPermissionGroupLabel.toString(), val);
                                    } else
                                        list.put(csPermissionGroupLabel.toString(), csPermissionLabel.toString());
                                }

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }


                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }


        @Override
        protected void onPostExecute(String result) {

            PermissionAdapter listAdapter = new PermissionAdapter(mContext, list);
            permission_list.setAdapter(listAdapter);
            permission_wrap.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }


    }
    */
    static class PermissionAdapter extends BaseAdapter {

        LinkedHashMap<String, String> mList = new LinkedHashMap<String, String>();
        Context mContext;

        public PermissionAdapter(Context context, LinkedHashMap<String, String> list) {
            this.mList = list;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                view = inflater.inflate(R.layout.list_permission_item, null);

            }

            TextView permission_name = (TextView) view.findViewById(R.id.permission_name);
            TextView permission_desc = (TextView) view.findViewById(R.id.permission_desc);
            String name = (new ArrayList<String>(mList.keySet())).get(position);
            String desc = (new ArrayList<String>(mList.values())).get(position);
            permission_name.setText(name);
            permission_desc.setText(desc);

            return view;
        }


    }
  /*public static void checkAndInstall(Collection<AppInfo> paramCollection, RefInfo paramRefInfo, Activity paramActivity)
  {
    ArrayList localArrayList = filterApps(paramCollection);
    if (localArrayList.isEmpty());
    while (true)
    {
      return;
      FragmentManager localFragmentManager = paramActivity.getSupportFragmentManager();
      if (checkLoginForInstall(localFragmentManager))
        if (!MarketUtils.isConnected(paramActivity))
          new NoNetworkDialog().show(localFragmentManager, "install_no_network");
        else if ((MarketUtils.isDownloadOnlyOnWifi(paramActivity)) && (!MarketUtils.isWifiConnected(paramActivity)))
          new InstallOnWifiDialog().show(localFragmentManager, "install_only_on_wifi");
        else if (paramCollection.size() == 1)
          DownloadInstallManager.getManager().arrange((AppInfo)localArrayList.get(0), paramRefInfo);
        else
          new InstallAllDialog(localArrayList, paramRefInfo).show(localFragmentManager, "install_all");
    }
  }

  public static boolean checkAndInstall(Collection<AppInfo> paramCollection, RefInfo paramRefInfo)
  {
    boolean bool = false;
    ArrayList localArrayList = filterApps(paramCollection);
    if (localArrayList.isEmpty());
    while (true)
    {
      return bool;
      Context localContext = MarketApp.getMarketContext();
      if ((checkLoginForInstall()) && (MarketUtils.isScreenLocked(localContext)) && (MarketUtils.isConnected(localContext)) && (MarketUtils.isWifiConnected(localContext)))
      {
        Iterator localIterator = localArrayList.iterator();
        while (localIterator.hasNext())
        {
          AppInfo localAppInfo = (AppInfo)localIterator.next();
          DownloadInstallManager.getManager().arrange(localAppInfo, paramRefInfo);
        }
        bool = true;
      }
    }
  }


  private static ArrayList<AppInfo> filterApps(Collection<AppInfo> paramCollection)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      AppInfo localAppInfo = (AppInfo)localIterator.next();
      if ((localAppInfo.canInstallOrUpdate()) && (!DownloadInstallManager.getManager().isDownloadingOrInstalling(localAppInfo.appId)))
        localArrayList.add(localAppInfo);
    }
    return localArrayList;
  }

  public static void showNoEnoughSpaceDialog(Context paramContext)
  {
    if (mIsNoEnoughSpaceDialogShowing);
    while (true)
    {
      return;
      mIsNoEnoughSpaceDialogShowing = true;
      AlertDialog localAlertDialog = new AlertDialog.Builder(paramContext, 2131296262).setTitle(paramContext.getString(2131230853)).setPositiveButton(2131230913, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          InstallChecker.access$502(false);
          try
          {
            Intent localIntent = new Intent();
            localIntent.setFlags(268435456);
            localIntent.setAction("miui.intent.action.GARBAGE_CLEANUP");
            InstallChecker.this.startActivity(localIntent);
            return;
          }
          catch (ActivityNotFoundException localActivityNotFoundException)
          {
            while (true)
              Log.e("MarketInstallChecker", "ActivityNotFoundException occurs for intent action miui.content.ExtraIntent.ACTION_GARBAGE_CLEANUP");
          }
          catch (AndroidRuntimeException localAndroidRuntimeException)
          {
            while (true)
              Log.e("MarketInstallChecker", "AndroidRuntimeException occurs for intent action miui.content.ExtraIntent.ACTION_GARBAGE_CLEANUP");
          }
          catch (SecurityException localSecurityException)
          {
            while (true)
              Log.e("MarketInstallChecker", "SecurityException occurs for intent action miui.content.ExtraIntent.ACTION_GARBAGE_CLEANUP");
          }
        }
      }).setNegativeButton(2131230911, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          InstallChecker.access$502(false);
        }
      }).setOnCancelListener(new DialogInterface.OnCancelListener()
      {
        public void onCancel(DialogInterface paramAnonymousDialogInterface)
        {
          InstallChecker.access$502(false);
        }
      }).create();
      localAlertDialog.getWindow().setType(2003);
      localAlertDialog.show();
    }
  }


  public static class InstallOnWifiDialog extends DialogFragment
  {
    public Dialog onCreateDialog(Bundle paramBundle)
    {
      String str1 = getString(2131230903);
      String str2 = getString(2131230906);
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
      localBuilder.setTitle(str1).setMessage(str2).setPositiveButton(2131230911, null).setNegativeButton(2131230912, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          Intent localIntent = new Intent("android.intent.action.VIEW_DOWNLOADS");
          InstallChecker.InstallOnWifiDialog.this.startActivity(localIntent);
        }
      });
      return localBuilder.create();
    }
  }


  public static class InstallAllDialog extends DialogFragment
  {

  public static class NoNetworkDialog extends DialogFragment
  {
    public Dialog onCreateDialog(Bundle paramBundle)
    {
      String str1 = getString(2131230903);
      String str2 = getString(2131230904);
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
      localBuilder.setTitle(str1).setMessage(str2).setPositiveButton(2131230911, null);
      return localBuilder.create();
    }
  }

*/
}