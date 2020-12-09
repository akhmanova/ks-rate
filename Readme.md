Для запуска в консоли прописываем команду:

`java -jar ks-rate-1.0-SNAPSHOT.jar csvPath <Path_to_CSVFile>`

Для запуска через Intellij Idea указать параметры:
`csvPath <Path_to_CSVFile>`
в конфигурации запуска в поле Program arguments

Описание доступных параметров:
1. `csvPath <path>` - Путь к файлу локальной базы (CSV файл);
1. `gcsConfig <path>` - (По умолчанию = `./gcs_config.properties`) 
   Путь к конфигурационному файлу Google Cloud Storage модуля.
   Если файл не будет найден, то модуль будет отключен.
   Пример файла [здесь](gcs_config.properties).

Пути прописываются с разделителем `/`. 
Пример пути: `resources/data/ks-projects-201801.csv`