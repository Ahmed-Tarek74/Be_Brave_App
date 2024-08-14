package com.compose.data.remote
import com.compose.data.utils.FcmServiceUtil
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val fcmServiceUtil: FcmServiceUtil
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = fcmServiceUtil.getAccessToken()
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .addHeader("Content-Type", "application/json")
            .build()
        return chain.proceed(request)
    }
}