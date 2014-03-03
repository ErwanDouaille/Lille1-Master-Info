----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : hstack
-- Project Name :  Homade V2.1
-- Revision :      no
--                                         
-- Target Device :     spartan 6 spartan 3
-- Tool Version : tested on ISE 12.4,
--                                                   
-- Description :  Stack Association unit
-- 
-- 
-- Contributor(s) :
-- Dekeyser Jean-Luc ( Creation  juin 2012) jean-luc.dekeyser@lifl.fr
-- Wissem Chouchene ( revision 001,  Ocotbre 2012) wissem.chouchene@inria.fr
-- 
-- 
-- Cecil Licence:
-- This software is a computer program whose purpose is to Implement the
-- Homade processor on Xilinx FPGA systems.
-- 
-- This software is governed by the CeCILL license under French law and
-- abiding by the rules of distribution of free software.  You can  use,
-- modify and/ or redistribute the software under the terms of the CeCILL
-- license as circulated by CEA, CNRS and INRIA at the following URL
-- "http://www.cecill.info".
-- 
-- As a counterpart to the access to the source code and  rights to copy,
-- modify and redistribute granted by the license, users are provided only
-- with a limited warranty  and the software's author,  the holder of the
-- economic rights,  and the successive licensors  have only  limited
-- liability.
--                                                                                          
-- In this respect, the user's attention is drawn to the risks associated
-- with loading,  using,  modifying and/or developing or reproducing the
-- software by the user in light of its specific status of free software,
-- that may mean  that it is complicated to manipulate,  and  that  also
-- therefore means  that it is reserved for developers  and  experienced
-- professionals having in-depth computer knowledge. Users are therefore
-- encouraged to load and test the software's suitability as regards their                                                                           
-- requirements in conditions enabling the security of their systems and/or
-- data to be ensured and,  more generally, to use and operate it in the
-- same conditions as regards security.
-- 
-- The fact that you are presently reading this means that you have had
-- knowledge of the CeCILL license and that you accept its terms.   
----------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.all;
use IEEE.STD_LOGIC_unsigned.all;

entity hstack is
    port (
        clr: in STD_LOGIC;
        clk: in STD_LOGIC;  
---  nuber push pop for the memory
        push1: in STD_LOGIC;
        push2: in STD_LOGIC;
        push3: in STD_LOGIC;
        pop1: in STD_LOGIC;  
        pop2: in STD_LOGIC; 
        pop3: in STD_LOGIC;       
--mux select
        Tsel: in std_logic_vector(2 downto 0);                 
        Nsel: in std_logic_vector(2 downto 0);     
        N2sel: in std_logic_vector(2 downto 0);   
        ramsel: in std_logic_vector(1 downto 0);    
        ram1sel: in std_logic_vector(1 downto 0);    
    
--load reg fro input bus
		LITload: in std_logic;   
		Tload: in std_logic;   		
		Nload: in std_logic;   
		N2load: in std_logic;      
--in bus fro RI
		Tlit:in std_logic_vector(31 downto 0);
--in bus fro IP
		Tin: in std_logic_vector(31 downto 0);  
		Nin: in std_logic_vector(31 downto 0);  
		N2in: in std_logic_vector(31 downto 0);                                                                  
--out bus  to IP
		Tout: out std_logic_vector(31 downto 0);  
		Nout: out std_logic_vector(31 downto 0);  
		N2out: out std_logic_vector(31 downto 0);                                
-- IP finsish
		IPdone:in std_logic

-- flag if needed
--        full: out STD_LOGIC;
--        empty: out STD_LOGIC
    ); 
	attribute clock_signal : string;
	attribute clock_signal of clk : signal is "yes";
end hstack;
                                 
