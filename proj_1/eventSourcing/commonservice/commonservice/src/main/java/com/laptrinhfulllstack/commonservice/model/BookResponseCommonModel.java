package com.laptrinhfulllstack.commonservice.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseCommonModel {
    private String id;
    private String name;
    private String author;
    private Boolean isReady;
}
