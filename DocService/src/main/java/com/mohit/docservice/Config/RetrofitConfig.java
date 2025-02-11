package com.mohit.docservice.Config;

import com.mohit.docservice.HttpRequests.AuthService;
import io.micrometer.core.instrument.binder.okhttp3.OkHttpObservationInterceptor;
import io.micrometer.observation.ObservationRegistry;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfig {
    @Value("${endpoints.user-service}")
    private String BASE_URL;
    @Bean
    AuthService createAuthService(ObservationRegistry observationRegistry){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(
                    OkHttpObservationInterceptor.builder(observationRegistry, "http.client.requests").build()
                );
        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
                .create(AuthService.class);
    }
}
