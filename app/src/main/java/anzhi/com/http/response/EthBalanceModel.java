package anzhi.com.http.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EthBalanceModel {
    @SerializedName("status") @Expose public String status;
    @SerializedName("message") @Expose public String message;
    @SerializedName("result") @Expose public String result;
}
