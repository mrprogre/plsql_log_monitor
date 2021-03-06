# plsql_log_monitor

Приложение делал для себя, т.к. не нравилось постоянно переключать вкладку в PLSQL developer чтобы посмотреть лог, 
а также чтобы новые записи добавлялись накопительным итогом, а не затирались актуальным данными интервала в селекте. 
Очень удобно разрабатывать новый функционал и отлавливать каждые 2 секунды значения переменных или ошибки, которые пишутся в таблицу лога (особенно удобно когда пользуешься двумя мониторами). Также можно открыть 3 раза приложение, чтобы одновременно мониторить все среды разработки.

В процессе работы добавил много нового функционала:
1) подключение к 3 средам разработки: dev, test, prod (быстрое переключение между средами по кнопке "Connect")
2) автоматическое обновление селекта за указанный интервал с указанным условием (накопительным итогом)
3) добавлены кнопки: экспорт таблицы в эксель, остановить поиск, очистить список
4) добавлено контекстное меню при нажатии правой кнопкой мыши: копировать, удалить выделенные строки
5) добавлен tooltip на текст сообщения, т.к. перенос по строкам визуально мне не нравится
6) добавлен комбобокс со списком всех таблиц текущего пользователя в текущей среде разработки (столбцы, их тип, уникальные значения в столбцах на данный момент(считается при нажатии на кнопку "Go" в Java, а не берётся из статистики Oracle))
7) добавлена возможность добавления/удаления таблицы из списка избранных таблиц (значок звёздочки превращается в зелёную галочку для этой таблицы), список которых отображается при нажатии галочки "Favorite tables".
8) общий список избранных таблиц можно посмотреть при нажатии на чёрный квадрат (для каждой среды свои избранные таблицы)
9) ведётся лог работы программы. Посмотреть его можно нажатием на коричневый квадрат.
10) создано 8 тем интерфейса, включая тему DOS и контраст. Также любой цвет фона приложения можно выбрать по кнопке с палитрой
11) в нижней правой таблице производится селект указанной таблицы в комбобоксе, если нажать на жёлтую кнопку "Select". Создан хинт, подсказывающий что выводится первые 18 записей. Чтобы вывести все записи надо установить каретку в поле где хинт, чтобы он пропал и нажать желтый "Select" или клавишу Enter
12) В таблице п.11 имеется возможность скачивать blob, clob файлы, причём создна логика определяющая основные типы файлов, чтобы пользователь сам его не угадывал после сохранения (на скрине в таблице указаны некоторые типы). Если blob, clob пустой, то стоит знак "-".

p.s. записать гиф файл работы приложения не могу, т.к. названия таблиц и процедур в логе конфиденциальны.

![Image alt](https://github.com/mrprogre/plsql_log_monitor/blob/master/GUI.png) 

I made the application for myself, because I didn’t like to constantly switch the tab in PLSQL developer to see the log, and also so that new records were added as a cumulative total, and not overwritten with the current data of the interval in the select. It is very convenient to develop new functionality and catch every 2 seconds the values of variables or errors that are written to the log table (especially convenient when you use two monitors). You can also open the application 3 times to simultaneously monitor all development environments.

In the process of work, I added a lot of new functionality:
1) connection to 3 development environments: dev, test, prod (fast switching between environments using the "Connect" button)
2) automatic update of the select for the specified interval with the specified condition (cumulative total)
3) added buttons: export table to Excel, stop search, clear list
4) added context menu when right-clicking: copy, delete selected lines
5) added tooltip to the message text, because I don't like line wrapping visually
6) added combobox with a list of all tables of the current user in the current development environment (columns, their type, unique values ​​in columns at the moment (calculated when you click on the "Go" button in Java, and not taken from Oracle statistics))
7) added the ability to add / remove a table from the list of favorite tables (the star icon turns into a green checkmark for this table), the list of which is displayed when you click the "Favorite tables" checkbox.
8) a general list of selected tables can be viewed by clicking on the black square (each environment has its own selected tables)
9) a log of the program is kept. You can view it by clicking on the brown square.
10) created 8 interface themes, including DOS theme and contrast. Also, any background color of the application can be selected by the button with the palette
11) in the lower right table, the specified table is selected in the combo box, if you press the yellow "Select" button. A hint has been created that suggests that the first 18 records are displayed. To display all records, you need to set the caret in the field where the hint is, so that it disappears and press the yellow "Select" or the Enter key
12) In the table in clause 11, it is possible to download blob, clob files, moreover, a logic has been created that determines the main types of files so that the user himself does not guess it after saving (some types are shown in the table in the screenshot). If blob, clob is empty, then there is a "-" sign.

p.s. I can not write the GIF file of the application operation, tk. the names of tables and procedures in the log are confidential
