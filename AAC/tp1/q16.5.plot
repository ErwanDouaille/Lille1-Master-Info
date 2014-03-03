set term png;
set xrange [0:1000];
set yrange[0:2000000000];
set output 'q165.png';
plot 'q16.txt' using 1:6 title 'KpN' with lines;
