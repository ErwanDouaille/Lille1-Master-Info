set term png;
set xrange [0:500000];
set yrange[0:300000000];
set output 'q10.png';
plot 'q10.txt' using 1:2 title 'minimumSimpleT' with lines, \
'q10.txt' using 1:3 title 'minimumSimpleL' with lines ;
