
################################################################################
#################  grille lin + methode aleaT ##################################
# 
# rediriger les resultats des mesures pour le cas 0 dans le fichier cas0.txt
################################################################################
set term png;
set xrange [0:500000];
set yrange[0:200000000];
set output 'lin_aleaT.png';
plot 'cas0.txt' using 1:2 title 'aleaT' with lines, \
'cas0.txt' using 1:3 title '200 n' with lines, \
'cas0.txt' using 1:4 title '300 n' with lines, \
'cas0.txt' using 1:5 title '400 n' with lines, \
'cas0.txt' using 1:6 title '500 n' with lines, \
'cas0.txt' using 1:7 title '600 n' with lines ;
