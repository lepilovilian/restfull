package com.ilepilov.restfull.response;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class ErrorMessage {

    @NonNull private Date date;
    @NonNull private String message;
}
