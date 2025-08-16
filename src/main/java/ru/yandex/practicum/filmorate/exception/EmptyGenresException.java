package ru.yandex.practicum.filmorate.exception;

public class EmptyGenresException extends NotFoundException {  // Наследуем от NotFoundException
  public EmptyGenresException(String message) {
    super(message);
  }
}
