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
-- Dekeyser Jean-Luc ( Creation  juin 2012 revision sept 2013 ) jean-luc.dekeyser@lifl.fr
-- 
-- 
-- 
-- Cecil Licence:
-- 
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
	
	COMPONENT reg1
	generic (N : integer := 5);
	PORT(
		load : IN std_logic;
		d : IN std_logic_vector(N-1 downto 0);
		clr ,clk: IN std_logic;          
		q : OUT std_logic_vector(N-1 downto 0)
		);
	END COMPONENT;

    -- define the states of FSM model

    type state_type is (idle, starting, finish);
    signal next_state, current_state: state_type;
	 signal IPDone_i : std_logic:='0';

                                       
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
--	
	BufO_reg: reg1 
		generic map (N=>5)
		PORT MAP(
		load => '1',
		d => b1,
		clk => clk,
		clr => reset,
		q => btnout
	);

    -- cocurrent process#1: state registers
    SYNCHRO: process(clk, reset)
    begin

 if (clk'event and clk='1') then
  if (reset='1') then
       current_state <= idle;
  else
		current_state <= next_state;
		IPDone <= IPDone_i;
		if IPDone_i ='1' then
			Tout(31 downto 5)<= (others =>'0'); 
			Tout(4 downto 0) <= btnout;
		else
			Tout <= (others =>'Z');
		end if;
   end if;
 end if;

    end process;        

    -- cocurrent process#2: combinational logic
Next_output: process(current_state)
    begin

 -- use case statement to show the 
 -- state transistion
IPDone_i <= '0';

 case current_state is

     when finish => 
			IPDone_i  <= '1';  

     when others =>  
			null;
 end case;

end process;
next_node : process (IPcode, B1, B2)
begin
case current_state is
     when idle => 
			if Ipcode =  Mycode then
				 next_state <= starting;
			else             
				 next_state <= idle;
			end if;
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
        end if; 
     when finish => 
			next_state <= idle;
     when others =>  
			next_state <= idle;
end case;

end process;

end FSM;

