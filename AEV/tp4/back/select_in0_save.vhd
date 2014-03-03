----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    13:34:30 09/29/2013 
-- Design Name: 
-- Module Name:    select_in0 - Behavioral 
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
use IEEE.STD_LOGIC_signed.ALL;

-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
--use IEEE.NUMERIC_STD.ALL;

-- Uncomment the following library declaration if instantiating
-- any Xilinx primitives in this code.
--library UNISIM;
--use UNISIM.VComponents.all;

entity select_in is
    Port ( count : in  STD_LOGIC_VECTOR (7 downto 0);
			  X,Y : in  STD_LOGIC_vector(1 downto 0);
			  Lastcycle : in std_logic;
			  
			  adr0 : out STD_LOGIC_VECTOR (3 downto 0);
			  adr1 : out STD_LOGIC_VECTOR (3 downto 0);
			  adr2 : out STD_LOGIC_VECTOR (3 downto 0);
			  adr3 : out STD_LOGIC_VECTOR (3 downto 0);
			  selin0 : out STD_LOGIC_VECTOR (1 downto 0);
			  selin1 : out STD_LOGIC_VECTOR (1 downto 0);
			  selin2 : out STD_LOGIC_VECTOR (1 downto 0);
			  selin3 : out STD_LOGIC_VECTOR (1 downto 0);  
			  selreg0 : out STD_LOGIC_VECTOR (1 downto 0);
			  selreg1 : out STD_LOGIC_VECTOR (1 downto 0);
			  selreg2 : out STD_LOGIC_VECTOR (1 downto 0);
			  selreg3 : out STD_LOGIC_VECTOR (1 downto 0); 
			  selpredicat : out std_logic;			  
           we0 : out  STD_LOGIC;
           we1 : out  STD_LOGIC;
           we2 : out  STD_LOGIC;
			  we3 : out  STD_LOGIC);
end select_in;

architecture Behavioral of select_in is
signal S01, S02, S03, S11, S12, S13, S21, S22, S23, S31, S32, S33 , R0, R1, R2, R3, W0, W1, W2, W3: std_logic;

begin

selpredicat <= '1' when Y="00" else '0';

