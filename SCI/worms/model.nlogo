__includes ["IODA_2_3.nls"]
extensions [ioda table]

breed [ sea ] 
breed [ clouds cloud ]
breed [ fishes fish ]
breed [ walls wall ]
breed [ teams team ]
breed [ worms worm ]
breed [ heads head ]
breed [ targets target ]
breed [ mines mine ]
breed [ missiles missile ]
breed [ grenades grenade ]
breed [ blasts blast ]
breed [ fire ]

globals [ 
  current-team ; id of the team that currently plays
  active-worm ; reference to the actual worms that plays
  team-list ; list of the teams 
  ammunition
  ]

walls-own [ destructible? ]

teams-own [ 
  current-worm ; id of the worm that currently plays
  members ; list of the worms which compose the team
]

worms-own [ 
  health ; nb of life points of the worm
  my-team ; ref to its team
  delta-x ; horizontal speed (+1 = right, -1=left)
  delta-y ; vertical speed
  commands ; messages sent by player (movement, jump, shoot...)
  can-shoot ; only one shoot per worms
  ]

to setup
  clear-all
  init-world
  ioda:load-interactions "interactions.txt"
  ioda:load-matrices "matrix.txt" " \t"
  ioda:setup
  set active-worm nobody
  reset-ticks
end


to go
  if active-worm = nobody [ command-next ]
  ioda:go
  tick
  if (count teams = 1) and (nb-teams > 1) 
    [ user-message (word "VICTORY FOR TEAM " one-of teams) stop ]
end

to init-world
  set-default-shape fishes "fish right"
  set-default-shape clouds "cloud"
  set-default-shape worms "worm right"
  set-default-shape targets "small target"
  set-default-shape walls "tile brick"
  set-default-shape missiles  "rocket"
  set-default-shape heads "void"
  set-default-shape mines "mine"
  set-default-shape grenades "grenade"
  set-default-shape fire "fire"
  ask patches [set pcolor ifelse-value (pycor < 0) [blue - 2] [ sky + 2 ]]
  create-sea 1 [ setxy 0 min-pycor hide-turtle ]
  read-level "level1.txt"
  set team-list []
  create-teams nb-teams [ init-team ]
  create-clouds 3 + random 10 [ init-cloud ]
  create-fishes 5 + random 10 [ init-fish ]
end

to init-cloud
  set color gray + 4.9 - random-poisson 1
  set heading 90
  move-to one-of patches with [max-pycor - pycor < 3]
end

to init-fish
  set color one-of (list (red - 2) (gray + 2) (yellow + 1))
  set heading 90 + 180 * random 2
  ifelse heading = 90 [ set shape "fish right" ] [set shape "fish left"]
  move-to one-of patches with [pycor < 0]
end

to init-team
  set team-list fput self team-list 
  set color item (who mod nb-teams) [ red lime orange brown yellow magenta ]
  hatch-worms nb-worms-per-team
    [ set my-team myself init-worm ]
  set members [self] of (worms with [my-team = myself])
  hide-turtle   
end

to init-worm
  set commands []
  move-to one-of patches with 
    [(pycor >= 0) and (not any? turtles-here) and (any? walls-on patch-at 0 -1)]
  set delta-y 0
  set delta-x 1 - 2 * random 2
  set heading 0
  hatch-targets 1 [ 
    set heading ([delta-x] of myself) * (90 - angle)
    fd 2 set color color + 2
    create-link-with myself [ tie hide-link ]
  ]
  set health 100
  hatch-heads 1 [ 
    set heading 0 fd 1.5 set heading 180 set size 1.5 set color color + 2
    set label [health] of myself    
    create-link-with myself [ tie hide-link ] 
  ]
end

to-report outside-y [y]
  report y <= min-pycor or y >= max-pycor
end

to sea::filter-neighbors
  ioda:filter-neighbors-on-patches patches with [ pycor < 0 ]
end

to read-level [ filename ]
  if (not file-exists? filename) 
    [ user-message (word "MAP NOT FOUND: " filename)
      stop 
    ]
  file-open filename
;  let s read-from-string file-read-line ; list with width and height
;  resize-world 0 (first s - 1)  (1 - last s) 0
  set ammunition table:from-list read-from-string file-read-line
  let lines [] let s ""
  while [ not file-at-end? ]
     [ set lines fput file-read-line lines ]
  file-close
  let x 0 let y 0
  while [(y <= max-pycor) and (not empty? lines)]
    [ set x 0 
      set s first lines 
      set lines but-first lines
      while [(x + min-pxcor <= max-pxcor) and (x < length s)]
        [ ask patch (x + min-pxcor) y [ create-agent item x s ]
          set x x + 1 ]
      set y y + 1 ]
