set term png;
set xrange [0:500000];
set yrange[0:3000000];
set output 'q20.png';
plot 'q20.txt' using 1:2 title 'e=1' with lines, \
'q20.txt' using 1:3 title 'e=2' with lines, \
'q20.txt' using 1:4 title 'e=4' with lines, \
'q20.txt' using 1:5 title 'e=8' with lines ;
