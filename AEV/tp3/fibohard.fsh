
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
		1 lit 
		1 lit
		read call
		ticraz

		fibo

		tic 
		datapush
		print call		
		f lit
		waitbtn
		datapop
		print call
	again
endprogram

