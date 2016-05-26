package vn.appsmobi.ui;

import android.content.Context;
import android.os.Handler.Callback;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import vn.appsmobi.R;
import vn.appsmobi.loader.BaseResult;
import vn.appsmobi.loader.ProgressNotifiable;

public class EmptyLoadingView extends LinearLayout implements ProgressNotifiable {
    private TextView mActionButton;
    private String mButtonTextFailed;
    String mButtonTextSuccess;
    private Context mContext;
    private View mDataLoadingView;
    boolean mNeedSuccessActionButton;
    private NoNewDataCallback mNoNewDataCallback;
    private ProgressBar mProgressBar;
    //    private TextView mProgressText;
    private Refreshable mRefreshable;
    String mTextDefaultLoading;
    private String mTextNoActiveNetWork;
    private String mTextServerError;
    private String mTextSuccessDefault;
    private TextView mTextView;
    private LinearLayout mErrorArea;

    public EmptyLoadingView(Context context) {
        this(context, null);
    }

    public EmptyLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.empty_loading, this, true);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mErrorArea = (LinearLayout) findViewById(R.id.error_view_container);
        mTextView = (TextView) findViewById(R.id.txt_message);
        mActionButton = (TextView) findViewById(R.id.txt_act);
        mTextNoActiveNetWork = getResources().getString(R.string.network_unavaliable);
        mTextServerError = getResources().getString(R.string.service_unavailiable);
        mTextSuccessDefault = "";
        mButtonTextFailed = getResources().getString(R.string.text_tryagain);
        mButtonTextSuccess = "";
        mNeedSuccessActionButton = true;
//        setBackgroundColor(mContext.getResources().getColor(R.color.primaryColor));
    }

    public void setDataLoadingView(View dataLoadingView) {
        mDataLoadingView = dataLoadingView;
    }

    @SuppressWarnings("deprecation")
    private void updateStyle(boolean hasData) {
        if (mDataLoadingView == null) {
            if (hasData) {
                getLayoutParams().height = LayoutParams.WRAP_CONTENT;
//                setBackgroundResource(R.drawable.loading_view_bg);
                return;
            }
            getLayoutParams().height = LayoutParams.FILL_PARENT;
//            setBackgroundDrawable(null);
        }
    }

    //Bắt đầu view progress
    public void startLoading(boolean hasData) {
        updateStyle(hasData);
        showProgressView(hasData, true);
        mTextView.setVisibility(View.GONE);
        mActionButton.setVisibility(View.GONE);
        if ((mDataLoadingView != null) && (hasData)) {
            showView(mDataLoadingView);
            hideView(this);
            return;
        }
        showView(this);
        hideView(mDataLoadingView);
    }

    //Ngừng view progress
    public void stopLoading(boolean hasData, boolean hasNext) {
        if (!hasNext) {
            updateStyle(hasData);
            hideView(mDataLoadingView);
            if (hasData) {
                hideView(this);
                return;
            }
            showView(this);
            showProgressView(hasData, false);
            mTextView.setVisibility(View.VISIBLE);
        }
    }

    public void stopLoading(boolean hasData, boolean hasNext, int errorCode) {

    }

    //Hiện view progress
    private void showView(View view) {
        if (view == null) {
            return;
        }
        if (view.getVisibility() == View.GONE) {
            view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.appear));
            view.setVisibility(View.VISIBLE);
        }
    }

    //Ẩn view progress
    private void hideView(View view) {
        if (view == null) {
            return;
        }
        if (view.getVisibility() == View.VISIBLE) {
            if (view.isShown()) {
                view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.disappear));
            }
            view.setVisibility(View.GONE);
        }
    }

    public void init(boolean hasData, boolean isLoading) {
        updateStyle(hasData);
        mActionButton.setVisibility(View.GONE);
        if (isLoading) {
            if ((mDataLoadingView != null) && (hasData)) {
                setVisibility(View.GONE);
                mDataLoadingView.setVisibility(View.VISIBLE);
                return;
            }
            setVisibility(View.VISIBLE);
            showProgressView(hasData, true);
            mTextView.setVisibility(View.GONE);
            return;
        }

        if (mDataLoadingView != null) {
            mDataLoadingView.setVisibility(View.GONE);
        }
        if (hasData) {
            setVisibility(View.GONE);
            return;
        }
        setVisibility(View.VISIBLE);
        showProgressView(hasData, false);
        mTextView.setVisibility(View.VISIBLE);
    }

    private void showProgressView(boolean hasData, boolean visibile) {
        if (visibile) {
            mProgressBar.setVisibility(View.VISIBLE);
//            mProgressText.setVisibility(hasData ? 0x8 : 0x0);
            return;
        }
        mProgressBar.setVisibility(View.GONE);
//        mProgressText.setVisibility(0x8);
    }

    public void setNeedSuccessActionButton(boolean isNeed) {
        mNeedSuccessActionButton = isNeed;
    }

    public void setTextSuccessDefault(String text) {
        mTextSuccessDefault = text;
        mTextView.setText(mTextSuccessDefault);
    }

    public void setTextDefaultLoading(String text) {
        mTextDefaultLoading = text;
//        mProgressText.setText(mTextDefaultLoading);
    }

    public void setRefreshable(Refreshable refreshable) {
        mRefreshable = refreshable;
    }

    //    private View.OnClickListener mOnSearchListener = new View.OnClickListener() {
