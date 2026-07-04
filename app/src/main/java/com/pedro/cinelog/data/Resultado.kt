package com.pedro.cinelog.data

/**
 * Representa o estado de uma operação assíncrona (chamada de rede, leitura do banco, etc.)
 * para que as telas possam exibir loading, sucesso ou uma mensagem de erro amigável.
 */
sealed class Resultado<out T> {
    data object Carregando : Resultado<Nothing>()
    data class Sucesso<T>(val dados: T) : Resultado<T>()
    data class Erro(val mensagem: String) : Resultado<Nothing>()
}
