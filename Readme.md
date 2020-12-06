Для запуска в консоли прописываем команду:

`java -jar ks-rate-1.0-SNAPSHOT.jar csvPath <Path_to_CSVFile> --noGcs`

Для запуска через Intellij Idea указать параметры:
`csvPath <Path_to_CSVFile> --noGcs`
в конфигурации запуска в поле Program arguments

Описание доступных параметров:
1. `csvPath <path>` - Путь к файлу локальной базы (CSV файл);
1. `gcsAuth <path>` - Путь к файлу Json с данными аутентификации 
   к сервисам Google Cloud Storage 
1. `--noGcs` - параметр для отключения модуля обращения 
   к сервисам Google Cloud Storage.
   Необходим в случае, если на текущий момент 
   нет необходимости в сервисах Google Cloud Storage.

Пути прописываются с разделителем `/`. 
Пример пути: `resources/data/ks-projects-201801.csv`