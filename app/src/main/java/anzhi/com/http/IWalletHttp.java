package anzhi.com.http;

import anzhi.com.http.response.EthBalanceModel;
import anzhi.com.http.response.PongModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IWalletHttp {
    @GET("/ping") Call<PongModel> ping();
    @GET("/api") Call<EthBalanceModel> getEthBalance(@Query("module") String module, @Query("action") String action, @Query("address") String address, @Query("tag") String tag);
}
