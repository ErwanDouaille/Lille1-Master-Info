set term png;
set xrange [3:50];
set yrange[-0.5:0.5];
set output 'q29.png';
plot 'q29.txt' using 1:2 title 'mesureMystereT' with lines, \
'q29.txt' using 1:3 title 'mesureMystereL' with lines ;
