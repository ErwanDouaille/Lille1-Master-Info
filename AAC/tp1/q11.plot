set term png;
set xrange [0:500000];
set yrange[0:999999999];
set output 'q11.png';
plot 'q11.txt' using 1:2 title 'minimumTriT' with lines, \
'q11.txt' using 1:3 title 'minimumTriL' with lines ;
