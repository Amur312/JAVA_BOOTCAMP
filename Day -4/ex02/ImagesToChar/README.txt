
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

3. **Компиляция Java-файлов с внешними библиотеками**

    Скомпилируйте Java-файлы с использованием внешних библиотек (`JColor` и `jcommander`) в каталог `target`.

    ```
    javac -classpath ".:./lib/JColor-5.5.1.jar:./lib/jcommander-1.82.jar" -d target src/java/edu/school21/printer/app/ConsolePrinterApp.java src/java/edu/school21/printer/logic/ImageProcessor.java
    ```

4. **Копирование ресурсов и манифеста в рабочий каталог**

    Скопируйте ресурсы и манифест в каталог `target`.

    ```
    cp -r src/resources target/.
    cp src/manifest.txt target/.
    ```

5. **Извлечение файлов библиотек и перемещение их в рабочий каталог**

    Извлеките файлы библиотек (`JColor` и `jcommander`) и переместите их в каталог `target`.

    ```
    jar xf lib/JColor-5.5.1.jar com
    jar xf lib/jcommander-1.82.jar com
    mv com target/com
    ```

6. **Сборка исполняемого JAR**

    Соберите исполняемый JAR, используя манифестный файл.

    ```
    jar cmf target/manifest.txt target/ConsolePrinterApp.jar -C target edu -C target com -C target resources
    ```

7. **Запуск программы**

    Запустите скомпилированное приложение с необходимыми аргументами командной строки. Измените значения для `--white` и `--black` в соответствии с вашими предпочтениями.

    ```
    java -jar target/ConsolePrinterApp.jar --white=red --black=green
    ```

## Примечания


- Измените аргументы командной строки (`--white` и `--black`) в соответствии с вашими предпочтениями.
- https://repo1.maven.org/maven2/com/beust/jcommander/1.82/jcommander-1.82.jar
- https://repo1.maven.org/maven2/com/diogonunes/JColor/5.5.1/JColor-5.5.1.jar