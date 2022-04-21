QPED - Feedback tools for Java
========================
To be added - short description of QPED

What is this project about?
------------
### General
With this project, you can analyze and automatically generate feedback for learners of computer science.
This project is aimed at early learners beginning their education by learning the JAVA programming language.
For a python version of this project, please refer to the python folder.

### What does it do?
At the moment, it is a highly customizable software that can compile and analyze submitted code and generate feedback.
The generated feedback can be configured by the user to be in a specific language (e.g. German, English, etc) and can be adjusted to the level of knowledge of the learner.

Furthermore, it is possible to require a learner to use a specific loop within their solution or solve a task recursively.
It is also possible to use different metrics and mutation testing to evaluate the test-quality of the submitted code and give the learner helpfull feedback.
For a full list of possible configurations, please refer to the section below.

### Checkers
Certain parts of this project are referred to as "checkers".
A checker is a module that can be configured individually and has a specific analytical function for the submitted code.
A "Syntax Checker" is for instance used to analyze the syntax of submitted code and generate feedback in that specific regard.
Below you may find a list of currently implemented checkers and a sort description of each one.

#### Syntax Checker
To be added: Short description

#### Style Checker
To be added: Short description

#### Class Checker
To be added: Short description

#### Test Checker
To be added: Short description

Requirements
------------
This project is meant to interface specifically with the Quarterfall plattform in the form of a cloud-check, though it can be used with other plattforms.
We interface with Quarterfall by using a JSON configuration that is done in Quarterfall and submitted to this software.
The output is also generated as a JSON-object.
However, the feedback itself is formatted in markdown.

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


### Syntax Checker
To be added: short description on what can be configured

Object: 

| Option Name | Possible Values | Regular Expression |
| ------ | --------------- | ----- |

### Style Checker
To be added: short description on what can be configured

Object: qfStyleConf

| Option Name | Possible Values | Regular Expression |
| ------ | --------------- | ----- |

### Semantics Checker
To be added: short description on what can be configured

Object: qfSemConfigs

| Option Name | Possible Values | Regular Expression |
| ------ | --------------- | ----- |

### Class Checker WIP
To be added: short description on what can be configured

Object: 

| Option Name | Possible Values | Regular Expression |
| ------ | --------------- | ----- |

### Test Checker WIP
To be added: short description on what can be configured

Object: 

| Option Name | Possible Values | Regular Expression |
| ------ | --------------- | ----- |

### Example Configuration with Quarterfall
```
qf.checkerClass="eu.qped.umr.Mass";
qf.qfMainSettings = {
    "syntaxLevel":"1",
    "preferredLanguage":"en" ,
    "styleNeeded":"true"  ,
    "semanticNeeded":"true"  
};
qf.qfStyleConf= {
    "mainLevel":"profi",
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

Licence
----------
To be discussed