s" homade.out" w/o create-file throw value mybin
s" homade_slave.out" w/o create-file throw value mybinslave
variable HMD_debut 
variable HMD_wim1
variable HMD_wim2
variable HMD_wim3
variable HMD_assoc
variable HMD_nextc
4 HMD_nextc !




( rename mots forth utilisés par homade )

: Fdup dup ;

: Fswap swap
;
: Fdrop drop 
;	  
: Ftuck tuck 
;
: Fover over 
;	 
: Frot rot 
; 

: Fnip nip
;

: Fand    and
;
: For    or 
;
: Fxor     xor
;

hex
: Binary fdup   ff00 fand 8 rshift c, 00ff fand c,

;

: Null 	   ffff binary
;

: NBnull HMD_debut @ - 2/ 3 fand 4 swap -  fdup 4 =  if fdrop 0 endif 
	fdup 1 >= if NULL endif 
	 1 - fdup 1  >= if Null endif 
	 1 - 1 >= if Null endif 
   ;
( slave )
: get 8A00 binary ;
: put A201 binary ;
: num BA03 binary ;
( master )
: com32 85FF binary ;
: com 81FF binary ;
: on A1FE binary ;
: on64 C1FE binary ;
: M2E A013 binary ;
: E2M 8812 binary ;

:  push1    8800 binary
;
:  push2    9000 binary
;
:  push3    9800 binary
;
:  pop1    A000 binary
;
:  pop2    C000 binary
;
:  pop3    E000 binary
;
:  Tic	    8801 binary
;
:  TicRaz    8001 binary
;
:  BufOut	    A002 binary
;
:  BufOutDup	    8002 binary
;
:  Led		A003 binary
;
:  LedDup	    8003 binary
;
:  Switch	    8804 binary
;


: Nop      8000 binary
;
: Hlt      1C00 binary
;
: DataPop  8806 binary 
;
: DataPush A007 binary
;
: DataPushDup 8007 binary
;	  
: RegLd  A811 binary 
;
: RegSt C010 binary
;
: RegStDup A010 binary
;	 
: Delay  A401 binary 
;
: WaitBtn A402 binary 
;
: WaitBtnpush AC02 binary 
;
: fibo AC03 binary
;
: dup B008 binary 
;
: swap d009 binary 
;
: drop pop1 
;	  
: tuck d80A binary 
;
: over d80b binary 
;	 
: rot f80C binary 
; 
: invrot f80d binary 
; 
: nip c80e binary 
;

: nip3 F005 binary
;
: nip23 E805 binary
;
: dup2 9005 binary
;
: dup3 9805 binary
;


: add     C820 binary 
;
: minus C821 binary 
;
: inc A822 binary 
;
: dec A823 binary 
;
: Hnot A824 binary 
;
: not A824 binary 
;
: and     C825 binary 
;
: or     C826 binary 
;
: xor     C827 binary 
;
: 2*     A828 binary 
;
: Hu2/     A829 binary 
;
: H2/     A82A binary 
;
: H->     C82B binary 
;
: H<-     C82C binary 
;
: Comp2		A82E binary
;
: Vrai     8830 binary 
;
: Faux     8831 binary 
;
: Ez     A832 binary 
;
: Neg     A833 binary 
;
: GtU     C834 binary 
;
: LtU     C835 binary 
;
: Eq     C836 binary 
;
: GeU     C837 binary 
;
: LeU     C838 binary 
;
: Ne     C839 binary 
;
: Gt     C83A binary 
;
: Lt     C83B binary 
;
: Le     C83C binary 
;
: Ge     C83D binary 
;
: ShiftLit C83E binary 
;

: return   1400 binary  
;

: function here NBnull here HMD_debut @ - 2/ fswap !
;
: wait 6000 binary 
;
: spmd ( a)  @ 2 rshift 1FFF fand 4000 for fdup 8 rshift c, c, 
;
: call 
( add null before )
here NBnull

1000 binary @	 fdup fdup fdup ff000000 fand  18 rshift c, FF0000  fand 10 rshift  c,
FF00 fand 8 rshift    c, FF fand  c,
		 
;
: fcall @ HMD_debut  @  - call 
;

: Lit ( a) FFF fand 2000 for fdup 8 rshift c, c, 
;
( extension litteral )
: w32 fdup fff fand Lit fdup FFF000 fand c rshift Lit FF000000 fand  18 rshift Lit shiftlit shiftlit 
;
: w16 fdup fff fand Lit  F000 fand c rshift Lit  shiftlit 
;
: w8 ff fand Lit 
;


: Dynamic ( a ) 
here 8 - @ HMD_WIM1 ! here 4 - @ HMD_WIM2 ! 

 -6 allot
here NBnull
@  2 rshift fff fand 3000 for fdup  8 rshift c, c, 

 HMD_WIM1 @ fdup    10 rshift ff fand c, 18 rshift ff fand c,  HMD_WIM2 @  fdup fdup fdup c, 8 rshift c, 10 rshift c, 18 rshift c,  