architecture hstack_arch of hstack is
signal regload, Tl,Nl,N2l, raml, pop: STD_LOGIC;
signal Tbus,Nbus,N2bus,rambus,ram1bus,ram2bus, XLXN_1,XLXN_2,XLXN_3,XLXN_4,XLXN_5,XLXN_6, Tbp, Nbp,N2bp: std_logic_vector(31 downto 0);


	attribute KEEP : string;
	attribute KEEP of XLXN_1 : signal is "yes";
	attribute KEEP of Tbus : signal is "yes";
	attribute KEEP of Tl : signal is "yes";


	
	COMPONENT reg0
   generic (N : integer := 32);
	 port(
		 load : in STD_LOGIC;
		 d : in STD_LOGIC_VECTOR(N-1 downto 0);
		 clk : in STD_LOGIC;
		 clr : in STD_LOGIC;
		 q : out STD_LOGIC_VECTOR(N-1 downto 0)
	     );
	END COMPONENT;

	COMPONENT reg1
	PORT(
		load : IN std_logic;
		d : IN std_logic_vector(31 downto 0);
		clr ,clk: IN std_logic;          
		q : OUT std_logic_vector(31 downto 0)
		);
	END COMPONENT;
	
   component mux6                                              
      port ( sel : in    std_logic_vector (2 downto 0); 
             in0sel       : in    std_logic_vector (31 downto 0); 
             in1sel       : in    std_logic_vector (31 downto 0); 
             in2sel       : in    std_logic_vector (31 downto 0); 
             in3sel      : in    std_logic_vector (31 downto 0); 
             in4sel      : in    std_logic_vector (31 downto 0);                                                                  
             in5sel      : in    std_logic_vector (31 downto 0); 
             outsel     : out   std_logic_vector (31 downto 0));
   end component;   

component fifo3port                                              
      port ( 
             clk  : in    std_logic; 
             clr  : in    std_logic; 
			    ramload,  rampop,pop1,pop2,pop3,push1,push2,push3 : in    std_logic; 
             ramin       : in    std_logic_vector (31 downto 0); 
             ram1in       : in    std_logic_vector (31 downto 0); 
             ram2in       : in    std_logic_vector (31 downto 0); 
             ramout      : out    std_logic_vector (31 downto 0); 
             ram1out      : out    std_logic_vector (31 downto 0);                                                                  
             ram2out      : out    std_logic_vector (31 downto 0)

				);
   end component;

   component mux3                                              
      port ( sel : in    std_logic_vector (1 downto 0); 
             in0sel       : in    std_logic_vector (31 downto 0); 
             in1sel       : in    std_logic_vector (31 downto 0); 
             in2sel       : in    std_logic_vector (31 downto 0); 
             outsel     : out   std_logic_vector (31 downto 0));
   end component;	                 
               
				

begin
               
      Tout <=  Tbus;
		Nout <= Nbus;
		N2out <= N2bus;   


ram3p:  fifo3port                                             
      port map( clk=>clk,
                clr=>clr,
					 ramload => raml,
				rampop => pop,
		     	pop1=>pop1,
				pop2=>pop2,
				pop3=>pop3,
				push1=>push1,
				push2=>push2,
				push3=>push3,
             ramin (31 downto 0)=>Tbp(31 downto 0),
             ram1in (31 downto 0)=>Nbp(31 downto 0),
             ram2in (31 downto 0)=>N2bp(31 downto 0),
             ramout  (31 downto 0)=>rambus(31 downto 0),
             ram1out (31 downto 0)=>ram1bus(31 downto 0),                                                    
             ram2out  (31 downto 0)=>ram2bus(31 downto 0));

				regload <= Ipdone or LITload;
				Tl <=(Tload and regload) ;
      
    					 

	Tmux : mux6
		port map (sel =>Tsel,
             in0sel(31 downto 0)=>Tin(31 downto 0),
             in1sel(31 downto 0)=>Nbus(31 downto 0), 
             in2sel(31 downto 0)=>N2bus(31 downto 0),
             in3sel(31 downto 0)=>rambus(31 downto 0), 
             in4sel(31 downto 0)=>Tlit,          


             in5sel(31 downto 0)=>x"00000000",
             outsel(31 downto 0)=>XLXN_1(31 downto 0)); 
				 
	Nl <= (Nload and regload) ;
      
   Nlatch : reg0              
      port map (
                d(31 downto 0)=>XLXN_2(31 downto 0),
                load=>Nl,
					 clk => clk,
					 clr => clr,
                q(31 downto 0)=>Nbus(31 downto 0));   
   Nprim : reg1              
      port map (
                d(31 downto 0)=>Nbus(31 downto 0),
                load=>'1',
					 clk => clk,
					 clr => clr,
                q(31 downto 0)=>Nbp(31 downto 0));   					 
	Nmux : mux6
		port map (sel =>Nsel,
             in0sel(31 downto 0)=>Nin(31 downto 0), 
             in1sel(31 downto 0)=>N2bus(31 downto 0), 
             in2sel(31 downto 0)=>rambus(31 downto 0),         
             in3sel(31 downto 0)=>ram1bus(31 downto 0),
             in4sel(31 downto 0)=>Tbus(31 downto 0),  

             in5sel(31 downto 0)=>x"00000000",
             outsel(31 downto 0)=>XLXN_2(31 downto 0));                                                               

	N2l<= (N2load and regload) ;

	raml<= (push1 and IPdone) or (push1 and LITload) or (push2 and IPdone) or (push2 and LITload) or (push3 and IPdone) or (push3 and LITload);

 
	N2latch : reg0
      port map (
                d(31 downto 0)=>XLXN_3(31 downto 0),
                load=>N2l,
					 clk => clk,
					 clr => clr,
                q(31 downto 0)=>N2bus(31 downto 0));   
					 
	N2prim : reg1
      port map (
                d(31 downto 0)=>N2bus(31 downto 0),
                load=>N2l,
					 clk => clk,
					 clr => clr,
                q(31 downto 0)=>N2bp(31 downto 0));  

	N2mux : mux6
		port map (sel =>N2sel, 
             in0sel(31 downto 0)=>N2in(31 downto 0), 
             in1sel(31 downto 0)=>rambus(31 downto 0),       
             in2sel(31 downto 0)=>ram1bus(31 downto 0), 
             in3sel(31 downto 0)=>ram2bus(31 downto 0), 
             in4sel(31 downto 0)=>Nbus(31 downto 0),                                                                  
             in5sel(31 downto 0)=>Tbus(31 downto 0), 
             outsel(31 downto 0)=>XLXN_3(31 downto 0));          

 
