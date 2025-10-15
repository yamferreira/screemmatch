package com.yamferreira.screemmatch.service;

public interface IConverterDados {
    <T> T obterDados (String json, Class<T> classe);
}
