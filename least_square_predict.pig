register 'LeastSquare.jar';
DEFINE FeatureWeightDot com.yahoo.leastsquare.FeatureWeightDotProduct();
-- You should have a tuple whose attribute are features(no need to consider bias, but the order of feature attributes should be corresponding to the weight tuple). 
features = load '$input' as  ( $schema ) ;
weights = load '$weights' as w:tuple(f1:double,f2:double);
features_weights = join features by 1, weights by 1 using 'replicated';
feature_score = foreach features_weights generate 
    features::label,
    FeatureWeightDot(features::vec, weights::w) as score;
store feature_score into '$output';

