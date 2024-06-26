package ru.otus.hw.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.RatingCalculationException;
import ru.otus.hw.exceptions.TourGamesGenerationException;

@ControllerAdvice
@Slf4j
public class ErrorControllerAdvice {

    @ExceptionHandler(RatingCalculationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String ratingCalculationException(final RatingCalculationException throwable, final Model model) {
        log.error("Exception during execution of application", throwable);
        model.addAttribute("errorMessage", throwable.getMessage());
        return "error";
    }

    @ExceptionHandler(TourGamesGenerationException.class)  //handle this exception
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String ratingCalculationException(final TourGamesGenerationException throwable, final Model model) {
        log.error("Exception during execution of application", throwable);
        model.addAttribute("errorMessage", throwable.getMessage());
        return "error";
    }

    @ExceptionHandler(EntityNotFoundException.class)  //handle this exception
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String ratingCalculationException(final EntityNotFoundException throwable, final Model model) {
        log.error("Exception during execution of application", throwable);
        model.addAttribute("errorMessage", throwable.getMessage());
        return "error";
    }

    @ExceptionHandler(EnumConstantNotPresentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String enumConstantNotPresentException(final EnumConstantNotPresentException throwable, final Model model) {
        log.error("Exception during execution of application", throwable);
        model.addAttribute("errorMessage", throwable.getMessage());
        return "error";
    }
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String enumConstantNotPresentException(final NullPointerException throwable, final Model model) {
        log.error("Exception during execution of application", throwable);
        model.addAttribute("errorMessage", throwable.getMessage());
        return "error";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String enumConstantNotPresentException(final DataIntegrityViolationException throwable, final Model model) {
        log.error("Exception during execution of application", throwable);
        model.addAttribute("errorMessage", throwable.getMessage());
        return "error";
    }
}
