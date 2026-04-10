package com.clean.demo.dto;

import java.util.List;

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
    private List<String> errors;
    private List<String> messages;
    private PaginationInfo pagination;

    @Data
    @Builder
    public static class PaginationInfo {
        private int current_page;
        private int last_page;
        private int per_page;
        private long total;
    }
}
