package com.dj.p2p.pojo.project;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoanContract implements Serializable {

    private Integer id;
    private String loanContract;

    private String  chargeType;
}
