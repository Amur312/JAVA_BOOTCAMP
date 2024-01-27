## Использование

1. **Удаление существующего каталога**

    Перед построением приложения удалите существующий каталог `target`.

    ```
    rm -rf target
    ```

2. **Создание рабочего каталога**

    Создайте новый каталог `target` для хранения скомпилированных файлов и ресурсов.

    ```
    mkdir target
    ```

3. **Компиляция Java-файлов**

    Скомпилируйте Java-файлы в каталог `target`.

    ```
    javac -d target src/java/edu/school21/printer/app/ConsolePrinterApp.java src/java/edu/school21/printer/logic/ImageProcessor.java
    ```

4. **Копирование ресурсов и манифеста в рабочий каталог**

    Скопируйте ресурсы и манифест в каталог `target`.

    ```
    cp -r src/resources target/.
    cp src/manifest.txt target/.
    ```

5. **Сборка исполняемого JAR**

    Соберите исполняемый JAR, используя манифестный файл.

    ```
    jar cmf target/manifest.txt target/ConsolePrinterApp.jar -C target edu -C target resources
    ```

6. **Запуск программы**

    Запустите скомпилированное приложение с необходимыми аргументами командной строки. Измените значения для `--black` и `--white` в соответствии с вашими предпочтениями.

    ```
    java -jar target/ConsolePrinterApp.jar --black=. --white=0
    ```

## Примечания

- Измените аргументы командной строки (`--black` и `--white`) в соответствии с вашими предпочтениями.
