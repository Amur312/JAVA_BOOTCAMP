package Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Теория  - https://topjava.ru/blog/rukovodstvo-po-annotatsiyam-v-java-i-mekhanizmu-ikh-raboty

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
// @interface в Java используется для определения пользовательских аннотаций.
// Это ключевое слово указывает компилятору, что определение аннотации следует за ним
public @interface HtmlForm {
    String fileName();

    String action();

    String method();
}
