package com.laptrinhfulllstack.commonservice.event;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookUpdateStatusEvent {
    private String BookId;
    private Boolean isReady;
    private String employeeId;
    private String borrowingId;
}
