# this is only useful when the answer should be compliled
# and called by the checker
# write the answer to a source file
#echo "class Answer { $answer }" > ./src/main/java/Answer.java
#echo "class Answer { $answer }" > ./Answer.java

#echo "package eu.qped.umr;public class SumNumbers { public int sum(int a , int b) {$answer} }" > ./src/main/java/eu/qped/umr/SumNumbers.java


#mvn validate compile exec:java -Dexec.mainClass="eu.qped.umr.TestSum"

#mvn checkstyle:checkstyle -Dcheckstyle.includes=**\/Answer.java -Dcheckstyle.config.location=./checkstyle.xml
#java -jar checkstyle.jar -f xml -c checkstyle.xml Answer.java >> ./report.xml

# run the tests
#mvn validate compile exec:java -Dexec.mainClass="eu.qped.umr.CheckerRunner"
#mvn -e exec:java -Dexec.mainClass="eu.qped.umr.CheckerRunner"

gradle runWithJavaExec
