( test led)

slave 


begin 

master 

begin
repeat
	01f lit
	waitbtn
	switch
	01f lit
	waitbtn
	switch
	add
	bufout
again
endprogram

