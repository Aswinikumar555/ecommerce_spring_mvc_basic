/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vedantu.requests;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author ajith
 */
public abstract class AbstractReq {

    protected List<String> collectVerificationErrors() {
        return new ArrayList<>();
    }

    public void verify() throws RuntimeException {

        List<String> verificationErrors = collectVerificationErrors();
        if (verificationErrors != null && !verificationErrors.isEmpty()) {
            throw new RuntimeException("req error");
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
