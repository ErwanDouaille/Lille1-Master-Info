
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
		TicRaz
		8 lit
		H<-
		Tic
		DataPush

		read call
		TicRaz
		or
		Tic
		DataPush

		read call
		TicRaz		
		8 lit
		H<-
		Tic
		DataPush

		read call
		TicRaz		
		or	
		add	
		Tic
		DataPush

		f lit
		waitbtn
		print call

		
	

		DataPop
		DataPop
		add 
		DataPop
		add 
		DataPop
		add 
		f lit
		waitbtn
		print call
				

	again
endprogram

