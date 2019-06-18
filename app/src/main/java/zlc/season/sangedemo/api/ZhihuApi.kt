package zlc.season.sangedemo.api

import io.reactivex.Maybe
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import zlc.season.sangedemo.zhihu.DailyResp

interface ZhihuApi {

    @GET("/api/4/news/latest")
    fun getDaily(): Maybe<DailyResp>


    companion object {
        private const val BASE_URL = "https://news-at.zhihu.com/"

        fun create(): ZhihuApi = create(HttpUrl.parse(BASE_URL)!!)

        private fun create(httpUrl: HttpUrl): ZhihuApi {
            val client = OkHttpClient.Builder().build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ZhihuApi::class.java)
        }
    }

}