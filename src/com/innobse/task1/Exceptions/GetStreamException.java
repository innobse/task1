package com.innobse.task1.Exceptions;

/**
 * Exception, which throw when Streamer can't open file stream
 *
 */
public class GetStreamException extends Exception {
    GetStreamException(){
        this("Can't open stream!");
    }
    public GetStreamException(String message){
        super(message);
    }
}
