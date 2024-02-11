package Processor;

import Annotations.HtmlForm;
import Annotations.HtmlInput;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Процессор аннотаций для генерации HTML-форм на основе аннотаций @HtmlForm и @HtmlInput.
 */
// Регистрируем этот процессор в автоматическом режиме с помощью AutoService
@AutoService(Processor.class)
// Указываем тип аннотации, с которой этот процессор будет работать
@SupportedAnnotationTypes("Annotations.HtmlForm")
// Указываем версию Java, поддерживаемую этим процессором
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class HtmlProcessor extends AbstractProcessor {

    private static final Logger logger = LogManager.getLogger(HtmlProcessor.class);
    private static final Path OUTPUT_DIR = Paths.get("target/classes/");

    /**
     * Обрабатывает все элементы, помеченные аннотацией @HtmlForm.
     *
     * @param annotations множество типов аннотаций, переданных этому процессору
     * @param roundEnv    информация о раунде обработки, включая элементы, помеченные аннотацией @HtmlForm
     * @return true, если все аннотации были обработаны успешно
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // Обходим все элементы, помеченные аннотацией @HtmlForm
        // getElementsAnnotatedWith(HtmlForm.class) - этот метод возвращает множество элементов (Set), помеченных аннотацией @HtmlForm.
        for (Element element : roundEnv.getElementsAnnotatedWith(HtmlForm.class)) {
            try {
                // Генерируем содержимое формы и записываем его в файл
                List<String> content = generateFormContent(element, roundEnv);
                writeToFile(element, content);
            } catch (Exception e) {
                logger.error("Error processing " + element, e);
            }
        }

        return true;
    }

    /**
     * Генерирует содержимое HTML-формы на основе элемента и окружения обработки.
     *
     * @param element элемент, помеченный аннотацией @HtmlForm
     * @param env     окружение обработки, содержащее информацию о раунде
     * @return список строк, представляющих содержимое HTML-формы
     */
    private List<String> generateFormContent(Element element, RoundEnvironment env) {
        List<String> content = new ArrayList<>();
        // Получаем аннотацию @HtmlForm из элемента
        HtmlForm form = element.getAnnotation(HtmlForm.class);
        // Создаем тег <form> на основе параметров аннотации
        content.add(createFormTag(form));
        // Обходим все элементы, помеченные аннотацией @HtmlInput
        for (Element field : env.getElementsAnnotatedWith(HtmlInput.class)) {
            // Проверяем, содержится ли текущее поле внутри класса с аннотацией @HtmlForm
            if (!element.getEnclosedElements().contains(field)) continue;
            // Получаем аннотацию @HtmlInput из поля и создаем соответствующий тег <input>
            HtmlInput input = field.getAnnotation(HtmlInput.class);
            content.add(createInputTag(input));
        }
        // Добавляем кнопку отправки формы
        content.add(createSubmitButton());
        // Закрываем тег <form>
        content.add("</form>");

        return content;
    }

    /**
     * Создает тег <form> на основе аннотации @HtmlForm.
     *
     * @param form аннотация @HtmlForm
     * @return строка, представляющая открывающий тег формы
     */
    private String createFormTag(HtmlForm form) {
        return "<form action=\"" + form.action() + "\" method=\"" + form.method() + "\">";
    }

    /**
     * Создает тег <input> на основе аннотации @HtmlInput.
     *
     * @param input аннотация @HtmlInput
     * @return строка, представляющая тег ввода
     */
    private String createInputTag(HtmlInput input) {
        return "<input type=\"" + input.type() + "\" name=\"" + input.name() + "\" placeholder=\"" + input.placeholder() + "\">";
    }

    /**
     * Создает тег <input> для кнопки отправки формы.
     *
     * @return строка, представляющая тег кнопки отправки формы
     */
    private String createSubmitButton() {
        return "<input type=\"submit\" value=\"Submit\">";
    }

    /**
     * Записывает содержимое HTML-формы в файл.
     *
     * @param element элемент, помеченный аннотацией @HtmlForm
     * @param content содержимое HTML-формы
     * @throws IOException если возникла ошибка при записи в файл
     */
    private void writeToFile(Element element, List<String> content) throws IOException {

        HtmlForm form = element.getAnnotation(HtmlForm.class);
        // Формируем путь к файлу на основе имени файла из аннотации
        Path file = OUTPUT_DIR.resolve(validateFileName(form.fileName()));
        // Записываем содержимое в файл с помощью BufferedWriter
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (String line : content) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Проверяет корректность имени файла.
     *
     * @param fileName имя файла
     * @return проверенное имя файла
     * @throws IllegalArgumentException если имя файла некорректно
     */
    private String validateFileName(String fileName) {
        // Проверяем, что имя файла не пустое
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be empty");
        }
        // Проверяем, что имя файла не содержит недопустимых символов или символов пути
        Path path = Paths.get(fileName);
        if (path.getFileName().toString().contains("..")
                || path.getFileName().toString().contains("/")
                || path.getFileName().toString().contains("\\")) {
            throw new IllegalArgumentException("Invalid file name: " + fileName);
        }

        if (Files.exists(OUTPUT_DIR.resolve(path)) && !Files.isWritable(OUTPUT_DIR.resolve(path))) {
            throw new IllegalArgumentException("Cannot write to file: " + fileName);
        }
        // Проверяем, что файл доступен для записи
        return fileName;
    }


}
