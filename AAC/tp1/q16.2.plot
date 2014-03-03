set term png;
set xrange [0:500000];
set yrange[0:2000000000];
set output 'q162.png';
plot 'q16.txt' using 1:3 title 'NpK' with lines;
