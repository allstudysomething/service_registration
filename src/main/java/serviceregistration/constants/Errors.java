package serviceregistration.constants;

public interface Errors {
//    class Book {
//        public static final String BOOK_DELETE_ERROR = "Книга не может быть удалена, так как у нее есть активные аренды";
//    }
//
//    class Authors {
//        public static final String AUTHOR_DELETE_ERROR = "Автор не может быть удален, так как у его книг есть активные аренды";
//    }
//
    class Users {
        public static final String USER_FORBIDDEN_ERROR = "У вас нет прав просматривать информацию о пользователе";
    }

    class REST {
        public static final String DELETE_ERROR_MESSAGE = "Удаление невозможно";
        public static final String AUTH_ERROR_MESSAGE = "Неавторизованный пользователь";
        public static final String ACCESS_ERROR_MESSAGE = "Отказано в доступе!";
        public static final String NOT_FOUND_ERROR_MESSAGE = "Объект не найден!";
    }
}