process (X,Y,count,lastcycle)
variable sel : std_logic_vector (3 downto 0) ;
variable adr : std_logic_vector(3 downto  0):="0000" ;
variable bank : std_logic_vector (1 downto 0):="00" ;
begin
sel := X&Y;
we0<='0'; we1<='0';we2<='0'; we3<='0';
adr0<= "0000"; 
adr1<= "0000"; 
adr2<= "0000"; 
adr3<= "0000"; 
selreg0<="00";
selreg1<="00";
selreg2<="00";
selreg3<="00";
selin0<="00";
selin1<="00";
selin2<="00";
selin3<="00";
adr := count(5 downto 2);
bank := count(1 downto 0);
if lastcycle='1' then
	case sel is
		when "0000"  => null;
		when "0100" => 
			case bank is 
				when "00" =>  selreg3 <= "10"; adr3 <= adr -2;
				when "01" =>  selreg0 <= "10"; adr0 <= adr-1;
				when "10" =>  selreg1 <= "10"; adr1 <= adr-1;
				when "11" =>  selreg2 <= "10"; adr2 <= adr-1;
				when others => null;
			end case;
		when "1000" => 
			case bank is 
				when "00" =>  selreg3 <= "10"; adr3 <= adr -2; 	 selreg2 <= "10"; adr2 <= adr -2;
				when "01" =>  selreg0 <= "10"; adr0 <= adr-1; 		 selreg3 <= "10"; adr3 <= adr -2;
				when "10" =>  selreg1 <= "10"; adr1 <= adr-1; 		 selreg0 <= "10"; adr0 <= adr-1;
				when "11" =>  selreg2 <= "10"; adr2 <= adr-1; 		 selreg1 <= "10"; adr1 <= adr-1;
				when others  => null;
			end case;
		when "1100" => 
			case bank is 
				when "00" =>  selreg3 <= "10"; adr3 <= adr -2; 	 selreg2 <= "10"; adr2 <= adr -2; 	 selreg1 <= "10"; adr1 <= adr -2;
				when "01" =>  selreg0 <= "10"; adr0 <= adr-1; 		 selreg3 <= "10"; adr3 <= adr -2; 	 selreg2 <= "10"; adr2 <= adr -2; 
				when "10" =>  selreg1 <= "10"; adr1 <= adr-1; 		 selreg0 <= "10"; adr0 <= adr-1; 		 selreg3 <= "10"; adr3 <= adr -2;
				when "11" =>  selreg2 <= "10"; adr2 <= adr-1; 		 selreg1 <= "10"; adr1 <= adr-1; 		 selreg0 <= "10"; adr0 <= adr-1;
				when others => null;
			end case;
		when "0001" =>
			case bank is
				when "00" => we0 <= '1'; selreg0 <= "01"; adr0 <= adr; selin0 <= "00";
				when "01" => we1 <= '1'; selreg1 <= "01"; adr1 <= adr; selin1 <="00" ;
				when "10" => we2 <= '1'; selreg2 <= "01"; adr2 <= adr; selin2 <="00" ;
				when "11" => we3 <= '1'; selreg3 <= "01"; adr3 <= adr; selin3 <="00" ;
				when others => null;
			end case;		
		when "0010" =>
			case bank is
				when "00" => we0 <= '1'; selreg0 <= "01"; adr0 <= adr; selin0 <= "01"; 	we1 <= '1'; selreg1 <= "01"; adr1 <= adr; selin1 <="00" ;
				when "01" => we1 <= '1'; selreg1 <= "01"; adr1 <= adr; selin1 <="01" ; 	we2 <= '1'; selreg2 <= "01"; adr2 <= adr; selin2 <="00" ;
				when "10" => we2 <= '1'; selreg2 <= "01"; adr2 <= adr; selin2 <="01" ; 	we3 <= '1'; selreg3 <= "01"; adr3 <= adr; selin3 <="00" ;
				when "11" => we3 <= '1'; selreg3 <= "01"; adr3 <= adr; selin3 <="01" ; 	we0 <= '1'; selreg0 <= "01"; adr0 <= adr +1 ; selin0 <= "00";
				when others => null;
			end case;	
		when "0011" =>
			case bank is
				when "00" => we0 <= '1'; selreg0 <= "01"; adr0 <= adr; selin0 <= "10"; 	we1 <= '1'; selreg1 <= "01"; adr1 <= adr; selin1 <="01" ;		we2 <= '1'; selreg2 <= "01"; adr2 <= adr; selin2 <="00" ;
				when "01" => we1 <= '1'; selreg1 <= "01"; adr1 <= adr; selin1 <="10" ; 	we2 <= '1'; selreg2 <= "01"; adr2 <= adr; selin2 <="01" ;		we3 <= '1'; selreg3 <= "01"; adr3 <= adr; selin3 <="00" ;
				when "10" => we2 <= '1'; selreg2 <= "01"; adr2 <= adr; selin2 <="10" ; 	we3 <= '1'; selreg3 <= "01"; adr3 <= adr; selin3 <="01" ; 		we0 <= '1'; selreg0 <= "01"; adr0 <= adr +1 ; selin0 <= "00";
				when "11" => we3 <= '1'; selreg3 <= "01"; adr3 <= adr; selin3 <="10" ; 	we0 <= '1'; selreg0 <= "01"; adr0 <= adr +1 ; selin0 <= "01";	we1 <= '1'; selreg1 <= "01"; adr1 <= adr +1; selin1 <="00" ;
				when others => null;
			end case;	
		when "0101" =>
			case bank is
				when "00" => we3 <= '1'; selreg3 <= "01"; adr3 <= adr-1; selin3 <= "00"; 	 
				when "01" => we0 <= '1'; selreg0 <= "01"; adr0 <= adr; selin0 <="00" ; 	 
				when "10" => we1 <= '1'; selreg1 <= "01"; adr1 <= adr; selin1 <="00" ; 	
				when "11" => we2 <= '1'; selreg2 <= "01"; adr2 <= adr; selin2 <="00" ; 	
				when others => null;
			end case;	
		when "0110" =>
			case bank is
				when "00" => we3 <= '1'; selreg3 <= "01"; adr3 <= adr-1; selin3 <= "01"; 	we0 <= '1'; selreg0 <= "01"; adr0 <= adr; selin0 <="00" ; 
				when "01" => we0 <= '1'; selreg0 <= "01"; adr0 <= adr; selin0 <="01" ; 	we1 <= '1'; selreg1 <= "01"; adr1 <= adr; selin1 <="00" ; 	 
				when "10" => we1 <= '1'; selreg1 <= "01"; adr1 <= adr; selin1 <="01" ; 	we2 <= '1'; selreg2 <= "01"; adr2 <= adr; selin2 <="00" ; 
				when "11" => we2 <= '1'; selreg2 <= "01"; adr2 <= adr; selin2 <="01" ; 	we3 <= '1'; selreg3 <= "01"; adr3 <= adr; selin3 <= "00";
				when others => null;
			end case;	
		when "0111" =>
			case bank is
				when "00" => we3 <= '1'; selreg3 <= "01"; adr3 <= adr-1; selin3 <= "10"; 	we0 <= '1'; selreg0 <= "01"; adr0 <= adr; selin0 <="01" ; 	we1 <= '1'; selreg1 <= "01"; adr1 <= adr; selin1 <="00" ; 
				when "01" => we0 <= '1'; selreg0 <= "01"; adr0 <= adr; selin0 <="10" ; 	we1 <= '1'; selreg1 <= "01"; adr1 <= adr; selin1 <="01" ; 	we2 <= '1'; selreg2 <= "01"; adr2 <= adr; selin2 <="00" ;  
				when "10" => we1 <= '1'; selreg1 <= "01"; adr1 <= adr; selin1 <="10" ; 	we2 <= '1'; selreg2 <= "01"; adr2 <= adr; selin2 <="01" ; 	we3 <= '1'; selreg3 <= "01"; adr3 <= adr; selin3 <= "00";
				when "11" => we2 <= '1'; selreg2 <= "01"; adr2 <= adr; selin2 <="10" ; 	we3 <= '1'; selreg3 <= "01"; adr3 <= adr; selin3 <= "01";	we0 <= '1'; selreg0 <= "01"; adr0 <= adr +1; selin0 <="00" ;
				when others => null;
			end case;			
		when "1001" =>
			case bank is
				when "00" => we2 <= '1'; selreg2 <= "01"; adr2 <= adr-1; selin2 <= "00"; 		 selreg3 <= "10"; adr3 <= adr -2; 
				when "01" => we3 <= '1'; selreg3 <= "01"; adr3 <= adr -1; selin3 <="00" ; 	 selreg0 <= "10"; adr0 <= adr -1; 
				when "10" => we0 <= '1'; selreg0 <= "01"; adr0 <= adr; selin0 <="00" ; 		 selreg1 <= "10"; adr1 <= adr -1; 
				when "11" => we1 <= '1'; selreg1 <= "01"; adr1 <= adr; selin1 <="00" ; 		 selreg2 <= "10"; adr2 <= adr -1; 
				when others => null;
			end case;	
		when "1010" =>
			case bank is
				when "00" => we2 <= '1'; selreg2 <= "01"; adr2 <= adr-1; selin2 <= "01"; 	we3 <= '1'; selreg3 <= "01"; adr3 <= adr-1; selin3 <="00" ; 
				when "01" => we3 <= '1'; selreg3 <= "01"; adr3 <= adr-1; selin3 <="01" ; 	we0 <= '1'; selreg0 <= "01"; adr0 <= adr; selin0 <="00" ; 	 
				when "10" => we0 <= '1'; selreg0 <= "01"; adr0 <= adr; selin0 <="01" ; 	we1 <= '1'; selreg1 <= "01"; adr1 <= adr; selin1 <="00" ; 
				when "11" => we1 <= '1'; selreg1 <= "01"; adr1 <= adr; selin1 <="01" ; 	we2 <= '1'; selreg2 <= "01"; adr2 <= adr; selin2 <= "00";
				when others => null;
			end case;	
		when "1011" =>
			case bank is
				when "00" => we2 <= '1'; selreg2 <= "01"; adr2 <= adr-1; selin2 <= "10"; 	we3 <= '1'; selreg3 <= "01"; adr3 <= adr-1; selin3 <="01" ; 	we0 <= '1'; selreg0 <= "01"; adr0 <= adr; selin0 <="00" ; 
				when "01" => we3 <= '1'; selreg3 <= "01"; adr3 <= adr-1; selin3 <="10" ; 	we0 <= '1'; selreg0 <= "01"; adr0 <= adr; selin0 <="01" ; 	 	we1 <= '1'; selreg1 <= "01"; adr1 <= adr; selin1 <="00" ;
				when "10" => we0 <= '1'; selreg0 <= "01"; adr0 <= adr; selin0 <="10" ; 	we1 <= '1'; selreg1 <= "01"; adr1 <= adr; selin1 <="01" ; 		we2 <= '1'; selreg2 <= "01"; adr2 <= adr; selin2 <= "00";
				when "11" => we1 <= '1'; selreg1 <= "01"; adr1 <= adr; selin1 <="10" ; 	we2 <= '1'; selreg2 <= "01"; adr2 <= adr; selin2 <= "01";		we3 <= '1'; selreg3 <= "01"; adr3 <= adr; selin3 <="00" ;
				when others => null;
			end case;			
		when "1101" =>
			case bank is
				when "00" => we1 <= '1'; selreg1 <= "01"; adr1 <= adr-1; selin1 <= "00"; 		 selreg2 <= "10"; adr2 <= adr -2;  	 selreg3 <= "10"; adr3 <= adr -2; 
				when "01" => we2 <= '1'; selreg2 <= "01"; adr2 <= adr -1; selin2 <="00" ; 	 selreg3 <= "10"; adr3 <= adr -2; 		 selreg0 <= "10"; adr0 <= adr -1; 
				when "10" => we3 <= '1'; selreg3 <= "01"; adr3 <= adr -1; selin3 <="00" ; 	 selreg0 <= "10"; adr1 <= adr -1; 		 selreg1 <= "10"; adr2 <= adr -1;
				when "11" => we0 <= '1'; selreg0 <= "01"; adr0 <= adr; selin0 <="00" ; 		 selreg1 <= "10"; adr2 <= adr -1; 		 selreg2 <= "10"; adr2 <= adr -1;
				when others => null;
			end case;	
		when "1110" =>
			case bank is
				when "00" => we1 <= '1'; selreg1 <= "01"; adr1 <= adr-1; selin1 <= "01"; 	we2 <= '1'; selreg2 <= "01"; adr2 <= adr-1; selin2 <="00" ; 	selreg3 <= "10"; adr3 <= adr -2; 
				when "01" => we2 <= '1'; selreg2 <= "01"; adr2 <= adr-1; selin2 <="01" ; 	we3 <= '1'; selreg3 <= "01"; adr3 <= adr-1; selin3 <="00" ; 	selreg0 <= "10"; adr0 <= adr -1; 
				when "10" => we3 <= '1'; selreg3 <= "01"; adr3 <= adr-1; selin3 <="01" ; 	we0 <= '1'; selreg0 <= "01"; adr0 <= adr; selin0 <="00" ;  	selreg1 <= "10"; adr1 <= adr -1;
				when "11" => we0 <= '1'; selreg0 <= "01"; adr0 <= adr; selin0 <="01" ; 	we1 <= '1'; selreg1 <= "01"; adr1 <= adr; selin1 <= "00";		selreg2 <= "10"; adr2 <= adr -1;
				when others => null;
			end case;	
		when "1111" =>
			case bank is
				when "00" => we3 <= '1'; selreg3 <= "01"; adr3 <= adr-1; selin3 <= "00"; 	we2 <= '1'; selreg2 <= "01"; adr2 <= adr-1; selin2 <="01" ; 	we1 <= '1'; selreg1 <= "01"; adr1 <= adr-1; selin1 <="10" ; 
				when "01" => we0 <= '1'; selreg0 <= "01"; adr0 <= adr; selin0 <="00" ; 	we3 <= '1'; selreg3 <= "01"; adr3 <= adr-1; selin3 <="01" ; 	we2 <= '1'; selreg2 <= "01"; adr2 <= adr-1; selin2 <="10" ;  
				when "10" => we1 <= '1'; selreg1 <= "01"; adr1 <= adr; selin1 <="00" ; 	we0 <= '1'; selreg0 <= "01"; adr0 <= adr; selin0 <="01" ; 	we3 <= '1'; selreg3 <= "01"; adr3 <= adr-1; selin3 <= "10";
				when "11" => we2 <= '1'; selreg2 <= "01"; adr2 <= adr; selin2 <="00" ; 	we1 <= '1'; selreg1 <= "01"; adr1 <= adr; selin1 <= "01";	we0 <= '1'; selreg0 <= "01"; adr0 <= adr ; selin0 <="10" ;
				when others => null;
			end case;			
		when others  => null ;
		end case;
else
		null;
end if;
end process;
				


end Behavioral;
