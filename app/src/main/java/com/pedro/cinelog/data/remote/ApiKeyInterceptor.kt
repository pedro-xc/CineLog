package com.pedro.cinelog.data.remote

import com.pedro.cinelog.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Adiciona automaticamente o parâmetro api_key (vindo do BuildConfig, configurado
 * via local.properties) em toda requisição feita para a API do TMDb.
 */
class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requisicaoOriginal = chain.request()
        val urlComApiKey = requisicaoOriginal.url.newBuilder()
            .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .build()
        val requisicaoComApiKey = requisicaoOriginal.newBuilder()
            .url(urlComApiKey)
            .build()
        return chain.proceed(requisicaoComApiKey)
    }
}
