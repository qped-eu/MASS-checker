
  
## Tutorial for Class Checker  
This tutorial concerns itself with the use of the **class checker**, specifically how to set it up and use. The class checker has the ability to check for class properties, such as present class members and inherited classes. Furthermore it can check for the keywords that should be present in class elements, such as access and non access modifiers.   
We will use an example task to illustrate different features of the checker, demonstrate what an example set up would look like and what the checker is going to look for in the provided student solution. Based on the information given and the student solution, possible mismatches are marked and generated feedback for.  
## Task  
Create an ``interface Employee`` and a ``class Programmer``.  
The ``interface Employee`` possesses the following two methods:  
  
- ``void sleeping()``  
- ``void working()``  
  
Employee also possesses the method ``default void getSalary()``, which outputs the salary of the employee.  
For simplicity lets assume that every employee has a salary of 50000.  
  
The ``class Programmer`` implements the ``interface Employee`` and has a ``private String`` field, which holds the status of the  
employee. The field is set with the implemented methods ``void sleeping()`` and ``void working()``, either with the status **sleeping** or **working**.  
Programmer also possesses a method ``public String getStatus()``, which returns the current status of the programmer.  
  
## Example .json Input  
```json  
{  
	[ 
		{  
			"fullyQualifiedName": "Employee",  
			"classKeywordConfig": {  
				"allowExactModifierMatching": false,  
				"publicModifier": "no",  
				"protectedModifier": "no",  
				"privateModifier": "no",  
				"packagePrivateModifier": "yes",  
				"emptyNonAccessModifier": "yes",  
				"abstractModifier": "no",  
				"staticModifier": "no",  
				"finalModifier": "no",  
				"classType": "no",  
				"interfaceType": "yes",  
				"name": "Employee"  
			},  
			"inheritsFromConfigs": [],  
			"fieldKeywordConfigs": [],  
			"methodKeywordConfigs": [  
				{   
					"allowExactModifierMatching": false,  
					"publicModifier": "no",  
					"protectedModifier": "no",  
					"privateModifier": "no",  
					"packagePrivateModifier": "yes",  
					"emptyNonAccessModifier": "yes",  
					"abstractModifier": "no",  
					"staticModifier": "no",  
					"finalModifier": "no",  
					"defaultModifier": "no",  
					"synchronizedModifier": "no",  
					"nativeModifier": "no",  
					"type": "void",  
					"name": "sleeping"  
				}, 
				{  
					"allowExactModifierMatching": false,  
					"publicModifier": "no",  
					"protectedModifier": "no",  
					"privateModifier": "no",  
					"packagePrivateModifier": "yes",  
					"emptyNonAccessModifier": "yes",  
					"abstractModifier": "no",  
					"staticModifier": "no",  
					"finalModifier": "no",  
					"defaultModifier": "no",  
					"synchronizedModifier": "no",  
					"nativeModifier": "no",  
					"type": "void",  
					"name": "working"  
				}, 
				{  
					"allowExactModifierMatching": true,  
					"publicModifier": "no",  
					"protectedModifier": "no",  
					"privateModifier": "no",  
					"packagePrivateModifier": "yes",  
					"emptyNonAccessModifier": "no",  
					"abstractModifier": "no",  
					"staticModifier": "no",  
					"finalModifier": "no",  
					"defaultModifier": "yes",  
					"synchronizedModifier": "no",  
					"nativeModifier": "no",  
					"type": "void",  
					"name": "getSalary"  
				} 
			],  
			"matchExactFieldAmount": false,  
			"matchExactMethodAmount": true,  
			"customFieldFeedback": [],  
			"customMethodFeedback": [],  
			"customClassFeedback": [],  
			"customInheritanceFeedback": []    
		},  
		{  
			"fullyQualifiedName": "Programmer",  
			"classKeywordConfig": {  
					"allowExactModifierMatching": false,  
					"publicModifier": "no",  
					"protectedModifier": "no",  
					"privateModifier": "no",  
					"packagePrivateModifier": "yes",  
					"emptyNonAccessModifier": "yes",  
					"abstractModifier": "no",  
					"staticModifier": "no",  
					"finalModifier": "no",  
					"classType": "yes",  
					"interfaceType": "no",  
					"name": "Programmer"  
				},  
			"inheritsFromConfigs": [  
				{  
					"classType": "no",  
					"interfaceType": "yes",  
					"name": "Employee"  
				} 
			],  
			"fieldKeywordConfigs": [  
				{  
					"allowExactModifierMatching": false,  
					"publicModifier": "no",  
					"protectedModifier": "no",  
					"privateModifier": "yes",  
					"packagePrivateModifier": "no",  
					"emptyNonAccessModifier": "no",  
					"staticModifier": "no",  
					"finalModifier": "no",  
					"transientModifier": "no",  
					"volatileModifier": "no",  
					"type": "String",  
					"name": "?"  
				} 
			],  
			"methodKeywordConfigs": [  
				{  
					"allowExactModifierMatching": false,  
					"publicModifier": "no",  
					"protectedModifier": "no",  
					"privateModifier": "no",  
					"packagePrivateModifier": "yes",  
					"emptyNonAccessModifier": "yes",  
					"abstractModifier": "no",  
					"staticModifier": "no",  
					"finalModifier": "no",  
					"defaultModifier": "no",  
					"synchronizedModifier": "no",  
					"nativeModifier": "no",  
					"type": "void",  
					"name": "sleeping"  
				}, 
				{  
					"allowExactModifierMatching": false,  
					"publicModifier": "no",  
					"protectedModifier": "no",  
					"privateModifier": "no",  
					"packagePrivateModifier": "yes",  
					"emptyNonAccessModifier": "yes",  
					"abstractModifier": "no",  
					"staticModifier": "no",  
					"finalModifier": "no",  
					"defaultModifier": "no",  
					"synchronizedModifier": "no",  
					"nativeModifier": "no",  
					"type": "void",  
					"name": "working"  
				}, 
				{  
					"allowExactModifierMatching": false,  
					"publicModifier": "no",  
					"protectedModifier": "no",  
					"privateModifier": "no",  
					"packagePrivateModifier": "yes",  
					"emptyNonAccessModifier": "yes",  
					"abstractModifier": "no",  
					"staticModifier": "no",  
					"finalModifier": "no",  
					"defaultModifier": "no",  
					"synchronizedModifier": "no",  
					"nativeModifier": "no",  
					"type": "String",  
					"name": "getStatus"  
				} 
			],  
			"matchExactFieldAmount": true,  
			"matchExactMethodAmount": false,  
			"customFieldFeedback": [  
				"Please verify that all keywords are set appropriately." 
			],  
			"customMethodFeedback": [],  
			"customClassFeedback": [],  
			"customInheritanceFeedback": []    
		}  
	]
}  
```  
  
