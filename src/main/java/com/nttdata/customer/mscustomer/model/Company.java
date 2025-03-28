package com.nttdata.customer.mscustomer.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;


@Data
@NoArgsConstructor
public class Company {
    @BsonId
    private String ruc;
    private String socialreason;
}