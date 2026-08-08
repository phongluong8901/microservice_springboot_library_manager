package com.laptrinhfulllstack.bookservice.query.projection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.laptrinhfulllstack.bookservice.command.data.Book;
import com.laptrinhfulllstack.bookservice.command.data.BookRepository;
import com.laptrinhfulllstack.bookservice.command.model.BookRequestModel;
import com.laptrinhfulllstack.bookservice.query.model.BookResponseModel;
import com.laptrinhfulllstack.bookservice.query.queries.GetAllBookQuery;
import com.laptrinhfulllstack.bookservice.query.queries.GetBookDetailQuery;

@Component
public class Bookprojection {
    @Autowired
    private BookRepository bookRepository;

    @QueryHandler
    public List<BookResponseModel> handle(GetAllBookQuery query) {
        List<Book> list = bookRepository.findAll();
        List<BookResponseModel> listBookResponse = new ArrayList<>();
        list.forEach(book -> {
            BookResponseModel model = new BookResponseModel();
            BeanUtils.copyProperties(book, model);
            listBookResponse.add(model);
        });

        return listBookResponse;
    }

    @QueryHandler
    public BookResponseModel handle(GetBookDetailQuery query) {
        BookResponseModel bookResponseModel = new BookResponseModel();

        bookRepository.findById(query.getId()).ifPresent(book -> {
            BeanUtils.copyProperties(book, bookResponseModel);
        });
        return bookResponseModel;
    }
}
