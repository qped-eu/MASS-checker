QPED - Feedback tools for Java
========================

## Checkers
Certain parts of this project are referred to as "checkers".
A checker is a module that can be configured individually and has a specific analytical function for the submitted code.
A "Syntax Checker" is for instance used to analyze the syntax of submitted code and generate feedback in that specific regard.
Below you may find a list of currently implemented checkers and a sort description of each one.

### Syntax Checker
To be added: Short description

### Style Checker
To be added: Short description

### Class Checker
To be added: Short description

### Test Checker
To be added: Short description

Configuration
------------
The configuration uses JSON-syntax.
Each configuration is located in a separate JSON-object.
For use with Quarterfall, use a code-action and add the specific JSON-objects to the qf object.
Please refer to the tables below how to configure specific options within the feedback tools.
An example for use with Quarterfall will be provided at the end of this section.

### General
With the general configuration it is possible to set overall aspects of the feedback generation.
For instance, the level of a learner (beginner, intermediate, expert) or the language that the feedback should be generated in.

Object: qfMainSettings

| Option Name | Possible Values | Regular Expression |
| ------ | --------------- | ----- |
| preferredLanguage | Google language codes (e.g. en, de, etc)| false |


### Syntax Checker Configuration
To be added: short description on what can be configured

Object: 

| Option Name | Possible Values | Regular Expression |
| ------ | --------------- | ----- |

### Style Checker Configuration
To be added: short description on what can be configured

Object: qfStyleConf

| Option Name | Possible Values | Regular Expression |
| ------ | --------------- | ----- |

### Semantics Checker Configuration
To be added: short description on what can be configured

Object: qfSemConfigs

| Option Name | Possible Values | Regular Expression |
| ------ | --------------- | ----- |

### Class Checker Configuration WIP
To be added: short description on what can be configured

Object: 

| Option Name | Possible Values | Regular Expression |
| ------ | --------------- | ----- |

### Test Checker Configuration WIP
To be added: short description on what can be configured

Object: 

| Option Name | Possible Values | Regular Expression |
| ------ | --------------- | ----- |


### Coverage Checker WIP
Coverage Checker maps not coverage constructor, methods and branching statements like
    
Statement:
    - if 
    - else if
    - else
    - for 
    - foreach
    - while 
    - case
    - default

to a default or custom feedback. Additional there is also the possibility to generate feedback for failed *JUNIT5* test.



Feedback Types:

- TEST - Default feedback for failed test.
- COVERAGE - Default feedback for not covered statements,
- CUSTOM - Your feedback for a failed test or a not covered statement.


Encoding feedback:

- " : TEST or COVERAGE" - Defines this type as default for all classes.
- "classname : TEST or COVERAGE" - Defines this type as default for an explicit class.
- "classname : TEST, COVERAGE or CUSTOM : method name or line index : feedback" - Defines a custom feedback for an explicit class and identifier.

The framework velocity is used to format the created feedback.
If no formatting template is provided only the feedback body will be generated.
The property summary can be used in a vm template to format and enrich your feedback
white additional information.
Following classes can be accessed be the summary property.
The summary has only diract access to the FormatterFacade class.


FormatterFacade Method:

- List< Feedback > feedbacks()
- List< TestFB > testFeedback()
- List< StmtFB > stmtFeedback()
- List< ByClass > byClass()

Over the Class FormatterFacade you can access following Classes:

ByClass Method:

- List< ByMethod > byMethods()
- CoverageCount branch()
- CoverageCount line()
- StateOfCoverage state()
- String name()

ByMethod Method:

- List< StmtFB > statementsFB()
- StateOfCoverage state()
- CoverageCount branch()
- CoverageCount line()
- String name()
- String content()
- int start()
- int end()


TestFB Method:

- String getBody()
- String className()
- String methodName()
- String got()
- String want()

StmtFB Method:

- String getBody()
- String className()
- String methodName()
- int start()
- int end()
- StatementType type()


Object:

| Option Name                      | Possible Values                                                                                                            | 
|----------------------------------|----------------------------------------------------------------------------------------------------------------------------|
| qf.qfCovSetting.feedback         | Array< String >:<br> classname : TEST, COVERGA <br> classname : TEST, COVERGA, CUSTOM : line index, method name : feedback |
| qf.qfCovSetting.excludeByTypeSet | Array< String >: GET, SET, PRIVATE, PROTECTED                                                                              |
| qf.qfCovSetting.excludeByNameSet | Array< String >: class or method name                                                                                      |
| qf.qfCovSetting.format           | String: content of a vm file  or empty                                                                                     |
| qf.additional                    | Zip file with additional resources                                                                                         |                                                                                                                                             |

```
qf.checkerClass="eu.qped.umr...CoverageChecker";
qf.file = {
    "id: "",
	"label":"",
	"extension: "",
	"path":"",
	"mimetype":"",
	"url":""
};
qf.additional = {
    "id: "",
	"label":"",
	"extension: "",
	"path":"",
	"mimetype":"",
	"url":""
};
qf.covSetting = {
   "excludeByTypeSet" : ["SET", "GET", "PRIVATE"],
   "excludeBYNameSet" : [],
   "format" :  #foreach($r in $summary.stmtFeedback())
               <p>Branch $r.type() failed at index $r.start()</p>
               #end",
   "feedback" : [":test", "Checker:COVERAGE"] 
};

```


### Example Configuration with Quarterfall
```
qf.checkerClass="eu.qped.umr.Mass";
qf.qfMainSettings = {
    "syntaxLevel":"BEGINNER",
    "preferredLanguage":"en" ,
    "styleNeeded":"true"  ,
    "semanticNeeded":"true"  
};
qf.qfStyleConf= {
    "mainLevel":"ADVANCED",
    "compLevel":null,
    "namesLevel":null,
    "basisLevel":null,
    "classLength":"100",
    "methodLength":"10",
    "cycloComplexity":"10",
    "fieldsCount":"-1",
    "varName":"undefined",
    "methodName":"[A]*",
    "className":"undefined"
};
```