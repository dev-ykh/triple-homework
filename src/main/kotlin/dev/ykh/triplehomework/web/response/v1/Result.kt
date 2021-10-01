package dev.ykh.triplehomework.web.response.v1

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Result<T>(
    var data: T? = null,
    val code: Int = 0,
    val description: String = "정상 처리 되었습니다.",
)
