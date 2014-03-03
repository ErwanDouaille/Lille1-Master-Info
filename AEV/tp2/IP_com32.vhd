----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    11:59:29 02/18/2013 
-- Design Name: 
-- Module Name:    IP_com32 - Behavioral 
-- Project Name: 
-- Target Devices: 
-- Tool versions: 
-- Description: 
--
-- Dependencies: 
--
-- Revision: 
-- Revision 0.01 - File Created
-- Additional Comments: 
--
----------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.std_logic_unsigned.all;
use IEEE.std_logic_arith.all;
use IEEE.numeric_std.all;

-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
--use IEEE.NUMERIC_STD.ALL;

-- Uncomment the following library declaration if instantiating
-- any Xilinx primitives in this code.
--library UNISIM;
--use UNISIM.VComponents.all;

entity IP_com32 is
		GENERIC (Mycode : std_logic_vector(10 downto 0) );
    Port ( clk : in  STD_LOGIC;
	 reset : std_logic ;
           IPcode  : in  STD_LOGIC_VECTOR (10 downto 0);
			  IPdone : out std_logic;
           shift_en : out  STD_LOGIC);
end IP_com32;


architecture FSM of IP_com32 is
--
--    -- define the states of FSM model
-- signal action,rst, cp_zero,IPd: STD_LOGIC:='0';
 signal rst,  IPd , reset_or: std_logic;
 signal cptr_d,cptr_next : std_logic_vector (4 downto 0);
--
--
 signal c :integer :=0;
 
    -- define the states of FSM model

    type state_type is (idle, starting, finish, fetch_next,Decode_next);
    signal next_state : state_type;
	 signal current_statew : state_type := idle;
--    type state_type is (Idle, Init, Decr, Done);
--    signal next_state, current_state: state_type := idle;
--  
COMPONENT reg0
Generic (N : integer := 5);
 PORT(
  load : IN std_logic;
  d : IN std_logic_vector(N-1 downto 0);
  clr ,clk: IN std_logic;          
  q : OUT std_logic_vector(N-1 downto 0)
  );
 END COMPONENT;
begin
  Wait_reg : reg0
  generic map (N => 5)
      port map (
                d=>cptr_d,
                load=>'1',
      clk=>clk,
      clr => reset_or ,
                q=>cptr_next );  

reset_or <= reset or rst ;
--

--
--cptr_d <= "11111" when rst='1' else cptr_next - 1;
--cptr_d <= "11110" when rst='1' else cptr_next - 1;
cptr_d<= cptr_next + 1;
---

IPdone<=IPd;

    -- cocurrent process#1: state registers
    state_reg: process(clk, reset)
    begin

 if (clk'event and clk='1') then
  if (reset='1') then
            current_statew <= idle;
  else
   current_statew <= next_state; 
  end if;
 end if;

    end process;        

    -- cocurrent process#2: combinational logic
    comb_logic: process( Ipcode,cptr_next,current_statew)
    begin

 -- use case statement to show the 
 -- state transistion

 case current_statew is
--=================================
     when idle => 

   if Ipcode =  Mycode then
     rst<='1';
       next_state <= starting;
shift_en <= '1';
   else
shift_en <= '0';
    rst <='0';
    next_state <= idle;
   end if;
   IPd <= '0'; 
--=================================
     when starting =>
   rst<='0';
  shift_en <='1';
 Ipd <= '0';
if cptr_next= "11111" then
next_state <= finish;       
 else 
next_state <= starting;  
end if; 
     
--=================================
     when finish => 
   IPd  <= '1';  
   rst<='0';
   shift_en <= '0';
   next_state<= fetch_next;
--=================================   
     when fetch_next => 
   IPd  <= '0';  
   rst<='0';
   shift_en <= '0';
    next_state<=  Decode_next;
--=================================
     when Decode_next => 
   IPd  <= '0';  
   rst<='0';
   shift_en <= '0';
   next_state<= idle;
--=================================
when others =>  
IPd <= '0';     
rst<='0';
next_state <= idle;
end case;

end process;

end FSM;

----------------------------