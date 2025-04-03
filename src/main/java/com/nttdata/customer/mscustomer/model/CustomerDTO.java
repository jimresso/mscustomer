package com.nttdata.customer.mscustomer.model;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.openapitools.model.Customer;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customers")
public class CustomerDTO {
    @BsonId
    private String id;
    private String dni;
    private String customertype;
    private String email;
    private String lastname;
    private String name;
    private ClienttypeEnum clienttype;
    private  Company company;


    public enum ClienttypeEnum {
        VIP,
        PYME,
    }

}
