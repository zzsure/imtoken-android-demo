package anzhi.com.http.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PongModel {
    @SerializedName("message") @Expose
    public String message;
}
