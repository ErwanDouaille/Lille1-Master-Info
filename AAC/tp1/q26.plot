set term png;
set xrange [0:50];
set yrange[0:1000];
set output 'q26.png';
plot 'q26.txt' using 1:2 title 'k=3' with lines, \
'q26.txt' using 1:3 title 'k=6' with lines, \
'q26.txt' using 1:4 title 'k=9' with lines, \
'q26.txt' using 1:5 title 'k=12' with lines ;
