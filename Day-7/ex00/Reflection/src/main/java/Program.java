import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Program {
    static final private String BUILDER_CLASS = "classes";
    static final private String SEPARATOR = "---------------------";
    static final private String ERROR_MSG_CLASS_LIST = "Couldn't create list of available classes";
    static final private String ERROR_MSG_INIT_OBJ = "Couldn't initialize object";
    static final private String MSG_ENTER_CLASS_NAME = "Enter class name:\n-> ";
    static final private String MSG_ERROR_INVALID_CLASS = "Error: invalid class name";
    static final private String MSG_ENTER_FIELD_NAME = "Enter name of the field for changing:\n-> ";
    static final private String MSG_ENTER_FIELD_VALUE = "Enter %s value:\n-> ";
    static final private String MSG_ENTER_METHOD_NAME = "Enter name of the method for call:\n-> ";
    static final private String MSG_ERROR_INVALID_FIELD = "Invalid field name. Try again";
    static final private String MSG_ERROR_INVALID_METHOD = "Invalid method name. Try again";
    static final private String MSG_METHOD_RETURNED = "Method returned:\n";

    static private Map<String, Class<?>> classes;
    // Теория https://java-course.ru/begin/reflection/
    // https://nuancesprog.ru/p/15245/
    // https://www.baeldung.com/java-reflection

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Program program = new Program();
            program.initializeClasses();
            program.execute(scanner);
        } catch (Exception e) {
            terminateProgram(e.getMessage(), -1);
        }
    }

    /**
     * Выполняет основной процесс программы, включающий в себя:
     * - вывод доступных классов;
     * - получение информации о выбранном классе;
     * - инициализацию объекта выбранного класса;
     * - обновление значений полей объекта;
     * - вызов методов объекта.
     *
     * @param scanner объект Scanner для ввода данных пользователем
     */
    private void execute(Scanner scanner) {
        printAvailableClasses();
        Class<?> cls = printClassInfo(scanner);
        Object obj = initializeObject(scanner, cls);
        updateFieldValue(scanner, obj, cls);
        invokeObjectMethod(scanner, obj, cls);
    }

    /**
     * Инициализирует доступные классы, загружая их из пакета.
     * Если список классов не удалось загрузить, программа завершается с указанным сообщением об ошибке.
     */
    private void initializeClasses() {
        classes = loadClassesFromPackage();
        if (classes == null) {
            terminateProgram(ERROR_MSG_CLASS_LIST, -1);
        }
    }

    /**
     * Загружает классы из указанного пакета.
     * Если не удалось загрузить список классов, возвращает null.
     *
     * @return Map, содержащая имена классов в качестве ключей и соответствующие классы в качестве значений.
     */
    private static Map<String, Class<?>> loadClassesFromPackage() {
        // Получаем загрузчик классов системы из classpath
        // JVM использует метод ClassLoader.loadClass() для загрузки класса в память
        // https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/ClassLoader.html
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        // getResource() - метод ClassLoader для поиска ресурса по имени
        // P.S Вся магия в том, что мы ищем классы динамически во время выполнения рантайма
        URL resourceURL = classLoader.getResource(BUILDER_CLASS);
        if (resourceURL == null) {
            return null;
        }
        // Извлекаем путь к пакету из URL-адреса resourceURL
        String packagePath = resourceURL.getPath();
        if (packagePath.isEmpty()) {
            return null;
        }
        // Маппа для хранения имен классов и соответствующих им классов
        Map<String, Class<?>> classes = new HashMap<>();
        // Получаем список файлов в нашем packagePath
        // Зачем это нужно? Чтобы можно было перебрать все файлы в пакете
        // и например, проверить соответствие шаблону имени, прочитать содержимое и т.д.
        File[] files = new File(packagePath).listFiles();
        if (files == null) {
            return null;
        }
        // Перебираем все файлы в пакете
        for (File file : files) {
            // Получаем имя файла из file
            // После этого в fileName будет строка с именем файла, например - "MyClass.class"
            String fileName = file.getName();
            // Проверяем, что имя заканчивается на ".class" - то есть это файл класса Java.
            if (!fileName.endsWith(".class")) {
                continue;
            }
            try {
                // Обрезаем ".class" из имени файла
                String className = fileName.substring(0, fileName.length() - 6);
                // Добавляем класс в маппу
                classes.put(className, Class.forName(BUILDER_CLASS + "." + className));
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
        return classes;
    }

    /**
     * Выводит доступные классы.
     * Если список классов не пустой, выводит их имена.
     * В противном случае, завершает программу с сообщением об ошибке.
     */
    private void printAvailableClasses() {
        System.out.println("Classes:");
        if (classes != null) {
            // Берем все имена классов и принтуем их
            classes.keySet().forEach(key -> System.out.println("  - " + key));
            System.out.println(SEPARATOR);
        } else {
            terminateProgram(ERROR_MSG_CLASS_LIST, -1);
        }
    }

    /**
     * Вводит имя класса с клавиатуры и отображает информацию о классе.
     *
     * @param scanner объект Scanner для чтения пользовательского ввода
     * @return объект Class, представляющий введенный класс, или null, если класс не найден
     */
    private Class<?> printClassInfo(Scanner scanner) {
        System.out.print(MSG_ENTER_CLASS_NAME);
        while (true) {
            String className = scanner.nextLine();
            // Получаем класс из мапы классов по имени
            Class<?> cls = classes.get(className);
            if (cls == null) {
                System.out.println(MSG_ERROR_INVALID_CLASS);
                continue;
            }

            displayClassDetails(cls);

            return cls;
        }
    }

    /**
     * Отображает информацию о полях и методах класса.
     *
     * @param cls объект Class, информацию о котором нужно отобразить
     */
    private void displayClassDetails(Class<?> cls) {
        System.out.println("fields:");
        // Используя рефлексию выводим инфу о полях класса cls
        // cls.getDeclaredFields() - возвращает массив полей данного класса
        for (Field field : cls.getDeclaredFields()) {
            // field - это одно поле класса
            // field.getType() - возвращает тип поля field
            // getSimpleName() - извлекаем простое имя класса типа, например "String".
            //field.getName() - получаем имя самого поля.
            System.out.println("\t" + field.getType().getSimpleName() + " " + field.getName());
        }
        System.out.println("methods:");
        // Получаем все методы класса через cls.getDeclaredMethods()
        for (Method method : cls.getDeclaredMethods()) {
            // Создаем строку methodString для формирования информации о методе.
            StringBuilder methodString = new StringBuilder("\t" + method.getReturnType().getSimpleName() + " " + method.getName() + "(");
            // Получаем количество параметров метода
            int paramsCount = method.getParameterCount();
            // Циклом перебираем все параметры метода
            for (int i = 0; i < paramsCount; i++) {
                // Берем тип каждого параметра через метод getParameterTypes()
                // Добавляем простое имя типа в строку methodString
                // Добавляем разделитель ", " если нужно
                methodString.append(method.getParameterTypes()[i].getSimpleName());
                if (i + 1 != paramsCount) methodString.append(", ");
            }
            // Добавляем закрывающую скобку ")"
            methodString.append(")");
            // Выводим получившуюся строку с информацией о методе
            System.out.println(methodString);
        }

        System.out.println(SEPARATOR);
    }

    /**
     * Создает экземпляр объекта класса и инициализирует его поля с помощью введенных пользователем значений.
     *
     * @param scanner объект Scanner для чтения пользовательского ввода
     * @param cls     объект Class, представляющий класс объекта, который нужно создать
     * @return новый объект, созданный на основе переданного класса, или null, если не удалось создать объект
     */
    private Object initializeObject(Scanner scanner, Class<?> cls) {
        System.out.println("Let’s create an object.");
        Object obj = null;
        try {
            // Создаем новый объект класса cls через newInstance()
            obj = cls.newInstance();
            // Получаем массив всех полей класса
            Field[] fields = cls.getDeclaredFields();
            // Перебираем каждое поле
            for (Field field : fields) {
                // Делаем поле доступным для изменения (в случае приватного доступа)
                // https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/AccessibleObject.html
                field.setAccessible(true);
                System.out.print(field.getName() + ":\n-> ");
                // Парсим введенное пользователем значение в нужный тип
                Object value = parseUserInput(scanner, field.getType().getSimpleName());
                // Устанавливаем значение поля в obj
                field.set(obj, value);
            }
            // Выводим информацию о созданном объекте
            displayObjectDetails(obj, fields, cls.getSimpleName());
        } catch (InstantiationException | IllegalAccessException e) {
            terminateProgram(ERROR_MSG_INIT_OBJ, -1);
        }
        return obj;
    }

    /**
     * Обновляет значение поля объекта на основе введенных пользователем данных.
     * Если введенное имя поля неверно или если доступ к полю невозможен, запрашивает ввод повторно.
     *
     * @param scanner объект Scanner для чтения пользовательского ввода
     * @param obj     объект, у которого нужно обновить значение поля
     * @param cls     класс объекта, содержащий поле, которое нужно обновить
     */
    private void updateFieldValue(Scanner scanner, Object obj, Class<?> cls) {
        System.out.print(MSG_ENTER_FIELD_NAME);
        // Получаем имя поля из пользовательского ввода
        String fieldName = scanner.nextLine();
        Field field = null;
        try {
            // Получаем объект Field, представляющий указанное имя поля в классе cls
            field = cls.getDeclaredField(fieldName);
            // Делаем поле доступным для изменения (в случае приватного доступа)
            field.setAccessible(true);
            System.out.printf(MSG_ENTER_FIELD_VALUE, field.getType().getSimpleName());
            // Получаем значение для поля из пользовательского ввода
            Object value = parseUserInput(scanner, field.getType().getSimpleName());
            // Устанавливаем новое значение для поля объекта obj
            field.set(obj, value);
            // Отображаем информацию о объекте после обновления значения поля
            displayObjectDetails(obj, cls.getDeclaredFields(), cls.getSimpleName());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println(MSG_ERROR_INVALID_FIELD);
            // Рекурсивно вызываем метод updateFieldValue для повторного ввода имени поля
            updateFieldValue(scanner, obj, cls);
        } finally {
            // Убеждаемся, что доступ к полю закрыт, если он был открыт
            if (field != null) {
                field.setAccessible(false);
            }
        }
    }

    /**
     * Вызывает метод объекта на основе введенного пользователем имени метода и отображает результат его выполнения.
     * Если введенное имя метода неверно или если метод недоступен для вызова, запрашивает ввод повторно.
     *
     * @param scanner объект Scanner для чтения пользовательского ввода
     * @param obj     объект, у которого нужно вызвать метод
     * @param cls     класс объекта, содержащий метод, который нужно вызвать
     */
    private void invokeObjectMethod(Scanner scanner, Object obj, Class<?> cls) {
        System.out.print(MSG_ENTER_METHOD_NAME);
        String methodName = scanner.nextLine();
        Method method = null;
        try {
            // Получаем объект Method, представляющий указанный метод в классе cls
            method = cls.getDeclaredMethod(methodName);
            // Вызываем метод объекта obj и сохраняем результат выполнения
            Object result = method.invoke(obj);
            // Если результат не равен null, печатаем сообщение о возвращенном значении метода
            if (result != null) {
                System.out.println(MSG_METHOD_RETURNED + result);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.out.println(MSG_ERROR_INVALID_METHOD);
            // Рекурсивно вызываем метод invokeObjectMethod для повторного ввода имени метода
            invokeObjectMethod(scanner, obj, cls);
        } finally {
            // Убеждаемся, что доступ к методу закрыт, если он был открыт
            if (method != null) {
                method.setAccessible(false);
            }
        }
    }


    /**
     * Читает ввод пользователя из Scanner и преобразует его в соответствующий тип данных, основываясь на переданном имени типа.
     *
     * @param scanner  объект Scanner для чтения пользовательского ввода
     * @param typeName строковое представление типа данных, которое нужно прочитать из ввода
     * @return объект, представляющий прочитанные данные, соответствующий типу данных typeName
     * @throws IllegalArgumentException если typeName не поддерживается (не является одним из поддерживаемых типов)
     */
    private Object parseUserInput(Scanner scanner, String typeName) {
        switch (typeName) {
            case "String":
                return scanner.nextLine();
            case "Integer":
            case "int":
                return scanner.nextInt();
            case "Long":
            case "long":
                return scanner.nextLong();
            case "Double":
            case "double":
                return scanner.nextDouble();
            case "Boolean":
            case "boolean":
                return scanner.nextBoolean();
            default:
                // Если тип не поддерживается, выбрасываем исключение IllegalArgumentException
                throw new IllegalArgumentException("Unsupported type: " + typeName);
        }
    }

    /**
     * Выводит информацию о полях объекта, включая их значения.
     *
     * @param obj       объект, информацию о полях которого нужно вывести
     * @param fields    массив полей объекта
     * @param className имя класса объекта
     * @throws IllegalAccessException если доступ к полям объекта невозможен
     */
    private void displayObjectDetails(Object obj, Field[] fields, String className) throws IllegalAccessException {
        System.out.print("Object created: " + className + "[");
        // Перебираем все поля объекта
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            // Делаем поле доступным для чтения
            field.setAccessible(true);
            System.out.print(field.getName() + "=" + field.get(obj));
            // Если это не последнее поле, добавляем разделитель ", ", иначе выводим закрывающую скобку и перевод строки
            if (i + 1 != fields.length) System.out.print(", ");
            else System.out.println("]");
        }
        // Выводим разделитель
        System.out.println(SEPARATOR);
    }


    /**
     * Завершает выполнение программы с заданным сообщением и кодом выхода.
     *
     * @param msg      сообщение, которое будет выведено перед завершением программы
     * @param exitCode код выхода программы
     */
    private static void terminateProgram(String msg, int exitCode) {
        // Проверяем код выхода
        if (exitCode == 0) {
            System.out.println(msg);
        } else {
            System.err.println(msg);
        }
        System.exit(exitCode);
    }
}
