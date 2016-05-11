#!/usr/bin/env gnuplot

reset
set term png;
set output 'Segregation-satisfaction.png';

set style fill solid 1.00 border 0
set style histogram errorbars gap 2 lw 1
set style data histogram
set xtics rotate by -45
set grid ytics
set xlabel "Satisfaction"
set ylabel "Relative execution time vs. reference implementation"
set yrange [0:*]
set datafile separator ","
plot 'satisfaction' using 2:3:xtic(1) ti "Green population" linecolor rgb "#00FF00", \
'' using 4:5 ti "Red population" lt 1 lc rgb "#FF0000"
