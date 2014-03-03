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
    bufout
return


begin
    repeat

    A lit
    do
        read call
    loop

    0 lit
    0 lit
   
    A lit
    do
        rot
        7F lit
        LeU
        if
            inc
        else
            swap
            inc
            swap
        endif
    loop

    f lit
    waitbtn
   
    print call

    f lit
    waitbtn
    print call

    again
endprogram