end

to create-agent [ char ]
  ifelse (char = "X") 
    [ sprout-walls 1 [ init-wall false ] ]
    [ ifelse (char = "x") 
        [ sprout-walls 1 [ init-wall true ] ]
        [ ; other agents ?
        ]
    ]
end

to init-wall [ d ]
  set destructible? d
  set heading 0 
  set color ifelse-value destructible? [ gray ] [ gray - 3 ]
end

to command-next
  set current-team (current-team + 1) mod length team-list
  ask item current-team team-list
    [ set current-worm (current-worm + 1) mod length members
      set active-worm item current-worm members]
end


to command-left
  if active-worm != nobody
    [ ask active-worm [ set commands fput "left" commands ]]
end

to command-right
  if active-worm != nobody
    [ ask active-worm [ set commands fput "right" commands ]]
end

to command-shoot
  if active-worm != nobody
    [ ask active-worm [ set commands fput "shoot" commands ]]
end

to command-jump
  if active-worm != nobody
    [ ask active-worm [ set commands fput "jump" commands ]]
end

to worms::die
  ask link-neighbors [ die ]
  ask my-team [ set members remove myself members ]
  ioda:die
end

to teams::die
  set team-list remove self team-list
  user-message (word "TEAM " self " IS DESTROYED!")
  ioda:die
end

to-report worms::excessive-speed?
  report abs delta-y > 1
end

to-report worms::in-water?
  report pycor < 0
end

to-report worms::nothing-below?
  report not any? walls-on patch-at 0 -1
end


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; COMMAND
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

to-report decode-command [ c ]
  ifelse (c = "right") 
    [ report [ 1 0 ]]
    [ ifelse (c = "left")
        [ report [ -1 0 ]]
        [ ifelse (c = "jump")
          [ ifelse worms::jumpOnLeft? 
            [ output-print "Jleft"
              report [ -1 1 ]]
            [ ifelse worms::jumpOnRight?
              [ output-print "Jright"
                report [ 1 1 ]]
              [report [ 0 1 ]] ;il faut check R/L second command ?              
            ]
          ]
          [ report [ 0 0 ]] 
          
        ]
    ]
end


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; FALL
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


to-report worms::can-fall?
  report delta-y >= 0
end

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; MOVE
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

to-report worms::move-command?
  if empty? commands [ report false ]
  report member? (first commands) ["left" "right" "jump" ]
end

to-report worms::can-move?
  let delta-v decode-command first commands
  let mx first delta-v 
  let my last delta-v
  
  if  patch (xcor + mx) (ycor + my) = nobody [report FALSE]
  report not any? walls-on patch-at mx my
end

