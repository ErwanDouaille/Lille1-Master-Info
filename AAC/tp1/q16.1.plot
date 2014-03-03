set term png;
set xrange [0:500000];
set yrange[0:30];
set output 'q161.png';
plot 'q16.txt' using 1:2 title 'log' with lines;
