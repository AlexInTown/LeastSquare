LeastSquare
===========

A convenient and efficient least square linear regression package for Apache Pig. Implemented with Java UDF and pig script.

This is a convenient Least Square Linear Regression for Data Scientist and Data Analyst who use Apache Pig for big data analysis. For more information Least Square Linear Regression, please see: http://en.wikipedia.org/wiki/Least_squares .

The key worker functions of this package are implemented with Java Algebraic UDF interface, which allows the calculation done at mapper side, just like Pig built-in function 'SUM' or 'COUNT'.

From the Apache Pig official site (for pig version 0.12.1) about algebraic interface http://pig.apache.org/docs/r0.12.1/udf.html#algebraic-interface, it says:

"An aggregate function is an eval function that takes a bag and returns a scalar value. One interesting and useful property of many aggregate functions is that they can be computed incrementally in a distributed fashion. We call these functions algebraic. 'COUNT' is an example of an algebraic function because we can count the number of elements in a subset of the data and then sum the counts to produce a final output. In the Hadoop world, this means that the partial computations can be done by the map and combiner, and the final result can be computed by the reducer."

Steps to use the package:

1.Copy 'run_least_square.sh' 'least_square.pig' 'LeastSquare.jar' into your working directory.

2.Change 'run_least_square.sh' to specify your input path, input data schema and output weight path.

3.Run 'run_least_square.sh' to compute least square weights.

4.Use the weight and the feature of test cases to predict target value.
