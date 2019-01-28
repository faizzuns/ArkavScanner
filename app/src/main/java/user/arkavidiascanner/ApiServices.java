package user.arkavidiascanner;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiServices {

    @GET("api/android/guest")
    Call<BaseResponse> callUser(@Query("token") String token, @Header("Authorization") String auth);

    @POST("/api/android/guest")
    Call<BaseResponse> confirmUser(@Header("Content-Type") String contentType, @Header("Authorization") String auth, @Body User user);

}
