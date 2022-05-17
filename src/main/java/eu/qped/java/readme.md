QPED - Feedback tools for Java
========================

## Checkers
Certain parts of this project are referred to as "checkers".
A checker is a module that can be configured individually and has a specific analytical function for the submitted code.
A "Syntax Checker" is for instance used to analyze the syntax of submitted code and generate feedback in that specific regard.
Below you may find a list of currently implemented checkers and a sort description of each one.

### Syntax Checker
Syntax checker has the ability to compile java code and report the encountered errors in readable form for students with extra information that helps the check system.

### Style Checker
Style checker is a checker that checks and then reports common formatting, code quality or efficiency  violations.
The report also contains examples for fixing the violations.
It is also possible to base the report on one of the three knowledge levels specified in the [configuration section below](#Syntax-Checker-Configuration).

### Class Checker
Class Checker compares the given solution to the expected design decisions, such as correctly  setting field or method visibilities.
This includes checking if the solution correctly uses the expected class type for either the current class, or the inherited classes. 
The checker generates feedback if any discrepancies occur and provides starting points on how to resolve the issues.

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

Object: mainSettings

| Option Name | Possible Values | Regular Expression |
| ------ | --------------- | ----- |
| preferredLanguage | Google language codes (e.g. en, de, etc)| false |


### Syntax Checker Configuration
The syntax checker has not many configs because its goal just to check the correctness of the code.
Object: mainSettings

| Option Name | Possible Values | Regular Expression |
| ------ | --------------- | ----- |
| level | BEGINNER, INTERMEDIATE, PROFESSIONAL | false| 

### Style Checker Configuration

In syntax checker can many configs be modified. Classes, methods and variables name pattern.
Level of the student in naming conventions, efficiency, and styling.
Length of the class, method and variables names.
The max allowed number of Fields, methods and lines.
If the number is below 0, the option is ignored and the learner may use as many as they like.

Object: styleSettings

| Option Name | Possible Values | Regular Expression |
| ------ | --------------- | ----- |
| mainLevel | BEGINNER, INTERMEDIATE, PROFESSIONAL | false |
| compLevel | BEGINNER, INTERMEDIATE, PROFESSIONAL | false |
| namesLevel | BEGINNER, INTERMEDIATE, PROFESSIONAL | false |
| basisLevel | BEGINNER, INTERMEDIATE, PROFESSIONAL | false |
| classLength | numeric [-1, 0, 1, *] | false |
| methodLength | numeric [-1, 0, 1, *] | false |
| cycloComplexity | numeric [-1, 0, 1, *]  | false |
| fieldsCount | numeric [-1, 0, 1, *] | false |
| varName | String | true |
| methodName | String | true |
| className | String | true |


### Semantics Checker Configuration
Semantic checker has the ability to check the code according to criteria specified by the teacher 
e.g. number of loops or number of ```if``` expressions allowed and if it is required to solve the task with recursion.
Setting a negative number ignores the option and lets the learner use as many as they like.


Object: semSettings

| Option Name | Possible Values       | Regular Expression |
| ------ |-----------------------| ----- |
| methodName | String                | false |
| recursionAllowed | TRUE, FALSE           | false |
| whileLoop | numeric [-1, 0, 1, *] | false |
| forLoop | numeric [-1, 0, 1, *]      | false |
| forEachLoop | numeric [-1, 0, 1, *]     | false |
| ifElseStmt | numeric [-1, 0, 1, *]      | false |
| doWhileLoop | numeric [-1, 0, 1, *]       | false |
| returnType | String                | false |

### Class Checker Configuration WIP
The class checker provides ways for the teacher to specify on what to expect from a given class.
The teacher can specify, for example, what the expected class type and name should be, which
classes the current class should inherit and what each field or method in the class should look like.

Object: designSettings

| Option Name | Possible Values | Regular Expression |
| ------ | --------------- | ----- |
| classTypeName | String | false |
| inheritsFrom | String Array | false |
| fieldKeywords | String Array | false |
| methodKeywords | String Array | false |


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

qf.designSettings = {
    "classTypeName":"class:Card",
    "inheritsFrom":["interface:Comparable"],
    "fieldKeywords": [
        "private String name",
        "private String year"
    ],
    "methodKeywords": [
        "public int compareTo"
    ]
};
```