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
Coverage Checker maps not covered constructor, methods and branching statements like

- if 
- else if
- else
- for 
- foreach
- while 
- case
- default

to default or custom feedback. Additional there is also the possibility to generate feedback for failed *JUNIT5* test.

#### Example Assignment:
For a given program description analyse all constraints of the program and test them. 

``` 
# Pseudocode-PROGRAM                            # Feedback-CUSTOM

if (setConstraint(true)) {                          
    if (subSetAConstraint(false)) {             // are you sure you tested all constraints. Maybe read passage X more carefully. 
        ... 
    }
    if (subSetBConstraint(true)) {
        if (constraintB1(true)) {               
            ...
        }
        if (constraintB2(false) {               // you overlooked one constraint for case B.
            ...
        }
    }
}
```
[coverage-doc]: checkers/coverage/doc.md
#### Feedback:
There are two types of feedback default and custom.
The default feedback is only available in english and german. 
If needed we are happy to support your language.
Custom feedback can be provided directly over the quarterfall-platform.
For more information over feedback and settings see [doc] [coverage-doc].


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