
# PlanAhead Launch Script for Post-Synthesis floorplanning, created by Project Navigator

create_project -name homade -dir "/home/m1/douaille/AEV/tp2/homade/planAhead_run_1" -part xc6slx16csg324-3
set_property design_mode GateLvl [get_property srcset [current_run -impl]]
set_property edif_top_file "/home/m1/douaille/AEV/tp2/homade/Nexys3v3.ngc" [ get_property srcset [ current_run ] ]
add_files -norecurse { {/home/m1/douaille/AEV/tp2/homade} }
set_param project.paUcfFile  "Nexys3.ucf"
add_files "Nexys3.ucf" -fileset [get_property constrset [current_run]]
open_netlist_design
