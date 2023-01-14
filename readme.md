[![](https://qped.eu/logo.png)](https://qped.eu/)

QPED (Quality-focused Programming Education) - Feedback tools for Java
========================

What this readme is not about
----------

For a description of the implementation of the MASS Checker (QPED Feedback tools for Java) as well as the QPED project, please refer to the `readme.md` in the `master` branch.

Purpose of the `qf` branch
----------

This `qf` branch has the purpose of providing a binary distribution of the MASS Checker to limit the data to download and avoid the need for compilation when running MASS.
MASS is particularly designed to be used for generating feedback in the [Quarterfall learning platform](https://www.quarterfall.com/) which integrates MASS as "Cloudcheck".
Thus, to run MASS, the Git head revision is pulled and the `run.sh` is executed.
Using the `qf` branch reduces the time required for performing the MASS check.

How to update the `qf` branch
----------

To update this branch to a the latest version of the MASS Checker in the `master` branch, you must do the following.

* Go to your local copy of the `master` branch.

* Make sure the `master` branch is fully functioning by running the complete test suite, e.g. using `mvn test`.

* Check out a local copy of the `qf` branch and replace the contents of the `src/main` folder with those of the `master` branch's working copy.

* Replace the `pom.xml` file in the same way.

* Do the same with the folder `target/classes`.
  * Note that the classes-folder may be hidden in an IDE, therefore rather do the copying in the console or system explorer.
  * Also note that in the `master` branch the `target/classes` folder is not version controlled. Therefore make sure to compile all sources first, e.g., by running the tests as above or by running `mvn compile`.

* Do **not** replace the `run.sh` file unless you know exactly what you are doing. This script on the `qf` branch executes in "quiet" mode to avoid runtime overhead.

* Commit and push the changes `qf` branch to the `origin`.

Licence
----------
MIT-licence

Copyright (c) 2021 - 2023 QPED Organization

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 