package zlc.season.sangedemo.api

import io.reactivex.Maybe
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import zlc.season.sangedemo.github.GithubRepositoryResp

interface GithubApi {

    @GET("/search/repositories?sort=stars&order=desc&per_page=20")
    fun searchRepository(@Query("q") key: String, @Query("page") page: Int): Maybe<GithubRepositoryResp>


    companion object {
        private const val BASE_URL = "https://api.github.com/"

        fun create(): GithubApi = create(HttpUrl.parse(BASE_URL)!!)

        private fun create(httpUrl: HttpUrl): GithubApi {
            val client = OkHttpClient.Builder().build()
            return Retrofit.Builder()
                    .baseUrl(httpUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(GithubApi::class.java)
        }
    }

}