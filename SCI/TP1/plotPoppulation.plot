#!/usr/bin/env gnuplot

set term png;
set output 'Wator-population.png';
plot 'population' using 1:2 title 'fish population' with lines, \
'population' using 1:3 title 'shark population' with lines ;
