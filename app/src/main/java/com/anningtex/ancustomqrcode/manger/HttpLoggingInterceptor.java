package com.anningtex.ancustomqrcode.manger;

import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @ClassName: HttpLoggingInterceptor
 * @Author: alvis
 */
public class HttpLoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //拿到Request对象
        Request request = chain.request();
        long t1 = System.nanoTime();
        Log.e("日志拦截器", " request  = " + String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));

        //拿到Response对象
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        final String str = responseBody.string();
        Log.d("\n日志拦截器", "url :" + request.url() + "\n" + " request  = " + str);
        long t2 = System.nanoTime();
        //得出请求网络,到得到结果,中间消耗了多长时间
        Log.e("日志拦截器", "response  " + String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        if ("POST".equals(request.method())) {
            if (request.body() instanceof FormBody) {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                FormBody formBody = (FormBody) request.body();
                // 先复制原来的参数
                for (int i = 0; i < formBody.size(); i++) {
                    bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                    Log.e("参数", formBody.encodedName(i) + "  " + formBody.encodedValue(i));
                }
            }
        }
        return response;
    }
}
