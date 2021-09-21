package com.gch.webservice.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@RunWith(SpringRunner.class)
@WebService
public class DemoWebServiceImpl implements DemoWebService {
    @Override
    public void helloWord() {
        System.out.println("hello webservice");
    }

    @Test
    public void test() {
        Endpoint.publish("http://127.0.0.1:8081/DemoWebServiceImpl", new DemoWebServiceImpl());
    }

}
