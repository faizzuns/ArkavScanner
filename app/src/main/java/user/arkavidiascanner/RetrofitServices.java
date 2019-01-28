package user.arkavidiascanner;


public class RetrofitServices {
    public static ApiServices sendRequest() {
        return RetrofitClient.client().create(ApiServices.class);
    }
}
