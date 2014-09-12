queue=apg_qashort_p3
schema=weight:double,label:double,f1:double
input=/user/zhenouyang/least_square_new.txt
output=least_square_out/weights
PIG_PARAM="-Dmapred.job.queue.name=$queue -Dmapred.job.reduce.memory.mb=4096 -Dmapreduce.job.acl-view-job='*'";
cmd="pig $PIG_PARAM -Dmapred.child.env=ROOT=. -p output=$output -p schema='$schema' -p input=$input least_square.pig" 
eval $cmd
