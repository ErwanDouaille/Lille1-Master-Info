set term png;
set xrange [0:500000];
set yrange[0:30];
set output 'q18.png';
plot 'q18.txt' using 1:2 title 'k=1' with lines, \
'q18.txt' using 1:3 title 'k=5' with lines, \
'q18.txt' using 1:4 title 'k=10' with lines, \
'q18.txt' using 1:5 title 'k=20' with lines ;
