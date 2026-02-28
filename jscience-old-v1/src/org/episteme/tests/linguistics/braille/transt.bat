@echo off
REM
REM This batch file calls the OutputTestTerminal class which displays to the
REM terminal the results of translation via Language256, LanguageInt and 
REM LanguageUnicode. Note that your terminal may not present the output
REM correctly (the output is in Unicode).
REM
java -classpath . org.episteme.linguistics.braille.tests.OutputTestTerminal %1 %2 %3