XLXN_6(31 downto 0) <=  Tbp(31 downto 0);


pop<= (pop1 and IPdone) or (pop1 and LITload) or (pop2 and IPdone) or (pop2 and LITload) or (pop3 and IPdone) or (pop3 and LITload);


----------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------- New Tlatch ---------------------------------------------------------
----------------------------------------------------------------------------------------------------------------------------------
--=============================================

  Tlatch_1_0_0_0 : reg0
  	generic map (N=>2)
      port map (
                d=>XLXN_1(1 downto 0),
                load=>Tl,
					 clk=>clk,
					 clr => clr,
                q=>Tbus(1 downto 0)); 
------------------------------------------------	x2				 
  Tlatch_1_0_0_1_0 : reg0
  	generic map (N=>1)
      port map (
                d=>XLXN_1(2 downto 2),
                load=>Tl,
					 clk=>clk,
					 clr => clr,
                q=>Tbus(2 downto 2));
					 
  Tlatch_1_0_0_1_1 : reg0
  	generic map (N=>1)
      port map (
                d=>XLXN_1(3 downto 3),
                load=>Tl,
					 clk=>clk,
					 clr => clr,
                q=>Tbus(3 downto 3));						 
					 			 
-----------------------------------------------		x4			 
  Tlatch_1_0_1 : reg0
  	generic map (N=>4)
      port map (
                d=>XLXN_1(7 downto 4),
                load=>Tl,
					 clk=>clk,
					 clr => clr,
                q=>Tbus(7 downto 4)); 					 
					 
					 
----------------------------------------------- x8
  Tlatch_1_1 : reg0
  	generic map (N=>8)
      port map (
                d=>XLXN_1(15 downto 8),
                load=>Tl,
					 clk=>clk,
					 clr => clr,
                q=>Tbus(15 downto 8));

--============================================= x16

  Tlatch_2 : reg0
  	generic map (N=>16)
      port map (
                d=>XLXN_1(31 downto 16),
                load=>Tl,
					 clk=>clk,
					 clr => clr,
                q=>Tbus(31 downto 16));				


----------------------------------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------------------------
  Tprim : reg1
      port map (
                d(31 downto 0)=>Tbus(31 downto 0),
                load=>'1',
					 clk=>clk,
					 clr => clr,
                q(31 downto 0)=>Tbp(31 downto 0));  













			
end hstack_arch;

