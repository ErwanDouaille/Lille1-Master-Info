----------------------------------------------------------------------------------
-- Copyright : UNIVERSITE DE LILLE 1 - INRIA Lille Nord de France
--  Villeneuve d'Accsq France
-- 
-- Module Name  : IP wait button
-- Project Name :  Homade V2.1
-- Revision :      no
--                                         
-- Target Device :     spartan 6 spartan 3
-- Tool Version : tested on ISE 12.4,
--                                                   
-- Description :  IP Wait Button
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
-----------------------------------------------------
                             
library ieee ;
use ieee.std_logic_1164.all;
                             
-----------------------------------------------------

entity IP_waitbt is  
		GENERIC (Mycode : std_logic_vector(10 downto 0) ); 
port(    

	Tin : in STD_LOGIC_VECTOR(4 downto 0); 
                               
	IPcode:		in STD_LOGIC_VECTOR(10 downto 0);                
	clk:		in std_logic;
	reset:		in std_logic;
	Btn: in STD_LOGIC_VECTOR(4 downto 0);  
	Tout : out STD_LOGIC_VECTOR(31 downto 0); 
	IPdone:		out std_logic
); 
	attribute clock_signal : string;
	attribute clock_signal of clk : signal is "yes";        
end IP_waitbt;

-----------------------------------------------------

architecture FSM of IP_waitbt is                                                                                                    
   -- local signal
 signal b1, b2, btnout: std_logic_vector (4 downto 0);  
 signal btnload : std_logic;
  COMPONENT pulse
     PORT( clk : IN STD_LOGIC; 
          inp : IN STD_LOGIC; 
          outp : OUT STD_LOGIC);
   END COMPONENT;
	
	COMPONENT reg0
	generic (N : integer := 5);
	PORT(
		load : IN std_logic;
		d : IN std_logic_vector(N-1 downto 0);
		clr ,clk: IN std_logic;          
		q : OUT std_logic_vector(N-1 downto 0)
		);
	END COMPONENT;

    -- define the states of FSM model

    type state_type is (idle, starting, finish, finish2, finish3);
    signal next_state, current_state: state_type;
                                       
begin    
 -- local architecture
 b2 <= Tin; 

   pls0: pulse PORT MAP(
  clk => clk, 
  inp => Btn(0), 
  outp => b1(0)
   );
   pls1: pulse PORT MAP(
  clk => clk, 
  inp => Btn(1), 
  outp => b1(1)
   );
   pls2: pulse PORT MAP(
  clk => clk, 
  inp => Btn(2), 
  outp => b1(2)
   );
   pls3: pulse PORT MAP(
  clk => clk, 
  inp => Btn(3), 
  outp => b1(3)
   );   
   pls4: pulse PORT MAP(
  clk => clk, 
  inp => Btn(4), 
  outp => b1(4)
   );
	
	BufO_reg: reg0 
		generic map (N=>5)
		PORT MAP(
		load => '1',
		d => b1,
		clk => clk,
		clr => reset,
		q => btnout
	);

    -- cocurrent process#1: state registers
    state_reg: process(clk, reset)
    begin

 if (clk'event and clk='1') then
  if (reset='1') then
            current_state <= idle;
  else
   current_state <= next_state;
   end if;
 end if;

    end process;        

    -- cocurrent process#2: combinational logic
    comb_logic: process(current_state, Ipcode, b1, b2,btnout)
	 variable b :  std_logic_vector (4 downto 0) :="00000" ;
    begin

 -- use case statement to show the 
 -- state transistion

 case current_state is

     when idle => 
			IPdone <= '0'; 	
			Tout <= (others =>'Z'); 
			if Ipcode =  Mycode then
				 next_state <= starting;
			else             
				 next_state <= idle;
			end if;
			b:="00000";
     when starting =>
         if  ( (b1(0) = '1'   and b2(0) = '1' ) or
              (b1(1) = '1'  and  b2(1) = '1' ) or
				 (b1(2) = '1'  and  b2(2) = '1') or
				 (b1(3) = '1'  and  b2(3) = '1'  )or
				 (b1(4) = '1'  and  b2(4) = '1'  ))
			then
				 next_state <= finish;  
			else 
				 next_state <= starting; 
				 b:="00000";
        end if; 
		  Ipdone <= '0';
		  Tout <= (others =>'Z'); 
     when finish => 
			IPdone  <= '1';  
			Tout(31 downto 5)<= (others =>'0'); 
			Tout(4 downto 0) <= btnout;
			next_state <= finish2;
			b:="00000";
     when finish2 => 
			IPdone  <= '0';  
			Tout <= (others =>'Z'); 
			next_state <= finish3;
						b:="00000";
     when finish3 => 
			IPdone  <= '0';  
			Tout <= (others =>'Z'); 
			next_state <= idle;
						b:="00000";
     when others =>  
			IPdone <= '0';     
			Tout <= (others =>'Z');  
			next_state <= idle;
						b:="00000";

 end case;

end process;

end FSM;