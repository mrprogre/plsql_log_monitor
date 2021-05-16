# plsql_log_monitor

I made the application for myself, because I didn’t like to constantly switch the tab in PLSQL developer to see the log, and also so that new records were added as a cumulative total, and not overwritten with the current data of the interval in the select. It is very convenient to develop new functionality and catch every 2 seconds the values of variables or errors that are written to the log table (especially convenient when you use two monitors). You can also open the application 3 times to simultaneously monitor all development environments.

In the process of work, I added a lot of new functionality:
1) connection to 3 development environments: dev, test, prod (fast switching between environments using the "Connect" button)
2) automatic update of the select for the specified interval with the specified condition (cumulative total)
3) added buttons: export table to Excel, stop search, clear list
4) added context menu when right-clicking: copy, delete selected lines
5) added tooltip to the message text, because I don't like line wrapping visually
6) added combobox with a list of all tables of the current user in the current development environment (columns, their type, unique values in columns at the moment (calculated when you click on the "Go" button in Java, and not taken from Oracle statistics))
7) added the ability to add / remove a table from the list of favorite tables (the star icon turns into a green checkmark for this table), the list of which is displayed when you click the "Favorite tables" checkbox.
8) a general list of selected tables can be viewed by clicking on the black square (each environment has its own selected tables)
9) a log of the program is kept. You can view it by clicking on the brown square.
10) created 8 interface themes, including DOS theme and contrast. Also, any background color of the application can be selected by the button with the palette
11) in the lower right table, the specified table is selected in the combo box, if you press the yellow "Select" button. A hint has been created that suggests that the first 18 records are displayed. To display all records, you need to set the caret in the field where the hint is, so that it disappears and press the yellow "Select" or the Enter key
12) In the table in clause 11, it is possible to download blob, clob files, moreover, a logic has been created that determines the main types of files so that the user himself does not guess it after saving (some types are shown in the table in the screenshot). If blob, clob is empty, then there is a "-" sign.
13) there is a calculation of selectivity

p.s. I can not write the GIF file of the application operation, tk. the names of tables and procedures in the log are confidential

![Image alt](https://github.com/mrprogre/plsql_log_monitor/blob/master/GUI.png) 