## .json Input Explanation  
The input allows us to specify, what we can expect from the solution. In particular, the input .json file is used to check against the student solution and generate feedback based on discrepancies.  
For each class we define an object, that holds all expected information for the class, including the class declaration itself.   
  
Here we defined two objects for ``interface Employee`` and ``class Programmer``.  
  
Through these objects we can define, what the fields and methods signatures inside the classes have to look like, what the class declaration should be expected to possess and what classes should be inherited.   
  
For each class, it is recommended to provide the fully qualified name of the class, such that the class in the solution can be matched up with the object here.  
  
If a fully qualified name can not be provided, the name of the class in the object will suffice, but this may run the risk of not being able to match up the provided classes if the names are not unique.  
  
## Keyword choice  
For most configuration option you can pick between three different options:   
- YES  
- NO  
- DON'T CARE  
  
**YES** indicates, that you want to allow a specific keyword in the declaration. This means that the checker will expect the keyword to be present in the declaration and will generate feedback, if that is not the case.  
  
**NO** indicates, that you do not want to allow a specific keyword. This means that if the keyword appears in the solution, it's classified as a mistake, and the checker generates appropriate feedback for it.  
  
**DON'T CARE** indicates that it does not matter if the keyword is present in the solution. No feedback will be generated for this keyword and will effectively be ignored from consideration.  
  
You can include the expected name for each element, but for **fields** and **methods** it may be useful to omit the name and allow the student to pick a fitting name.  
To choose this option, you can replace the name in the expected element with a ? (question mark), to indicate that any value can be accepted there, as long as it fits the usual style and JAVA rules.  
  
Generally it is important to be conscious about the modifiers you pick, as not every modifier combination is considered legal by the JAVA specification.   
  
It is your responsibility to pick legal combinations and make sure that they work together. This solution will not be compiled to verify its correctness, but rather will be treated as the **model solution** and checked against any student solution.   
  
If any discrepancies occur between the two, this solution will be seen as the correct one and feedback will be generated based on this.  
  
## .json Input Walkthrough  
  
### Employee  
This section concerns itself with what we can expect from the student solution for ``interface Employee``.  
  
#### classKeywordConfig  
For *Employee* we expect the type to be an interface, as indicated by ``interfaceType: yes``, and do not want to allow the type *class*, as indicated by ``classType: no``.  
  
The name of the interface is expected to be Employee, as indicated by ``name: Employee``. Note that this field is required for each class or interface.  
  
#### inheritsFromConfig  
We do not expect *Employee* to be inheriting other classes, so that *inheritsFromConfigs* is empty.  
  
#### fieldKeywordConfig  
Similarly, we do not expect there to be fields present in the interface, such that the array *fieldKeywordConfigs* is empty.  
  
#### methodKeywordConfig  
For the methods, we expect the interface to possess three different methods, indicated by the three different objects inside the *methodKeywordConfigs* array.  
The method ``sleeping`` is expected to have no access modifier, indicated by ``"packagePrivateModifier": "yes"``, and no non access modifier, indicated by ``"emptyNonAccessModifier" : "yes"``.   
  
All the other keywords have been set to **no**, indicating that they are not allowed in the solution.   
  
The expected return type is set to ``void`` and the expected name is ``sleeping``, such that we can expect the method signature to be ```void sleeping()```.  
Note that each method declaration here does not possess parentheses, as these are not compared against and can be omitted.  
  
We expect the method ``void working()`` to be set similarly.    
  
