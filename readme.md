QPED - Feedback tools for Java and Python
========================
To be added - short description of QPED

What is this project about?
------------
### General
With this project, you can analyze and automatically generate feedback for learners of computer science.
This project is aimed at early learners beginning their education by learning the JAVA and Python programming language.
For the respective versions of this project, please refer to their respective folder.

### What does it do?
At the moment, it is a highly customizable software that can compile and analyze submitted code and generate feedback.
The generated feedback can be configured by the user to be in a specific language (e.g. German, English, etc) and can be adjusted to the level of knowledge of the learner.

Furthermore, it is possible to require a learner to use a specific loop within their solution or solve a task recursively.
It is also possible to use different metrics and mutation testing to evaluate the test-quality of the submitted code and give the learner helpfull feedback.
For a full list of possible configurations, please refer to the section below.

Requirements
------------
This project is meant to interface specifically with the Quarterfall plattform in the form of a cloud-check, though it can be used with other plattforms.
We interface with Quarterfall by using a JSON configuration that is done in Quarterfall and submitted to this software.
The output is also generated as a JSON-object.
However, the feedback itself is formatted in markdown.

Licence
----------
MIT-licence

Copyright (c) 2021 QPED Organization

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 