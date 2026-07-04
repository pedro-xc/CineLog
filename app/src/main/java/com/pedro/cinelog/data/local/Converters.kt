package com.pedro.cinelog.data.local

import androidx.room.TypeConverter

/**
 * Converte o enum StatusAssistido para String e vice-versa, para o Room conseguir persistir.
 */
class Converters {
    @TypeConverter
    fun statusParaString(status: StatusAssistido): String = status.name

    @TypeConverter
    fun stringParaStatus(valor: String): StatusAssistido = StatusAssistido.valueOf(valor)
}
