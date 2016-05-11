#!/usr/bin/env gnuplot

set term png;
set output 'Wator-ratio.png';
plot 'ratio' lines 7, \
     'ratio' using 1:2:($0+1) title 'ratio line' with lines
