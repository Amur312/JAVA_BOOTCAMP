package app;

import annotation.OrmColumn;
import annotation.OrmColumnId;
import annotation.OrmEntity;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class OrmManager {
    private Map<String, Class<?>> modelClasses;
    private HikariDataSource dataSource;

    public OrmManager() {
        modelClasses = loadClasses();
        System.out.println("List of classes: " + modelClasses);
        if (modelClasses == null || modelClasses.isEmpty()) {
            throw new RuntimeException("No model classes found");
        }
        dataSource = createDataSource();
        if (dataSource == null) {
            throw new RuntimeException("Unable to create data source");
        }

    }

    /**
     * Создает таблицы в базе данных на основе классов моделей, аннотированных как @OrmEntity.
     * Для каждого класса модели из мапы {@code modelClasses} извлекается соответствующая таблица.
     * Если класс модели не аннотирован как @OrmEntity, процесс пропускается.
     * Для каждой найденной модели формируется запрос на создание таблицы и отправляется на выполнение.
     *
     * @throws RuntimeException если произошла ошибка при выполнении запроса или загрузке классов моделей
     */
    public void createTables() {
        for (String className : modelClasses.keySet()) {
            Class<?> cls = modelClasses.get(className);
            if (!cls.isAnnotationPresent(OrmEntity.class)) continue;
            String tableName = cls.getAnnotation(OrmEntity.class).table();
            String createTableQuery = generateCreateTableQuery(cls, tableName);
            updateQuery(createTableQuery);
        }
    }


    /**
     * Сохраняет объект в базе данных.
     * Для этого извлекает класс объекта и проверяет, аннотирован ли он как @OrmEntity.
     * Если класс не аннотирован, метод завершает свою работу.
     * Затем извлекает список столбцов и их значения из объекта.
     * Формирует запрос на вставку данных в таблицу и отправляет его на выполнение.
     *
     * @param entity объект, который необходимо сохранить в базе данных
     * @throws RuntimeException если произошла ошибка при выполнении запроса,
     *                          загрузке класса объекта или доступе к его полям
     */
    public void save(Object entity) {
        Class<?> entityClass = entity.getClass();
        if (!entityClass.isAnnotationPresent(OrmEntity.class)) {
            return;
        }

        OrmEntity ormEntityAnnotation = entityClass.getAnnotation(OrmEntity.class);
        final String tableName = ormEntityAnnotation.table();

        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        try {
            extractColumnsAndValues(entity, columns, values);
            String insertQuery = generateInsertQuery(tableName, columns, values);
            updateQuery(insertQuery);
        } catch (IllegalAccessException | NullPointerException e) {
            throw new RuntimeException("Error saving entity: " + e.getMessage(), e);
        }
    }

    /**
     * Обновляет объект в базе данных.
     * Для этого извлекает класс объекта и проверяет, аннотирован ли он как @OrmEntity.
     * Если класс не аннотирован, метод завершает свою работу.
     * Затем извлекает список столбцов и их значения из объекта.
     * Формирует запрос на обновление данных в таблице и отправляет его на выполнение.
     *
     * @param entity объект, который необходимо обновить в базе данных
     * @throws RuntimeException если произошла ошибка при выполнении запроса,
     *                          загрузке класса объекта или доступе к его полям
     */
    public void update(Object entity) {
        Class<?> entityClass = entity.getClass();
        if (!entityClass.isAnnotationPresent(OrmEntity.class)) {
            return;
        }

        OrmEntity ormEntityAnnotation = entityClass.getAnnotation(OrmEntity.class);
        final String tableName = ormEntityAnnotation.table();

        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        long id = -1;
        try {
            extractColumnsAndValues(entity, columns, values);
            id = extractId(entity);
            if (id == -1) {
                return;
            }
            String updateQuery = generateUpdateQuery(tableName, id, columns, values);
            updateQuery(updateQuery);
        } catch (IllegalAccessException | NullPointerException e) {
            throw new RuntimeException("Error updating entity: " + e.getMessage(), e);
        }
    }

    /**
     * Извлекает список столбцов и их значений из объекта.
     *
     * @param entity  объект, из которого извлекаются столбцы и значения
     * @param columns список столбцов
     * @param values  список значений
     * @throws IllegalAccessException если произошла ошибка доступа к полям объекта
     */
    private void extractColumnsAndValues(Object entity, List<String> columns, List<Object> values) throws IllegalAccessException {
        Arrays.stream(entity.getClass().getDeclaredFields())
                .forEach(field -> {
                    field.setAccessible(true);
                    try (AutoCloseable ignored = () -> field.setAccessible(false)) {
                        if (field.isAnnotationPresent(OrmColumnId.class)) {
                            columns.add("id");
                            values.add(field.get(entity));
                        } else if (field.isAnnotationPresent(OrmColumn.class)) {
                            OrmColumn ormColumnAnnotation = field.getAnnotation(OrmColumn.class);
                            columns.add(ormColumnAnnotation.name());
                            values.add(field.get(entity));
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * Извлекает идентификатор объекта.
     *
     * @param entity объект, из которого извлекается идентификатор
     * @return идентификатор объекта
     * @throws IllegalAccessException если произошла ошибка доступа к полю объекта
     */
    private long extractId(Object entity) throws IllegalAccessException {
        for (Field field : entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try (AutoCloseable ignored = () -> field.setAccessible(false)) {
                if (field.isAnnotationPresent(OrmColumnId.class)) {
                    return (long) field.get(entity);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return -1;
    }

    /**
     * Ищет запись в базе данных по заданному идентификатору и возвращает объект, соответствующий указанному классу.
     * Если запись не найдена или произошла ошибка во время выполнения запроса, возвращает null.
     *
     * @param id     идентификатор записи, которую необходимо найти
     * @param aClass класс объекта, который необходимо создать на основе найденной записи
     * @param <T>    тип объекта
     * @return объект, соответствующий найденной записи в базе данных, или null, если запись не найдена
     */
    public <T> T findById(Long id, Class<T> aClass) {
        if (!aClass.isAnnotationPresent(OrmEntity.class)) return null;

        String tableName = aClass.getAnnotation(OrmEntity.class).table();
        String query = "SELECT * FROM " + tableName + " WHERE id=?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) return null;
                return mapResultSetToObject(resultSet, aClass);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Создает объект указанного класса на основе данных из ResultSet.
     *
     * @param resultSet результат выполнения SQL-запроса
     * @param aClass    класс объекта, который необходимо создать
     * @param <T>       тип объекта
     * @return новый объект указанного класса, содержащий данные из ResultSet
     * @throws SQLException если произошла ошибка при работе с ResultSet
     */
    private <T> T mapResultSetToObject(ResultSet resultSet, Class<T> aClass) throws SQLException {
        try {
            T newInstance = aClass.getDeclaredConstructor().newInstance();
            for (Field field : aClass.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(OrmColumnId.class)) {
                    field.set(newInstance, resultSet.getLong("id"));
                } else if (field.isAnnotationPresent(OrmColumn.class)) {
                    OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);
                    field.set(newInstance, resultSet.getObject(ormColumn.name()));
                }
                field.setAccessible(false);
            }
            return newInstance;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException("Error mapping ResultSet to object: " + e.getMessage(), e);
        }
    }


    /**
     * Загружает классы из указанного пакета и возвращает их в виде отображения имени класса и соответствующего класса.
     *
     * @return отображение имени класса и соответствующего класса, загруженных из указанного пакета, или null в случае ошибки
     */
    private Map<String, Class<?>> loadClasses() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL packageUrl = classLoader.getResource("model");
        if (packageUrl == null) return null;

        String packagePath = packageUrl.getPath();
        if (packagePath.length() == 0) return null;

        Map<String, Class<?>> classes = new HashMap<>();
        File[] files = new File(packagePath).listFiles();
        if (files == null) return null;
        for (File file : files) {
            String fileName = file.getName();
            if (!fileName.endsWith(".class")) continue;
            try {
                String className = fileName.substring(0, fileName.length() - 6);
                classes.put(
                        className,
                        Class.forName("model" + "." + className)
                );
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        return classes;
    }

    private HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        config.setUsername("postgres");
        config.setPassword("postgres");
        config.setValidationTimeout(300_000);

        return new HikariDataSource(config);
    }

    /**
     * Генерирует SQL-запрос для создания таблицы на основе класса модели.
     *
     * @param cls       класс модели
     * @param tableName имя таблицы
     * @return SQL-запрос для создания таблицы
     */
    public static String generateCreateTableQuery(Class<?> cls, String tableName) {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE IF EXISTS ")
                .append(tableName)
                .append("; ")
                .append("CREATE TABLE IF NOT EXISTS ")
                .append(tableName)
                .append("(");

        boolean isFirst = true;
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(OrmColumnId.class)) {
                if (!isFirst) query.append(",");
                query.append("id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY");
                isFirst = false;
            } else if (field.isAnnotationPresent(OrmColumn.class)) {
                if (!isFirst) query.append(",");
                OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);
                String columnName = ormColumn.name();
                String columnType = getSqlType(field.getType().getSimpleName());
                query.append(columnName)
                        .append(" ")
                        .append(columnType);
                isFirst = false;
            }
        }

        query.append(");");
        return query.toString();
    }

    /**
     * Определяет SQL-тип данных на основе типа Java.
     *
     * @param javaType тип Java
     * @return SQL-тип данных
     */
    private static String getSqlType(String javaType) {
        switch (javaType) {
            case "String":
                return "VARCHAR(255)";
            case "Integer":
                return "INT";
            case "Long":
                return "BIGINT";
            case "Double":
                return "DOUBLE";
            case "Boolean":
                return "BOOLEAN";
            default:
                throw new IllegalArgumentException("Unsupported Java type: " + javaType);
        }
    }

    /**
     * Генерирует SQL-запрос для вставки данных в таблицу.
     *
     * @param tableName имя таблицы
     * @param columns   список столбцов
     * @param values    список значений
     * @return SQL-запрос для вставки данных
     */
    private String generateInsertQuery(String tableName, List<String> columns, List<Object> values) {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ")
                .append(tableName)
                .append("(");
        for (int i = 0; i < columns.size(); i++) {
            query.append(columns.get(i));
            if (i + 1 != columns.size()) query.append(",");
            else query.append(")");
        }
        query.append(" VALUES (");
        for (int i = 0; i < values.size(); i++) {
            Object obj = values.get(i);
            if (obj.getClass().getSimpleName().equals("String")) {
                query.append("'");
                query.append(obj);
                query.append("'");
            } else {
                query.append(obj);
            }
            if (i + 1 != columns.size()) query.append(",");
            else query.append(");");
        }
        return query.toString();
    }

    /**
     * Генерирует SQL-запрос для обновления данных в таблице.
     *
     * @param tableName имя таблицы
     * @param id        идентификатор объекта
     * @param columns   список столбцов
     * @param values    список значений
     * @return SQL-запрос для обновления данных
     */
    private String generateUpdateQuery(String tableName, long id, List<String> columns, List<Object> values) {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE ")
                .append(tableName)
                .append(" SET ");
        for (int i = 0; i < values.size(); i++) {
            String columnName = columns.get(i);
            Object newValue = values.get(i);

            query.append(columnName)
                    .append("=");
            if (newValue.getClass().getSimpleName().equals("String")) {
                query.append("'");
                query.append(newValue);
                query.append("'");
            } else {
                query.append(newValue);
            }
            if (i + 1 != columns.size()) query.append(",");
        }
        query.append(" WHERE id=")
                .append(id)
                .append(";");

        return query.toString();
    }

    /**
     * Выполняет обновление данных в базе данных на основе SQL-запроса.
     *
     * @param sql SQL-запрос для обновления данных
     */
    private void updateQuery(String sql) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            printQuery(sql);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Выводит на консоль сгенерированный SQL-запрос.
     *
     * @param sql SQL-запрос
     */
    /**
     * Выводит на консоль сгенерированный SQL-запрос.
     *
     * @param sql SQL-запрос для вывода
     */
    private void printQuery(String sql) {
        if (sql != null && !sql.isEmpty()) {
            System.out.println(" SQL query:");
            System.out.println(sql);
        } else {
            System.out.println("No SQL query provided.");
        }
    }


}
