QPED - Feedback tools for Java
========================

## Checkers
Certain parts of this project are referred to as "checkers".
A checker is a module that can be configured individually and has a specific analytical function for the submitted code.
A "Syntax Checker" is for instance used to analyze the syntax of submitted code and generate feedback in that specific regard.
Below you may find a list of currently implemented checkers and a sort description of each one.

### Syntax Checker
Syntax checker has the ability to compile java code and report the encountered errors 
in readable form for students with extra information that helps the check system.

### Style Checker
Style checker is a checker that check then report the common formatting, code quality, efficiency, 
violations with solutions examples. The report is often relative to the knowledge level of the student.

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
The syntax checker has not many configs because its goal just to check the correctness of the code.
Object: qfMainSettings

| Option Name | Possible Values | Regular Expression |
| ------ | --------------- | ----- |
| level | BEGINNER, INTERMEDIATE, PROFESSIONAL | false| 

### Style Checker Configuration

In syntax checker can many configs be modified. Classes, methods and variables name pattern.
Level of the student in naming conventions, efficiency, and styling.
Length of the class, method and variables names.
The max allowed number of Fields, methods and lines.

Object: qfStyleConf

| Option Name | Possible Values | Regular Expression |
| ------ | --------------- | ----- |
| mainLevel | BEGINNER, INTERMEDIATE, PROFESSIONAL | false |
| compLevel | BEGINNER, INTERMEDIATE, PROFESSIONAL | false |
| namesLevel | BEGINNER, INTERMEDIATE, PROFESSIONAL | false |
| basisLevel | BEGINNER, INTERMEDIATE, PROFESSIONAL | false |
| classLength | numeric [1, *] | false |
| methodLength | numeric [1, *] | false |
| cycloComplexity | numeric [1, *]  | false |
| fieldsCount | numeric [1, *] | false |
| varName | String | true |
| methodName | String | true |
| className | String | true |


### Semantics Checker Configuration
Semantic checker has the ability to check the code according to criteria specified by the teacher 
e.g. number of loops or number of if expressions allowed and if recursion is in the question required.


Object: qfSemConfigs

| Option Name | Possible Values | Regular Expression |
| ------ | --------------- | ----- |
| methodName | String | false |
| recursionAllowed | TRUE, FALSE | false |
| whileLoop | numeric [1, *] | false |
| forLoop | numeric [1, *] | false |
| forEachLoop | numeric [1, *] | false |
| ifElseStmt | numeric [1, *] | false |
| doWhileLoop | numeric [1, *] | false |
| returnType | String | false |

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

### Example Configuration with Quarterfall
```
qf.checkerClass = "eu.qped.java.checkers.mass.Mass";

qf.mainSettings = {
    "syntaxLevel":"BEGINNER",
    "preferredLanguage":"en",
    "semanticNeeded":"true", 
    "styleNeeded":"true"  
};

qf.styleSettings= {
    "mainLevel":"ADVANCED",
    "compLevel":"BEGINNER"  , 
    "namesLevel":"ADVANCED"  , 
    "basisLevel":"ADVANCED" ,
    "classLength":"100" ,
    "methodLength":"10" , 
    "cycloComplexity":"10" , 
    "fieldsCount":"-1" ,
    "varName":"undefined" , 
    "methodName":"[a-z][a-zA-Z0-9]*",
    "className":"undefined"
};
 
qf.semSettings = {
    "methodName":"getMinimum" ,
    "recursionAllowed":"false",
    "whileLoop":"0",
    "forLoop":"0",
    "forEachLoop":"0",
    "ifElseStmt":"1",
    "doWhileLoop":"0",
    "returnType":"float"
};
```