
variable read
variable print

slave

begin

master

read function
	f lit
	waitbtn
	switch
	leddup
return

print function
    bufoutdup
return

begin
	repeat
		read call
		8 lit
		H<-
		read call
		or
		read call
		8 lit
		H<-
		read call
		or	
		add	
		print call
	again
endprogram

