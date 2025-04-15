package com.nttdata.customer.mscustomer.model;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customers")
public class CustomerDTO {
    @BsonId
    private String id;
    private String dni;
    private String customerId;
    private String email;
    private String lastname;
    private String name;
    private  Company company;


}
