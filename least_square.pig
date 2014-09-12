register 'LeastSquare.jar';
DEFINE FeatureVectorToMatrix com.yahoo.leastsquare.FeatureVectorToMatrix();
DEFINE MatrixInverse com.yahoo.leastsquare.MatrixInverse();
DEFINE LeastSquare com.yahoo.leastsquare.LeastSquare();
DEFINE FeatureTargetVector com.yahoo.leastsquare.FeatureTargetVector();
/* 
 The schema of your input training data. All the attributes should be double, 
 the first attribute is the weight of this training case, the second attribute is the target value 'y', 
 the remaining attributes will be treated as features. 
 You can define your schema in your shell like this: 
 schema='case_weight:double,label:double,x1:double,x2:double,x3:double,...'
*/
features = load '$input' as ($schema) ;
/*
 This 'group all' clause won't let the 'features' really grouped into a single bag, if 
 you do not change the following group and foreach clause. Because 'FeatureTargetVector' 
 and 'FeatureVectorToMatrix' are implement using Java algebraic interface, the most of calculation 
 will done at mapper side, which is as efficient as pig built-in function like 'COUNT' and 'SUM'.
 */
features_grouped = group features all;
vector = foreach features_grouped generate FeatureTargetVector(features) as vec:tuple($schema) ; -- calculate XTy
matrix = foreach features_grouped generate FeatureVectorToMatrix(features) as mat:bag{}; -- calculate XTX
/*
 Calculate matrix inverse (XTX)^-1.
 */
matrix = foreach matrix generate MatrixInverse(mat) as mat:bag{};

/*
 Compute w = (XTX)^-1 XTy
 */
matrix_vector = join matrix by 1, vector by 1 USING 'replicated';
weights = foreach matrix_vector generate LeastSquare(matrix::mat, vector::vec ) as  t:tuple();
/*
 The weight output is a tuple, the schema is like (bias:double, w1:double, w2:double, w3:..)
*/
store weights into '$output';


