### Documentation

- [Setting](#setting)
- [YAML Default Feedback](#yaml-default-feedback)


### Setting

The json object *qfCovSetting* is used to configured the coverage checker.
Over the field *feedback* it's required to define with feedback is generated.
There are three types of feedback:
- TEST     - Default feedback for failed test.
- COVERAGE - Default feedback for not covered statements,
- CUSTOM   - Your feedback for failed test or not covered statement. 

To define witch feedback is generated there are three encodings used: 

- " : TEST or COVERAGE" - Defines this type as default for all classes.
- "classname : TEST or COVERAGE" - Defines this type as default for an explicit class.
- "classname : TEST, COVERAGE or CUSTOM : method name or line index : feedback" - Defines feedback for an explicit class and identifier.

In all feedback following String are replaced with specific information. 
- CLASS :: gets replaced by the classname 
- METHOD  :: gets replaced by the method name,
- WANT :: gets replaced by the expected value of a test case
- GOT :: gets replaced by the actual value of a test case
- INDEX :: gets replaced by the line index where a statement is not covered
- TYPE :: gets replaced by type of statement that is not covered

For some assignments it's necessary to provide additional java files. 
Over the field *additional* the location of zip can be added. 

Over the field *format* it's possible to provide a template to format and enrich your feedback
with additional information.
The framework velocity is used to format the created feedback.
Following classes can be accessed by the summary property.

#### Example: 
Prints the type and index where a branch is not covered.

```vm
#foreach($bc in $summary.byClass())
    <p> Class::$bc.name() branch coverage is $bc.branch().procent() %</0>
    #foreach($bm in $bc.byMethod())
        #foreach($fb in $bm.statementsFB())
            <p>Branch $fb.type() failed at index $fb.start()</p>
        #end
    #end
#end
```

The summary has only diract access the FormatterFacade class.

FormatterFacade Method:

- List< Feedback > feedbacks()
- List< TestFB > testFeedback()
- List< StmtFB > stmtFeedback()
- List< ByClass > byClass()

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

Feedback Method:
- String getBody()

CoverageCount Method:
- int total()
- int missing()
- String percent()

### YAML Default Feedback
To provide different default feedback following yaml file has to be provided.

#### Example in english
```yaml
testFB : "In class <b>CLASS</b> the test method <b>METHOD</b> failed. The expected value is <b>WANT</b> but was actually <b>GOT</b>."
ifFB : "In class <b>CLASS</b> at the method <b>METHOD</b> the if statement in line <b>INDEX</b> is always wrong."
elseFB : "In class <b>CLASS</b> at the method <b>METHOD</b> the else statement in line <b>INDEX</b> is never used."
elseIfFB : "In class <b>CLASS</b> at the method <b>METHOD</b> the else-if statement in line <b>INDEX</b> is always wrong."
forFB : "In class <b>CLASS</b> at the method <b>METHOD</b> the for-loop in line <b>INDEX</b> is always wrong."
forEachFB : "In class <b>CLASS</b> at the method <b>METHOD</b> the collection of the for loop in line <b>INDEX</b> is always empty."
whileFB : "In class <b>CLASS</b> at the method <b>METHOD</b> the while loop in line <b>INDEX</b> is always wrong."
caseFB : "In class <b>CLASS</b> at the method <b>METHOD</b> the case statement in line <b>INDEX</b> is always wrong."
methodFB : "In class <b>CLASS</b. the method <b>METHOD</b> in line <b>INDEX</b> is never used."
constructorFB : "In class <b>CLASS</b> the constructor in never called"
```