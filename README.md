# Домашнее задание по теме «Работа с файлами CSV, XML, JSON»
## Эта задание является продолжением задания про сериализацию (тема «Потоки ввода-вывода. Работа с файлами. Сериализация»)
## Задача 1 (обязательная)
Создайте класс `ClientLog` для сохранения всех операций, которые ввёл пользователь. В конце работы программы сохраняйте журнал действий в файл `log.csv`.
Также вместо вызова метода saveTxt в методе main сериализуйте корзину в json-формате в файл basket.json.
Аналогично при старте программы загружайте корзину десериализацией из `json-а` из файла `basket.json`, а не из обычной текстовой сериализации как было до того. При этом логику сериализации в методах в классе корзины трогать не нужно.
## Задача 2 (обязательная)
При старте ваша программа должна считывать файл `shop.xml` и загружать из него настройки:
- блок `load` говорит нужно ли загружать данные корзины при старте программы из файла (`enabled`), указывает имя этого файла (`fileName`) и формат (`json` или `text`). Ваша программа должна вести себя соответствующим образом;
- блок `save` говорит нужно ли сохранять данные корзины после каждого ввода, куда и в каком формате (`text` или `json`);
- блок `log` говорит нужно ли сохранять лог при завершении программы и в какой файл; формат лога всегда `csv`.