set term png;
set xrange [0:500000];
set yrange[-10:10];
set output 'q21.png';
plot 'q21.txt' using 1:2 title 'e=1' with lines, \
'q21.txt' using 1:3 title 'e=2' with lines, \
'q21.txt' using 1:4 title 'e=4' with lines, \
'q21.txt' using 1:5 title 'e=8' with lines ;
