package com.laptrinhfulllstack.bookservice.query.controller;

import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laptrinhfulllstack.bookservice.query.model.BookResponseModel;
import com.laptrinhfulllstack.bookservice.query.queries.GetAllBookQuery;
import com.laptrinhfulllstack.bookservice.query.queries.GetBookDetailQuery;
import com.laptrinhfulllstack.commonservice.services.KafkaService;

@RestController
@RequestMapping("/api/v1/books")
public class BookQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @Autowired
    private KafkaService kafkaService;

    @GetMapping
    public List<BookResponseModel> getAllBooks() {
        GetAllBookQuery query = new GetAllBookQuery();
        return queryGateway.query(query,
                ResponseTypes.multipleInstancesOf(BookResponseModel.class)).join();
    }

    @GetMapping("{bookId}")
    public ResponseEntity<BookResponseModel> getBookDetail(@PathVariable String bookId) {
        GetBookDetailQuery query = new GetBookDetailQuery(bookId);

        BookResponseModel book = queryGateway.query(
                query,
                ResponseTypes.instanceOf(BookResponseModel.class)).join();

        return ResponseEntity.ok(book);
    }

    @PostMapping("/sendMessage")
    public void sendMesage(@RequestBody String message) {
        kafkaService.sendMessage("test", message);
    }
}