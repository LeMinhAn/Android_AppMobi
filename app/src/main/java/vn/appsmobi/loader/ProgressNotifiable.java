package vn.appsmobi.loader;

import android.os.Handler;


public abstract interface ProgressNotifiable {
    public abstract void init(boolean paramBoolean1, boolean paramBoolean2);

    public abstract void onError(boolean paramBoolean, BaseResult.ResultStatus paramResultStatus, Handler.Callback paramCallback);

    public abstract void startLoading(boolean paramBoolean);

    public abstract void stopLoading(boolean hasData, boolean hasNext);

    public abstract void stopLoading(boolean hasData, boolean hasNext, BaseResult.ResultStatus localResultStatus);
}