;
: WIM dynamic ;
: static ( a ) 
here 8 - @ HMD_WIM1 ! here 4 - @ HMD_WIM2 ! 

 @  1 lshift    HMD_debut @   +    hmd_assoc  ! 

-6 allot
 
HMD_WIM1 @     10 rshift ffff fand   hmd_assoc @ !

HMD_WIM2 @     ffff fand hmd_assoc @ 2 +   !          

 HMD_WIM2 @ 10 rshift ffff fand hmd_assoc @ 4 + @ FFFF0000 fand    for hmd_assoc @ 4 +  ! 

 ;

: pc HMD_nextc @ fswap ! HMD_nextc @ 4 + HMD_nextc !
hlt 0000 binary 0000 binary hlt
;

: BZ ( a) 3FF fand 0400 for fdup 8 rshift c, c, 
;
: BNZ ( a) 3FF fand 0800 for fdup 8 rshift c, c, 
;
: BR ( a) 3FF fand 0000 for fdup 8 rshift c, c, 
;
: BRA ( a b )  
here NBnull
00 0c c, c, fdup 10 rshift fdup 8 rshift c, c, fdup 8 rshift c, c,  	  
;

: VC HMD_nextc @ fswap !
0 BRA RETURN
HMD_nextc @ 4 + fdup . HMD_nextc !
;

: PR4hopen 0 <<#    # # # # 
[char] " hold [char] x hold  #> Type #>> 
;
: PR4hmiddle 0 <<#    # # # #  
  #> Type #>> 
;
: PR4hclose 0 <<#  [char] - hold [char] - hold [char] , hold [char] " hold  # # # # [char] _ hold
  #> Type #>> 
;
: PR4hcloselast 0 <<#  [char] - hold [char] - hold [char] " hold  # # # # [char] _ hold
  #> Type #>> 
;


: program 
hex
here    HMD_debut  @ - 2/  fdup ff000000 fand 18 rshift c, fdup 00FF0000 fand 10 rshift c, fdup 0000ff00 fand 8 rshift c, 000000ff fand c, 

here 4 mybinslave write-file
mybinslave close-file
clearstacks hex here HMD_debut !  3 BRA 
here NBnull
;
: slave clearstacks hex here HMD_debut ! 1000 binary 0000 binary 0000 binary hlt
here NBnull
;
: master 
4 HMD_nextc !
return
here NBnull

hex
here    HMD_debut  @ - 2/  fdup ff000000 fand 18 rshift c, fdup 00FF0000 fand 10 rshift c, fdup 0000ff00 fand 8 rshift c, 000000ff fand c, 

here  4 - 4 mybinslave write-file



 here  8 -  HMD_debut @   
+do  

	  i 8 mybinslave write-file



i HMD_debut  @ - 2/  8
+loop 


mybinslave close-file
hex here HMD_debut !  3 BRA 
here NBnull
;
: begin here NBnull here HMD_debut @ - 2/ fdup fdup fdup ff fand HMD_debut @  5 + c! FF00  fand 8 rshift HMD_debut @ 4 + c!
FF0000 fand 10 rshift HMD_debut @ 3  + c! FF000000 fand 18 rshift HMD_debut @ 2 + c!
;
: endprogram  
hlt
here NBnull

hex
here    HMD_debut  @ - 2/  fdup ff000000 fand 18 rshift c, fdup 00FF0000 fand 10 rshift c, fdup 0000ff00 fand 8 rshift c, 000000ff fand c, 

here  4 - 4 mybin write-file



 here  8 -  HMD_debut @   
+do  

	  i 8 mybin write-file



i HMD_debut  @ - 2/  8
+loop 


mybin close-file


; 


( structure )

: repeat here ( debut de boucle ) 
;
: until here - 2/ BNZ ( fin de boucle  test du sommet de pile  avec conso )
;
: again here - 2/ BR  ( fin de boucle infinie )
;
: if here ff 4 c, c,   ( debut du if  conso du sommet de pile pour test) 
;
: endif  here NBnull fdup here  fswap - 2/ fswap fdup c@ 8 lshift frot 03FF fand for 
fdup 8 lshift  FF00 fand fswap 8 rshift FF fand for fswap w!  ( fin du if )
;
\ : else  here fdup . NBnull fdup here  fdup . fswap - 2/ 1 +  fdup . fswap   fdup c@ 8 lshift frot 03FF fand for 
 \ fdup 8 lshift  FF00 fand fswap 8 rshift FF fand for fswap w!  here FF 0 c, c,  ( if ... ELSE ... endif   else optionel )
\ ;
: else  here FF 0 c, c, fswap here  NBnull fdup here  fswap - 2/    fswap   fdup c@ 8 lshift frot 03FF fand for 
 fdup 8 lshift  FF00 fand fswap 8 rshift FF fand for fswap fdup . w!    ( if ... ELSE ... endif   else optionel )
;
( <end_val>   Do ... LOOP ....  [end_val.. 1] )
: DO  here  DataPush  
;
: loop DataPop Dec Dup here - 2/ BNZ pop1
; 
: index DataPop DataPushDup ( index of the inner loop )
;
