package com.lawyus.study.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

public class SuccessClass {
    private Date date;

    private int status;

    public Boolean ifSuccess() {
        return true;
    }

    public int getStatus() {
        return 1;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public Date getDate() {
        return new Date();
    }

    public void setDate(Date d) {
        this.date = d;
    }

    public static void main(String[] args) throws JsonProcessingException {
        SuccessClass sc = new SuccessClass();
        ObjectMapper om = new ObjectMapper();

        System.out.println(om.writeValueAsString(sc));
    }
}
