CREATE TABLE LanguageCodes (
   LangID      char(3) NOT NULL,       
   CountryID   char(2) NOT NULL,       
   LangStatus  char(1) NOT NULL,                                            
   Name        varchar(75) NOT NULL)   
 CREATE TABLE CountryCodes (
   CountryID   char(2) NOT NULL,        
   Name        varchar(75) NOT NULL )   
 CREATE TABLE LanguageIndex (
   LangID      char(3) NOT NULL,       
   CountryID   char(2) NOT NULL,       
   NameType    char(2) NOT NULL,                                               
   Name        varchar(75) NOT NULL )  
 CREATE TABLE ChangeHistory (
   LangID      char(3) NOT NULL,       
   Date        smalldatetime NOT NULL, 
   Action      char(1) NOT NULL,      
                                       
   Description varchar(200) NOT NULL ) 