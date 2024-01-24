package kr.co.rinnai.dms.common.service;

//import com.google.android.gms.common.api.Api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public class ApiService {
    public Api api;
    private String API_URL="http://58.72.180.60/";
    private String downloadUrl = "";
    //http://58.72.180.60/mobileservice/deploy/last/SmartDMS.apk

    private ApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL).build();

        api = retrofit.create(Api.class);


    }

    public static  ApiService getInstance() {
        return Holder.apiService;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    private static class Holder {
        public static final ApiService apiService = new ApiService();

    }

    interface Api {
        @GET
        @Streaming
        Call<ResponseBody> downloadFile(@Url String url);
    }

}