The method ``void getSalary()`` is expected to have a ``default`` modifier, indicated by ``"defaultModifier" : "yes"`` and do not want to allow an empty non access modifier. The access modifier is set, similarly to the previous methods, to empty or package private.  
Thus, we expect the full method signature ``default void getSalary()``.  
  
----  
  
### Programmer  
This section is for the object created for the ``class Programmer``.  
  
#### classKeywordConfig  
We expect ``class Programmer`` to be the class declaration found in the student solution.   
This means that the *access modifier* is set to package private, set by ``"packagePrivateModifier" : "yes"``,  the *type* to be ``class``, set by ``"classType": "yes"`` and the *name* of the class to be ``Programmer``.   
We do not expect any *non access modifier* to be part of the class declaration, thus we set ``"emptyNonAccessModifier" : "no"``.  
  
The other modifiers have also been set to no, to strictly only allow ``class Programmer`` to be part of the declaration.  
You could also set all of those modifiers to ``"don't care"``, which would ignore those modifiers, but the student would be allowed to use them in the solution.  
  
#### inheritsFromConfig  
For the super classes we expect the ``interface Employee`` to be inherited.  
We indicate that by setting ``"interfaceType" : "yes"`` and ``"classType" : "no"``, such that only interfaces are allowed here.  
Now we further expect the inherited class to have the name *Employee*, such that we have the desired interface implementation in the target class.  
  
#### fieldKeywordConfig  
We expect there to be one field, in particular it is not allowed to introduce more fields than expected, as indicated by ``"matchExactFieldAmount" : "true"`` outside of this array.   
This means that, since we only possess this one object for the expected field, the tool is going to make sure that only this one field is present in the solution. If that is not the case, the tool is going to generate feedback.  
  
For this field in particular, we expect the access modifier to be ``private``, indicated by ``"privateModifier" : "yes"`` and the type of the field to be ``String``.  
Through ``"name" : "?"``, we indicate that any name will be accepted here, allowing the student to choose the name themselves.  
  
#### methodKeywordConfig  
As we expect the methods from *Employee* to be implemented here, we also specify them in the *methodKeywordConfig* again, to indicate that we expect them inside this class.  Note that we did not specify the method ``default void getSalary`` here, as it has already been implemented and does not need to be implemented again in subclasses.  
  
Furthermore, we expect the additional method ``String getStatus()`` to be implemented here. Similarly to the other methods, we expect the method signature to possess a ``package private`` modifier and no non access modifier.  
The expected type is ``String`` and the expected name is ``getStatus``, which combined gives us the needed method.  
  
  
----  
  
### Additional Keywords  
As mentioned previously, ``matchExactFieldAmount`` and ``matchExactMethodAmount`` allow you to set, how the checker is going to check for the amount of fields and methods.   
  
Usually the checker only checks, if the expected amount of fields and methods does not exceed the actual fields and methods in the solution. With this option, the checker also checks if the amount of fields and methods in the actual solution is exactly the same as the amount in the expected solution.  
  
This allows you to specify, if you wish to allow additional helper methods or helper fields, without generating additional feedback.  
  
### Custom Feedback  
If you wish to include additional feedback for mistakes found, you can include them in ``customFieldFeedback``, ``customMethodFeedback``, ``customClassFeedback`` and ``customInheritanceFeedback``.  
  
Each array allows you to specify multiple messages. If a mistake in a field has been found, all elements in ``customFieldFeedback`` will be included in the feedback message for the affected field.  
  
Similarly, for method, class declaration or inheritance issues you can include feedback in those other arrays, which are then going to be included in the provided feedback for each of their area.  
  
 ---
  
  
## Example Student Submission  
The following code could be a possible student submission, that is now going to be checked by the tool against the expected keywords provided in the input.  
  
### Interface Employee  
```java  
interface Employee {  
 void working(); void sleeping(); public default int getSalary() { return 50000; }}  
```  
### Class Programmer  
```java  
public class Programmer implements Employee {  
 public String status = ""; public void working() { status = "working"; } public void sleeping() { status = "sleeping"; } public String getStatus() { return status; }}  
```  
## Generated Feedback  
  
### Feedback for Employee  
As the ``interface Employee`` matches up with the expected values provided in the .json file, the checker does not generate any feedback for it.  
  
### Feedback for Programmer  
``class Programmer`` does not match up exactly with the expected values provided in the .json file and thus the checker generates feedback for this class.  
As we can see for the field ``status`` inside of ``Programmer``, the expected access modifier, set in *fieldKeywordConfig*, is``private``, but here we see that the student set the access modifier for the field to ``public``, indicating a mistake. We can also see that the student had the freedom to choose the *name* of the field, which the checker does not generate any feedback for.   
  
The checker now generates the following feedback for the field ``status``:  
"AccessModifierError: Different access modifier for **status** in **class Programmer** expected.  
Please verify that all keywords are set appropriately."  
  
We can see, that the tool identifies the mistake in the field ``status`` and generates the feedback specifically targeted at the access modifier.  
Additionally, since this is a field mistake, we include the custom feedback message from ``customFieldFeedback`` to further direct the student towards the task.