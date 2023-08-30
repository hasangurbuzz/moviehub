package com.hasangurbuz.moviehub.service;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagedResult<T> {
    private List<T> results;
    private long total;
}
