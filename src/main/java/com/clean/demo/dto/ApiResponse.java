package com.clean.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean ok;
    private int status;
    private T data;
    private String error;
    private String message;
    private PaginationInfo pagination;

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder().ok(true).status(200).data(data).message(message).build();
    }

    @Data
    @Builder
    public static class PaginationInfo {
        private int current_page;
        private int last_page;
        private int per_page;
        private long total;
    }
}
