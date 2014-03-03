( test led)

slave 


begin 

master 

begin	
001 lit
repeat 
	leddup
	fffff w32
	delay
	2*
	dup 100 lit eq
	if 
		pop1 001 lit
	endif
again
endprogram