to-report worms::jumpOnLeft?
  ;ask active-worm [
  report any? walls-on (patch (xcor - 1) ycor)
end

to-report worms::jumpOnRight?
  ;ask active-worm [
  report any? walls-on (patch (xcor + 1) ycor)
end

to worms::do-move
  let delta-v decode-command first commands
  set commands but-first commands
  set delta-x first delta-v
  set delta-y last delta-v
;  if (patch-at delta-x delta-y
  move-to patch-at delta-x delta-y
;  set commands []
end

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; SHOOT
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

to-report worms::shoot-command?
  if empty? commands [ report false ]
  output-print (first commands)
  report member? (first commands) ["shoot"]
end

to-report worms::can-shoot?
  report TRUE
end

to worms::do-shoot
  set commands but-first commands
  ifelse weapon = "mine"
    []
    [ifelse weapon = "bazooka"
      []
      [ifelse weapon = "grenade"
        []
        [ifelse weapon = "teleporter"
          [ ]
          [ifelse weapon = "punch"
            []
            [if weapon = "jetpack"
              []
            ]
          ]
        ]
      ]
    ]
end

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; OTHER
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

to-report worms::on-sea-floor?
  report pycor = min-pycor
end

to worms::filter-neighbors
  let mx delta-x let my delta-y let px pxcor let py pycor
  let p (patch-set patch-here)
  if delta-y < 0
    [ foreach n-values ceiling abs delta-y [?]
      [ set p (patch-set p patch-at 0 (- ?)) ] 
    ]
  ioda:filter-neighbors-on-patches p
end

to worms::reset-speed
  set delta-x 1 - 2 * random 2
  set delta-y 0 
  ifelse any? walls-on patch-here
    [ move-to patch-at 0 1 ]
    [ move-to patch-here ]
end

to worms::reduce-health
  set health health - 10
end

to worms::water-speed
  set delta-x 0 set delta-y -0.05
end

to worms::advance
  if  patch (xcor + delta-x) (ycor + 1.5 + delta-y) != nobody [setxy (xcor + delta-x) (ycor + delta-y)]
end

to worms::increase-vspeed
  if abs delta-y < 1.5
    [ set delta-y delta-y - 0.1 ]
end

to heads::update-health
  set label [health] of one-of link-neighbors
end

to heads::update-shape
  ifelse member? active-worm link-neighbors
    [ set shape "arrow" ]
    [ set shape "void" ]
end

to-report worms::falling?
  report delta-y < 0
end

to worms::decrease-hspeed
  set delta-x delta-x * 0.2
end


to-report targets::my-worm-is-active?
  report member? active-worm link-neighbors
end

to targets::update-angle
  let mx [delta-x] of one-of link-neighbors
  ask my-links [ untie ]
  fd -2 
  set heading mx * (90 - angle)
  fd 2 
  ask my-links [ tie ]
end

to worms::update-shape
  ifelse delta-x < 0 
    [ set shape "worm left" ]
    [ set shape "worm right" ]
end

to-report worm-shooting-angle
  report [heading] of one-of link-neighbors with [breed = targets]
end

to-report worms::bad-health?
  report health <= 0
end

to-report teams::bad-health?
  report empty? members
end

to fishes::turn-sometimes
  if random 1000 < 5 [ 
    set heading 360 - heading 
    ifelse heading = 90 [ set shape "fish right" ] [set shape "fish left"]
  ]
end

to fishes::advance
  fd abs random-normal 0.005 0.001
end

to clouds::advance
  fd wind * abs random-normal 0.1 0.2
end

to-report walls::below?
  report ycor < [ycor] of ioda:my-target
end

to-report walls::altitude
  report ycor - min-pycor
end
@#$#@#$#@
GRAPHICS-WINDOW
229
10
1409
635
32
-1
18.0
1
10
1
1
1
0
1
0
1
-32
32
-8
24
1
1
1
ticks
30.0

BUTTON
4
10
70
43
NIL
setup\n
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

BUTTON
78
10
141
43
NIL
go
T
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

OUTPUT
20
448
215
599
12

BUTTON
20
609
198
642
show primitives to write
setup\noutput-print ioda:primitives-to-write
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

SLIDER
4
128
159
161
nb-worms-per-team
nb-worms-per-team
1
10
3
1
1
NIL
HORIZONTAL

SLIDER
4
89
99
122
nb-teams
nb-teams
2
5
2
1
1
NIL
HORIZONTAL

CHOOSER
69
194
221
239
weapon
weapon
"mine" "bazooka" "grenade" "teleporter" "punch" "jetpack"
0

BUTTON
3
318
66
351
LEFT
command-left
NIL
1
T
OBSERVER
NIL
Q
NIL
NIL
0

BUTTON
152
318
222
351
RIGHT
command-right
NIL
1
T
OBSERVER
NIL
D
NIL
NIL
0

SLIDER
6
279
178
312
angle
angle
0
90
41
1
1
NIL
HORIZONTAL

BUTTON
70
359
147
392
SHOOT
command-shoot
NIL
1
T
OBSERVER
NIL
X
NIL
NIL
0

BUTTON
149
10
213
43
NEXT
command-next
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

MONITOR
4
193
63
238
PLAYER
current-team
0
1
11

BUTTON
78
317
141
350
JUMP
command-jump
NIL
1
T
OBSERVER
NIL
Z
NIL
NIL
0

SLIDER
5
242
177
275
force
force
0
100
45
1
1
NIL
HORIZONTAL

SLIDER
4
50
176
83
wind
wind
-1
1
-0.99
0.01
1
NIL
HORIZONTAL

SLIDER
105
90
224
123
nb-human
nb-human
0
nb-teams
2
1
1
NIL
HORIZONTAL

TEXTBOX
5
173
155
191
PLAYER COMMANDS
14
0.0
1

@#$#@#$#@
## WHAT IS IT?

This file is a template for programming a Worms-like video game with the IODA approach.


## HOW IT WORKS

Please add here a short description.


## HOW TO USE IT

Please add here a small User Manual.


## GAMES FEATURES

Please add here your progression in the project goals:

* Base weapons

* Additional behaviors, timer, etc.

* Computer-controlled players

* Game extensions: additional features


## HERE YOUR OWN REMARKS

* Difficulties / bugs

* Solutions

* Conclusion



## HOW TO CITE

  * **The IODA methodology and simulation algorithms** (i.e. what is actually in use in this NetLogo extension):  
Y. KUBERA, P. MATHIEU and S. PICAULT (2011), "IODA: an interaction-oriented approach for multi-agent based simulations", in: _Journal of Autonomous Agents and Multi-Agent Systems (JAAMAS)_, vol. 23 (3), p. 303-343, Springer DOI: 10.1007/s10458-010-9164-z.  
  * The **key ideas** of the IODA methodology:  
P. MATHIEU and S. PICAULT (2005), "Towards an interaction-based design of behaviors", in: M.-P. Gleizes (ed.), _Proceedings of the The Third European Workshop on Multi-Agent Systems (EUMAS'2005)_.  
  * Do not forget to cite also **NetLogo** itself when you refer to the IODA NetLogo extension:  
U. WILENSKY (1999), NetLogo. http://ccl.northwestern.edu/netlogo Center for Connected Learning and Computer-Based Modeling, Northwestern University. Evanston, IL.

## COPYRIGHT NOTICE

All contents &copy; 2008-2014 Sébastien PICAULT and Philippe MATHIEU  
Laboratoire d'Informatique Fondamentale de Lille (LIFL), UMR CNRS 8022   
University Lille 1 -- Cité Scientifique, F-59655 Villeneuve d'Ascq Cedex, FRANCE.  
Web Site: http://www.lifl.fr/SMAC/projects/ioda

The IODA NetLogo extension is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

IODA NetLogo extension is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with the IODA NetLogo extension. If not, see http://www.gnu.org/licenses.
@#$#@#$#@
default
true
0
Polygon -7500403 true true 150 5 40 250 150 205 260 250

airplane
true
0
Polygon -7500403 true true 150 0 135 15 120 60 120 105 15 165 15 195 120 180 135 240 105 270 120 285 150 270 180 285 210 270 165 240 180 180 285 195 285 165 180 105 180 60 165 15

arrow
true
0
Polygon -7500403 true true 150 0 0 150 105 150 105 293 195 293 195 150 300 150

banana
false
0
Polygon -7500403 false true 25 78 29 86 30 95 27 103 17 122 12 151 18 181 39 211 61 234 96 247 155 259 203 257 243 245 275 229 288 205 284 192 260 188 249 187 214 187 188 188 181 189 144 189 122 183 107 175 89 158 69 126 56 95 50 83 38 68
Polygon -7500403 true true 39 69 26 77 30 88 29 103 17 124 12 152 18 179 34 205 60 233 99 249 155 260 196 259 237 248 272 230 289 205 284 194 264 190 244 188 221 188 185 191 170 191 145 190 123 186 108 178 87 157 68 126 59 103 52 88
Line -16777216 false 54 169 81 195
Line -16777216 false 75 193 82 199
Line -16777216 false 99 211 118 217
Line -16777216 false 241 211 254 210
Line -16777216 false 261 224 276 214
Polygon -16777216 true false 283 196 273 204 287 208
Polygon -16777216 true false 36 114 34 129 40 136
Polygon -16777216 true false 46 146 53 161 53 152
Line -16777216 false 65 132 82 162
Line -16777216 false 156 250 199 250
Polygon -16777216 true false 26 77 30 90 50 85 39 69

bottle
false
0
Circle -7500403 true true 90 240 60
Rectangle -1 true false 135 8 165 31
Line -7500403 true 123 30 175 30
Circle -7500403 true true 150 240 60
Rectangle -7500403 true true 90 105 210 270
Rectangle -7500403 true true 120 270 180 300
Circle -7500403 true true 90 45 120
Rectangle -7500403 true true 135 27 165 51

box
false
0
Polygon -7500403 true true 150 285 285 225 285 75 150 135
Polygon -7500403 true true 150 135 15 75 150 15 285 75
Polygon -7500403 true true 15 75 15 225 150 285 150 135
Line -16777216 false 150 285 150 135
Line -16777216 false 150 135 15 75
Line -16777216 false 150 135 285 75

bug
true
0
Circle -7500403 true true 96 182 108
Circle -7500403 true true 110 127 80
Circle -7500403 true true 110 75 80
Line -7500403 true 150 100 80 30
Line -7500403 true 150 100 220 30

butterfly
true
0
Polygon -7500403 true true 150 165 209 199 225 225 225 255 195 270 165 255 150 240
Polygon -7500403 true true 150 165 89 198 75 225 75 255 105 270 135 255 150 240
Polygon -7500403 true true 139 148 100 105 55 90 25 90 10 105 10 135 25 180 40 195 85 194 139 163
Polygon -7500403 true true 162 150 200 105 245 90 275 90 290 105 290 135 275 180 260 195 215 195 162 165
Polygon -16777216 true false 150 255 135 225 120 150 135 120 150 105 165 120 180 150 165 225
Circle -16777216 true false 135 90 30
Line -16777216 false 150 105 195 60
Line -16777216 false 150 105 105 60

cannon
true
0
Polygon -7500403 true true 165 0 165 15 180 150 195 165 195 180 180 195 165 225 135 225 120 195 105 180 105 165 120 150 135 15 135 0
Line -16777216 false 120 150 180 150
Line -16777216 false 120 195 180 195
Line -16777216 false 165 15 135 15
Polygon -16777216 false false 165 0 135 0 135 15 120 150 105 165 105 180 120 195 135 225 165 225 180 195 195 180 195 165 180 150 165 15

car
false
0
Polygon -7500403 true true 300 180 279 164 261 144 240 135 226 132 213 106 203 84 185 63 159 50 135 50 75 60 0 150 0 165 0 225 300 225 300 180
Circle -16777216 true false 180 180 90
Circle -16777216 true false 30 180 90
Polygon -16777216 true false 162 80 132 78 134 135 209 135 194 105 189 96 180 89
Circle -7500403 true true 47 195 58
Circle -7500403 true true 195 195 58

circle
false
0
Circle -7500403 true true 0 0 300

circle 2
false
0
Circle -7500403 true true 0 0 300
Circle -16777216 true false 30 30 240

cloud
false
0
Circle -7500403 true true 13 118 94
Circle -7500403 true true 86 101 127
Circle -7500403 true true 51 51 108
Circle -7500403 true true 118 43 95
Circle -7500403 true true 158 68 134

cow
false
0
Polygon -7500403 true true 200 193 197 249 179 249 177 196 166 187 140 189 93 191 78 179 72 211 49 209 48 181 37 149 25 120 25 89 45 72 103 84 179 75 198 76 252 64 272 81 293 103 285 121 255 121 242 118 224 167
Polygon -7500403 true true 73 210 86 251 62 249 48 208
Polygon -7500403 true true 25 114 16 195 9 204 23 213 25 200 39 123

crate
false
0
Rectangle -7500403 true true 45 45 255 255
Rectangle -16777216 false false 45 45 255 255
Rectangle -16777216 false false 60 60 240 240
Line -16777216 false 180 60 180 240
Line -16777216 false 150 60 150 240
Line -16777216 false 120 60 120 240
Line -16777216 false 210 60 210 240
Line -16777216 false 90 60 90 240
Polygon -7500403 true true 75 240 240 75 240 60 225 60 60 225 60 240
Polygon -16777216 false false 60 225 60 240 75 240 240 75 240 60 225 60

cylinder
false
0
Circle -7500403 true true 0 0 300

dot
false
0
Circle -7500403 true true 90 90 120

face happy
false
0
Circle -7500403 true true 8 8 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Polygon -16777216 true false 150 255 90 239 62 213 47 191 67 179 90 203 109 218 150 225 192 218 210 203 227 181 251 194 236 217 212 240

face neutral
false
0
Circle -7500403 true true 8 7 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Rectangle -16777216 true false 60 195 240 225

face sad
false
0
Circle -7500403 true true 8 8 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Polygon -16777216 true false 150 168 90 184 62 210 47 232 67 244 90 220 109 205 150 198 192 205 210 220 227 242 251 229 236 206 212 183

fire
false
0
Polygon -7500403 true true 151 286 134 282 103 282 59 248 40 210 32 157 37 108 68 146 71 109 83 72 111 27 127 55 148 11 167 41 180 112 195 57 217 91 226 126 227 203 256 156 256 201 238 263 213 278 183 281
Polygon -955883 true false 126 284 91 251 85 212 91 168 103 132 118 153 125 181 135 141 151 96 185 161 195 203 193 253 164 286
Polygon -2674135 true false 155 284 172 268 172 243 162 224 148 201 130 233 131 260 135 282

fish
false
0
Polygon -1 true false 44 131 21 87 15 86 0 120 15 150 0 180 13 214 20 212 45 166
Polygon -1 true false 135 195 119 235 95 218 76 210 46 204 60 165
Polygon -1 true false 75 45 83 77 71 103 86 114 166 78 135 60
Polygon -7500403 true true 30 136 151 77 226 81 280 119 292 146 292 160 287 170 270 195 195 210 151 212 30 166
Circle -16777216 true false 215 106 30

fish left
false
0
Polygon -1 true false 256 131 279 87 285 86 300 120 285 150 300 180 287 214 280 212 255 166
Polygon -1 true false 165 195 181 235 205 218 224 210 254 204 240 165
Polygon -1 true false 225 45 217 77 229 103 214 114 134 78 165 60
Polygon -7500403 true true 270 136 149 77 74 81 20 119 8 146 8 160 13 170 30 195 105 210 149 212 270 166
Circle -16777216 true false 55 106 30

fish right
false
0
Polygon -1 true false 44 131 21 87 15 86 0 120 15 150 0 180 13 214 20 212 45 166
Polygon -1 true false 135 195 119 235 95 218 76 210 46 204 60 165
Polygon -1 true false 75 45 83 77 71 103 86 114 166 78 135 60
Polygon -7500403 true true 30 136 151 77 226 81 280 119 292 146 292 160 287 170 270 195 195 210 151 212 30 166
Circle -16777216 true false 215 106 30

flag
false
0
Rectangle -7500403 true true 60 15 75 300
Polygon -7500403 true true 90 150 270 90 90 30
Line -7500403 true 75 135 90 135
Line -7500403 true 75 45 90 45

flower
false
0
Polygon -10899396 true false 135 120 165 165 180 210 180 240 150 300 165 300 195 240 195 195 165 135
Circle -7500403 true true 85 132 38
Circle -7500403 true true 130 147 38
Circle -7500403 true true 192 85 38
Circle -7500403 true true 85 40 38
Circle -7500403 true true 177 40 38
Circle -7500403 true true 177 132 38
Circle -7500403 true true 70 85 38
Circle -7500403 true true 130 25 38
Circle -7500403 true true 96 51 108
Circle -16777216 true false 113 68 74
Polygon -10899396 true false 189 233 219 188 249 173 279 188 234 218
Polygon -10899396 true false 180 255 150 210 105 210 75 240 135 240

ghost
false
0
Polygon -7500403 true true 30 165 13 164 -2 149 0 135 -2 119 0 105 15 75 30 75 58 104 43 119 43 134 58 134 73 134 88 104 73 44 78 14 103 -1 193 -1 223 29 208 89 208 119 238 134 253 119 240 105 238 89 240 75 255 60 270 60 283 74 300 90 298 104 298 119 300 135 285 135 285 150 268 164 238 179 208 164 208 194 238 209 253 224 268 239 268 269 238 299 178 299 148 284 103 269 58 284 43 299 58 269 103 254 148 254 193 254 163 239 118 209 88 179 73 179 58 164
Line -16777216 false 189 253 215 253
Circle -16777216 true false 102 30 30
Polygon -16777216 true false 165 105 135 105 120 120 105 105 135 75 165 75 195 105 180 120
Circle -16777216 true false 160 30 30

grenade
true
0
Polygon -6459832 true false 150 75 120 75 90 105 75 150 75 180 90 225 120 255 180 255 210 225 225 180 225 150 210 105 180 75
Circle -6459832 false false 136 24 67
Line -7500403 true 209 107 75 151
Line -7500403 true 223 149 81 196
Line -7500403 true 217 198 100 236
Line -7500403 true 83 198 200 236
Line -7500403 true 77 149 219 196
Line -7500403 true 91 107 225 151
Polygon -7500403 false true 120 75 90 105 75 150 75 180 90 225 120 255 180 255 210 225 225 180 225 150 210 105 180 75
Circle -7500403 false true 131 26 67

house
false
0
Rectangle -7500403 true true 45 120 255 285
Rectangle -16777216 true false 120 210 180 285
Polygon -7500403 true true 15 120 150 15 285 120
Line -16777216 false 30 120 270 120

leaf
false
0
Polygon -7500403 true true 150 210 135 195 120 210 60 210 30 195 60 180 60 165 15 135 30 120 15 105 40 104 45 90 60 90 90 105 105 120 120 120 105 60 120 60 135 30 150 15 165 30 180 60 195 60 180 120 195 120 210 105 240 90 255 90 263 104 285 105 270 120 285 135 240 165 240 180 270 195 240 210 180 210 165 195
Polygon -7500403 true true 135 195 135 240 120 255 105 255 105 285 135 285 165 240 165 195

line
true
0
Line -7500403 true 150 0 150 300

line half
true
0
Line -7500403 true 150 0 150 150

mine
false
0
Polygon -6459832 true false 30 300 30 255 60 240 105 240 105 255 195 255 195 240 240 240 270 255 270 300
Rectangle -7500403 true true 105 210 195 255

pentagon
false
0
Polygon -7500403 true true 150 15 15 120 60 285 240 285 285 120

person
false
0
Circle -7500403 true true 110 5 80
Polygon -7500403 true true 105 90 120 195 90 285 105 300 135 300 150 225 165 300 195 300 210 285 180 195 195 90
Rectangle -7500403 true true 127 79 172 94
Polygon -7500403 true true 195 90 240 150 225 180 165 105
Polygon -7500403 true true 105 90 60 150 75 180 135 105

plant
false
0
Rectangle -7500403 true true 135 90 165 300
Polygon -7500403 true true 135 255 90 210 45 195 75 255 135 285
Polygon -7500403 true true 165 255 210 210 255 195 225 255 165 285
Polygon -7500403 true true 135 180 90 135 45 120 75 180 135 210
Polygon -7500403 true true 165 180 165 210 225 180 255 120 210 135
Polygon -7500403 true true 135 105 90 60 45 45 75 105 135 135
Polygon -7500403 true true 165 105 165 135 225 105 255 45 210 60
Polygon -7500403 true true 135 90 120 45 150 15 180 45 165 90

rocket
true
0
Polygon -7500403 true true 120 165 75 285 135 255 165 255 225 285 180 165
Polygon -1 true false 135 285 105 135 105 105 120 45 135 15 150 0 165 15 180 45 195 105 195 135 165 285
Rectangle -7500403 true true 147 176 153 288
Polygon -7500403 true true 120 45 180 45 165 15 150 0 135 15
Line -7500403 true 105 105 135 120
Line -7500403 true 135 120 165 120
Line -7500403 true 165 120 195 105
Line -7500403 true 105 135 135 150
Line -7500403 true 135 150 165 150
Line -7500403 true 165 150 195 135

sheep
false
0
Rectangle -7500403 true true 151 225 180 285
Rectangle -7500403 true true 47 225 75 285
Rectangle -7500403 true true 15 75 210 225
Circle -7500403 true true 135 75 150
Circle -16777216 true false 165 76 116

small target
false
0
Circle -7500403 false true 83 83 134
Circle -7500403 false true 42 42 216
Circle -7500403 true true 135 135 30
Rectangle -7500403 true true 142 19 157 109
Rectangle -7500403 true true 19 143 109 158
Rectangle -7500403 true true 191 143 281 158
Rectangle -7500403 true true 142 191 157 281

square
false
0
Rectangle -7500403 true true 30 30 270 270

square 2
false
0
Rectangle -7500403 true true 30 30 270 270
Rectangle -16777216 true false 60 60 240 240

star
false
0
Polygon -7500403 true true 151 1 185 108 298 108 207 175 242 282 151 216 59 282 94 175 3 108 116 108

target
false
0
Circle -7500403 true true 0 0 300
Circle -16777216 true false 30 30 240
Circle -7500403 true true 60 60 180
Circle -16777216 true false 90 90 120
Circle -7500403 true true 120 120 60

tile brick
false
0
Rectangle -1 true false 0 0 300 300
Rectangle -7500403 true true 15 225 150 285
Rectangle -7500403 true true 165 225 300 285
Rectangle -7500403 true true 75 150 210 210
Rectangle -7500403 true true 0 150 60 210
Rectangle -7500403 true true 225 150 300 210
Rectangle -7500403 true true 165 75 300 135
Rectangle -7500403 true true 15 75 150 135
Rectangle -7500403 true true 0 0 60 60
Rectangle -7500403 true true 225 0 300 60
Rectangle -7500403 true true 75 0 210 60

tile log
false
0
Rectangle -7500403 true true 0 0 300 300
Line -16777216 false 0 30 45 15
Line -16777216 false 45 15 120 30
Line -16777216 false 120 30 180 45
Line -16777216 false 180 45 225 45
Line -16777216 false 225 45 165 60
Line -16777216 false 165 60 120 75
Line -16777216 false 120 75 30 60
Line -16777216 false 30 60 0 60
Line -16777216 false 300 30 270 45
Line -16777216 false 270 45 255 60
Line -16777216 false 255 60 300 60
Polygon -16777216 false false 15 120 90 90 136 95 210 75 270 90 300 120 270 150 195 165 150 150 60 150 30 135
Polygon -16777216 false false 63 134 166 135 230 142 270 120 210 105 116 120 88 122
Polygon -16777216 false false 22 45 84 53 144 49 50 31
Line -16777216 false 0 180 15 180
Line -16777216 false 15 180 105 195
Line -16777216 false 105 195 180 195
Line -16777216 false 225 210 165 225
Line -16777216 false 165 225 60 225
Line -16777216 false 60 225 0 210
Line -16777216 false 300 180 264 191
Line -16777216 false 255 225 300 210
Line -16777216 false 16 196 116 211
Line -16777216 false 180 300 105 285
Line -16777216 false 135 255 240 240
Line -16777216 false 240 240 300 255
Line -16777216 false 135 255 105 285
Line -16777216 false 180 0 240 15
Line -16777216 false 240 15 300 0
Line -16777216 false 0 300 45 285
Line -16777216 false 45 285 45 270
Line -16777216 false 45 270 0 255
Polygon -16777216 false false 150 270 225 300 300 285 228 264
Line -16777216 false 223 209 255 225
Line -16777216 false 179 196 227 183
Line -16777216 false 228 183 266 192

tree
false
0
Circle -7500403 true true 118 3 94
Rectangle -6459832 true false 120 195 180 300
Circle -7500403 true true 65 21 108
Circle -7500403 true true 116 41 127
Circle -7500403 true true 45 90 120
Circle -7500403 true true 104 74 152

triangle
false
0
Polygon -7500403 true true 150 30 15 255 285 255

triangle 2
false
0
Polygon -7500403 true true 150 30 15 255 285 255
Polygon -16777216 true false 151 99 225 223 75 224

truck
false
0
Rectangle -7500403 true true 4 45 195 187
Polygon -7500403 true true 296 193 296 150 259 134 244 104 208 104 207 194
Rectangle -1 true false 195 60 195 105
Polygon -16777216 true false 238 112 252 141 219 141 218 112
Circle -16777216 true false 234 174 42
Rectangle -7500403 true true 181 185 214 194
Circle -16777216 true false 144 174 42
Circle -16777216 true false 24 174 42
Circle -7500403 false true 24 174 42
Circle -7500403 false true 144 174 42
Circle -7500403 false true 234 174 42

turtle
true
0
Polygon -10899396 true false 215 204 240 233 246 254 228 266 215 252 193 210
Polygon -10899396 true false 195 90 225 75 245 75 260 89 269 108 261 124 240 105 225 105 210 105
Polygon -10899396 true false 105 90 75 75 55 75 40 89 31 108 39 124 60 105 75 105 90 105
Polygon -10899396 true false 132 85 134 64 107 51 108 17 150 2 192 18 192 52 169 65 172 87
Polygon -10899396 true false 85 204 60 233 54 254 72 266 85 252 107 210
Polygon -7500403 true true 119 75 179 75 209 101 224 135 220 225 175 261 128 261 81 224 74 135 88 99

void
true
0

wheel
false
0
Circle -7500403 true true 3 3 294
Circle -16777216 true false 30 30 240
Line -7500403 true 150 285 150 15
Line -7500403 true 15 150 285 150
Circle -7500403 true true 120 120 60
Line -7500403 true 216 40 79 269
Line -7500403 true 40 84 269 221
Line -7500403 true 40 216 269 79
Line -7500403 true 84 40 221 269

worm left
false
0
Polygon -7500403 true true 270 165 287 164 302 149 300 135 302 119 300 105 285 75 270 75 242 104 257 119 257 134 242 134 227 134 212 104 227 44 222 14 197 -1 107 -1 75 15 60 45 60 60 75 90 105 105 150 90 150 105 120 120 90 120 75 150 45 150 30 120 2 104 2 119 0 135 15 150 15 165 15 180 45 195 90 180 92 194 62 209 47 224 32 239 32 269 62 299 122 299 152 284 197 269 242 284 257 299 242 269 197 254 152 254 107 254 137 239 182 209 212 179 227 179 242 164
Line -16777216 false 111 253 85 253
Circle -1 true false 55 20 50
Circle -16777216 true false 53 36 28
Line -16777216 false 87 13 126 29

worm right
false
0
Polygon -7500403 true true 30 165 13 164 -2 149 0 135 -2 119 0 105 15 75 30 75 58 104 43 119 43 134 58 134 73 134 88 104 73 44 78 14 103 -1 193 -1 225 15 240 45 240 60 225 90 195 105 150 90 150 105 180 120 210 120 225 150 255 150 270 120 298 104 298 119 300 135 285 150 285 165 285 180 255 195 210 180 208 194 238 209 253 224 268 239 268 269 238 299 178 299 148 284 103 269 58 284 43 299 58 269 103 254 148 254 193 254 163 239 118 209 88 179 73 179 58 164
Line -16777216 false 189 253 215 253
Circle -1 true false 195 20 50
Circle -16777216 true false 219 36 28
Line -16777216 false 213 13 174 29

x
false
0
Polygon -7500403 true true 270 75 225 30 30 225 75 270
Polygon -7500403 true true 30 75 75 30 270 225 225 270

@#$#@#$#@
NetLogo 5.1.0
@#$#@#$#@
@#$#@#$#@
@#$#@#$#@
@#$#@#$#@
@#$#@#$#@
default
0.0
-0.2 0 0.0 1.0
0.0 1 1.0 0.0
0.2 0 0.0 1.0
link direction
true
0
Line -7500403 true 150 150 90 180
Line -7500403 true 150 150 210 180

@#$#@#$#@
0
@#$#@#$#@
