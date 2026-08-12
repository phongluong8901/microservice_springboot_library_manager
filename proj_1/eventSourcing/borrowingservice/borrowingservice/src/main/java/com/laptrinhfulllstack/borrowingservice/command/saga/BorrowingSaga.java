package com.laptrinhfulllstack.borrowingservice.command.saga;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.laptrinhfulllstack.commonservice.queries.GetBookDetailQuery;
import com.laptrinhfulllstack.borrowingservice.command.command.DeleteBorrowingCommand;
import com.laptrinhfulllstack.borrowingservice.command.event.BorrowingCreatedEvent;
import com.laptrinhfulllstack.borrowingservice.command.event.BorrowingDeletedEvent;
import com.laptrinhfulllstack.borrowingservice.command.model.BorrowingCreateModel;
import com.laptrinhfulllstack.commonservice.command.RollBackStatusBookCommand;
import com.laptrinhfulllstack.commonservice.command.UpdateStatusBookCommand;
import com.laptrinhfulllstack.commonservice.event.BookRollBackStatusEvent;
import com.laptrinhfulllstack.commonservice.event.BookUpdateStatusEvent;
import com.laptrinhfulllstack.commonservice.model.BookResponseCommonModel;
import com.laptrinhfulllstack.commonservice.model.EmployeeResponseCommonModel;
import com.laptrinhfulllstack.commonservice.queries.GetDetailEmployeeQuery;

import io.axoniq.axonserver.grpc.query.Query;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Saga
public class BorrowingSaga {
    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "id")
    private void handle(BorrowingCreatedEvent event) {
        log.info("BorrowingCreatedEvent in Saga for BookId: " + event.getBookId() + " : Employee: "
                + event.getEmployeeId());

        try {
            GetBookDetailQuery getBookDetailQuery = new GetBookDetailQuery(event.getBookId());
            BookResponseCommonModel bookResponseCommonModel = queryGateway.query(getBookDetailQuery,
                    ResponseTypes.instanceOf(BookResponseCommonModel.class)).join();

            // Kiểm tra nếu sách không sẵn sàng (isReady = false) thì báo lỗi
            if (!bookResponseCommonModel.getIsReady()) {
                throw new Exception("Sach da co nguoi muon");
            } else {
                SagaLifecycle.associateWith("bookId", event.getBookId());
                // Cập nhật trạng thái sách thành false (không còn sẵn sàng vì đã được mượn)
                UpdateStatusBookCommand command = new UpdateStatusBookCommand(event.getBookId(), false,
                        event.getEmployeeId(), event.getId());
                commandGateway.sendAndWait(command);
            }
        } catch (Exception e) {
            rollbackBorrowingRecord(event.getId());
            log.error(e.getMessage());
        }
    }

    @SagaEventHandler(associationProperty = "bookId")
    private void handler(BookUpdateStatusEvent event) {
        log.info("BookUpdateStatusEvent in Saga for BookId: " + event.getBookId());
        try {
            GetDetailEmployeeQuery query = new GetDetailEmployeeQuery(event.getEmployeeId());
            EmployeeResponseCommonModel employeeModel = queryGateway
                    .query(query, ResponseTypes.instanceOf(EmployeeResponseCommonModel.class)).join();
            if (employeeModel.getIsDiscriplined()) {
                throw new Exception("Nhan vien bi ki luat");
            } else {
                log.info("Da muon sach thanh cong");
                SagaLifecycle.end();
            }

        } catch (Exception e) {
            rollBackBookStatus(event.getBookId(), event.getEmployeeId(), event.getBorrowingId());
            log.error(e.getMessage());
        }

    }

    private void rollbackBorrowingRecord(String id) {
        DeleteBorrowingCommand command = new DeleteBorrowingCommand(id);
        commandGateway.sendAndWait(command);
        SagaLifecycle.end();
    }

    private void rollBackBookStatus(String bookId, String employeeId, String borrowingId) {
        SagaLifecycle.associateWith("bookId", bookId);
        RollBackStatusBookCommand command = new RollBackStatusBookCommand(bookId, true, employeeId, borrowingId);
        commandGateway.sendAndWait(command);

    }

    @SagaEventHandler(associationProperty = "bookId")
    private void handle(BookRollBackStatusEvent event) {
        log.info("BookRollBackStatusEvent in Saga for book Id: {} " + event.getBookId());
        rollbackBorrowingRecord(event.getBorrowingId());
    }

    @SagaEventHandler(associationProperty = "id")
    @EndSaga
    private void handle(BorrowingDeletedEvent event) {
        log.info("BorrowDeletedEvent in Saga for Borrwing Id: {}", event.getId());
        SagaLifecycle.end();
    }
}
