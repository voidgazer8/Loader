package com.example.loader.interfaces;

import com.example.loader.primary.Requests;

public interface IRequest {

    void running(Requests reference, IRequest request) throws InterruptedException;

}
