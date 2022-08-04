=== QPED - Quarterfall communication - Tests

The system tests mimic the way how Cloud checks are executed by the Quarterfall Cloud check server, namely
by providing the configuration via the file `qf.json` and by running the script `run.sh` in a new process.
The `SystemTest` will scan the folder `src/test/resources/system-tests` for sufolders containing system tests.
Such a subfolder must contain at least three files:

* `description.yaml`
* `qf-input.json`
* `qf-expected.json`

# Description
The description has content like this:

```yaml
title: "Short title of test case"
description: |
    Long description of this test case. It can be written as multi-line
    string literal, as in this example.
    
    This description may be written in _[markdown](https://markdown-it.github.io)_ syntax.
author: "Christoph Bockisch"
qfAssignmentShareCode: "1QJX3K "
disabled: false
```
The property qfAssignmentShareCode is optional and can contain the code of a Quarterfall question that corresponds to this test case.
The disabled property can be used to exclude tests from a test run.

# JSON files

The two JSON files correspond to the Quarterfall objects provided to the Cloud check via the `qf.json` file. Thereby, `qf-input.json` contains the
Quarterfall object that is used to invoke the Cloud check. The file `qf-expected.json` is compared to the Quarterfall object after running the
Cloud check (i.e., the `qf.json` file after it has been overwritten by the Cloud check when it terminated).
The comparison is performed with the JSON comparator `ro.skyah.comparator.JSONCompare` which tolerates different orders of properties in the
JSON string as well as the presence of additional properties.

# Running the system tests

The class `SystemTest` is an ordinary JUnit test and can be executed by any JUnit runner, i.e., also by means of the facilities of your IDE.
For each found system test, the framework will create a new process to run the `run.sh` script which will ultimately run maven to execute
the Checker (by executing the class `CheckrRunner`).
Thus, the actual Checker will run within a different JVM than the `SystemTest` class, which makes debugging the Checker during a system test
a bit more complicated. But the framework has built in a means to make debugging possible.

To debug your Checker during a system test, you must start the `SystemTest` JUnit test in debug mode. In the first place, this enables you only
to debug the class `SystemTest`. Nevertheless, this class detects that it is run in debug mode and will also instruct Maven to start a JVM for
executing the CheckerRunner in the debug mode. Since this second JVM has not been started by the IDE, you still need to tell the IDE to connect
its debugger to the second JVM, which will be waiting for a remote debugger connection on port 8000.

If no debugger is attached, the JVM will wait forever and the system test does not terminate. Of course, the process executing the `SystemTest`
can still be terminated explicitly.
Since the system test framework will create a new process (including a freshly started JVM) for each found system test, it is also necessary
to attach the remote debugger newly whenever a new system test is executed.

For instructions on how to attach a remote debugger, you can have a look at the following tutorials:

* [IDEA](https://www.jetbrains.com/help/idea/tutorial-remote-debug.html#8d4d52a4)
* [Eclipse](https://dzone.com/articles/how-debug-remote-java-applicat)