//
//        public void onClick(View p1) {
//            Intent localIntent1 = new Intent(mContext, SearchActivity.class);
//            mContext.startActivity(localIntent1);
//        }
//    };
    private View.OnClickListener mOnRefreshListener = new View.OnClickListener() {

        public void onClick(View v) {
            if (mRefreshable != null) {
                mRefreshable.refreshData();
            }
        }
    };

    public void setNoNewDataCallback(EmptyLoadingView.NoNewDataCallback noNewDataCallback) {
        mNoNewDataCallback = noNewDataCallback;
    }

    public static abstract interface NoNewDataCallback {
        public abstract boolean onNoNewData();
    }

    @Override
    public void onError(boolean paramBoolean, BaseResult.ResultStatus paramResultStatus,
                        Callback paramCallback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stopLoading(boolean hasData, boolean hasNext,
                            BaseResult.ResultStatus errorCode) {
        stopLoading(hasData, hasNext);
        if (!hasNext) {
            switch (errorCode) {
                case OK: {
                    if (!hasData) {
                        mErrorArea.setVisibility(View.GONE);
//	                        mTextView.setText(mTextSuccessDefault);
//	                        mActionButton.setText(mButtonTextSuccess);
//	                        if(mNeedSuccessActionButton) {
//	                            mActionButton.setVisibility(View.VISIBLE);
////	                            mActionButton.setOnClickListener(mOnSearchListener);
//	                        }
                    }
                    return;
                }

                case NETWROK_ERROR: {
                    if (hasData) {
                        Toast.makeText(mContext, mTextNoActiveNetWork, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mErrorArea.setVisibility(View.VISIBLE);
                    mTextView.setText(mTextNoActiveNetWork);
                    mActionButton.setVisibility(View.VISIBLE);
                    mActionButton.setText(mButtonTextFailed);
                    mActionButton.setOnClickListener(mOnRefreshListener);
                    return;
                }
                case SERVICE_ERROR: {
                    if (hasData) {
                        Toast.makeText(mContext, mTextServerError, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mErrorArea.setVisibility(View.VISIBLE);
                    mTextView.setText(mTextServerError);
                    mActionButton.setVisibility(View.VISIBLE);
                    mActionButton.setText(mButtonTextFailed);
                    mActionButton.setOnClickListener(mOnRefreshListener);
                    return;
                }
                case DATA_ERROR: {
                    if ((mNoNewDataCallback == null) || (!mNoNewDataCallback.onNoNewData())) {
//	                	Toast.makeText(mContext, mContext.getString(R.string.data_not_exist),Toast.LENGTH_SHORT).show();
                        Toast.makeText(mContext, mContext.getString(R.string.data_error), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        }

    }
}