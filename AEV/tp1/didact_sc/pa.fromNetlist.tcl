
# PlanAhead Launch Script for Post-Synthesis pin planning, created by Project Navigator

create_project -name didact_sc -dir "/home/m1/douaille/didact_sc/planAhead_run_1" -part xc6slx16csg324-3
set_property design_mode GateLvl [get_property srcset [current_run -impl]]
set_property edif_top_file "/home/m1/douaille/didact_sc/didact_top.ngc" [ get_property srcset [ current_run ] ]
add_files -norecurse { {/home/m1/douaille/didact_sc} {ipcore_dir} }
add_files "ipcore_dir/rom_msa.ncf" -fileset [get_property constrset [current_run]]
set_param project.pinAheadLayout  yes
set_param project.paUcfFile  "didact_top.ucf"
add_files "didact_top.ucf" -fileset [get_property constrset [current_run]]
open_netlist_design
