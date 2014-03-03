set term png;
set xrange [0:500000];
set yrange[0:999999999];
set output 'q14.png';
plot 'q14.txt' using 1:2 title 'minimumTriL' with lines, \
'q14.txt' using 1:3 title 'minimumSimpleL' with lines ;
