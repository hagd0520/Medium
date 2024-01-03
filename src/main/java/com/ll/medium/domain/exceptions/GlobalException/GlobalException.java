package com.ll.medium.domain.exceptions.GlobalException;

import com.ll.medium.global.rsData.RsData;
import lombok.Getter;

public class GlobalException extends RuntimeException {
    @Getter
    private RsData<?> rsData;
    public GlobalException(String resultCode, String msg) {
        super(resultCode + ": " + msg);

        rsData = RsData.of(resultCode, msg);
    }
}
