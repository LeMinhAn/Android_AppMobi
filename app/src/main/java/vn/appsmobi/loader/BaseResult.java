
package vn.appsmobi.loader;

import java.util.HashMap;
import java.util.Map;

import vn.appsmobi.R;


public abstract class BaseResult {
    public enum ResultStatus {
        NETWROK_ERROR,
        SERVICE_ERROR,
        DATA_ERROR,
        AUTH_ERROR,
        IP_ERROR,
        OK //
    }

    private static Map<ResultStatus, Integer> RESULT_STATUS_DES = new HashMap<BaseResult.ResultStatus, Integer>();

    static {
        RESULT_STATUS_DES.put(ResultStatus.NETWROK_ERROR, R.string.network_unavaliable);
        RESULT_STATUS_DES.put(ResultStatus.SERVICE_ERROR, R.string.service_unavailiable);
        RESULT_STATUS_DES.put(ResultStatus.DATA_ERROR, R.string.data_error);
        RESULT_STATUS_DES.put(ResultStatus.AUTH_ERROR, R.string.auth_error);
        RESULT_STATUS_DES.put(ResultStatus.IP_ERROR, R.string.ip_error);
    }

    public static int getStatusDes(ResultStatus status) {
        return RESULT_STATUS_DES.get(status);
    }


    public abstract BaseResult shallowClone();

    private ResultStatus mResultStatus = ResultStatus.OK;

    protected abstract int getCount();

    public ResultStatus getResultStatus() {
        return mResultStatus;
    }

    public void setResultStatus(ResultStatus status) {
        mResultStatus = status;
    }
}
