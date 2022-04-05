package com.bootcamp.ondemandreservation.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.FORBIDDEN, reason="ODRUser not found or wrong role")  // 404
public class ODRUserNotFoundException extends RuntimeException {

}
