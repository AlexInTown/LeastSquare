queue=apg_qashort_p3
#schema="(s1:double, s2:double, s3:double)";
schema='label:double,vec:tuple(f1:double)'
input=least_square_to_predict.txt
weights=least_square_out/weights
predicts=least_square_out/preidicts
PIG_PARAM="-Dmapred.job.queue.name=$queue -Dmapred.job.reduce.memory.mb=4096 -Dmapreduce.job.acl-view-job='*'";
cmd="pig $PIG_PARAM -Dmapred.child.env=ROOT=. -p output=$predicts -p schema='$schema' -p weights='$weights' -p input='$input' least_square_predict.pig" 
eval $cmd
