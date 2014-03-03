set term png;
set xrange [0:500000];
set yrange[0:30000000];
set output 'q5.png';
plot 'q5.txt' using 1:2 title 'rechercheT max' with lines, \
'q5.txt' using 1:3 title 'rechercheL max' with lines , \
'q5.txt' using 1:4 title 'rechercheT min' with lines , \
'q5.txt' using 1:5 title 'rechercheL min' with lines ;
