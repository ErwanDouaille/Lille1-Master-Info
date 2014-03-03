----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    11:26:07 10/04/2013 
-- Design Name: 
-- Module Name:    didact_top - Behavioral 
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

-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
--use IEEE.NUMERIC_STD.ALL;

-- Uncomment the following library declaration if instantiating
-- any Xilinx primitives in this code.
--library UNISIM;
--use UNISIM.VComponents.all;

entity didact_top is
    Port ( rst : in  STD_LOGIC;
           clkin : in  STD_LOGIC;
           bouton1 : in  STD_LOGIC;
           bouton2 : in  STD_LOGIC;
           bouton3 : in  STD_LOGIC;
           Q_del : out  STD_LOGIC_VECTOR (7 downto 0));
end didact_top;

architecture Behavioral of didact_top is
	component debounce_hdl
	port(
		sig_in : in STD_LOGIC;
		clkin : in STD_LOGIC;
		sig_out : out STD_LOGIC);
	end component;
	
	component dcm1
	port
	 (-- Clock in ports
	  CLK_IN1           : in     std_logic;
	  -- Clock out ports
	  CLK_OUT1          : out    std_logic;
	  -- Status and control signals
	  RESET             : in     std_logic
	 );
	end component;
	
	component diviseur_clk
		port(
		clkin : in STD_LOGIC;
		clk2hz : buffer STD_LOGIC;
		clk16hz : buffer STD_LOGIC;
		clk2khz : buffer STD_LOGIC);
	end component;
	
	component msa_hdl
		port(
			clkin : in STD_LOGIC;
			rst : in STD_LOGIC;
			b0 : in STD_LOGIC;
			b1 : in STD_LOGIC;
			gs : in STD_LOGIC;
			enable_del : out STD_LOGIC);
	end component;

	
signal clk_dcm1 : std_logic;
signal CLKIN_IBUFG_OUT : std_logic;
signal CLK0_OUT : std_logic;
signal LOCKED_OUT : std_logic;
signal b0, b1, gs, enable_del : std_logic;
signal clk2hz,clk16hz,clk2khz : std_logic;
signal debout1,debout2,debout3 : std_logic;
signal shreg : std_logic_vector(7 downto 0);



begin
	inst1_debounce: debounce_hdl port map(
		sig_in => bouton1,
		sig_out => debout1,
		clkin => clk2khz
	);
	
	inst2_debounce: debounce_hdl port map(
		sig_in => bouton2,
		sig_out => debout2,
		clkin => clk2khz
	);
	
	inst3_debounce: debounce_hdl port map(
		sig_in => bouton3,
		sig_out => debout3,
		clkin => clk2khz
	);
	
	Inst_dcm1: dcm1 port map(
    CLK_IN1         => clkin,
    CLK_OUT1           => clk_dcm1,
    RESET              => rst
	 );
	 
	 inst_diviseur_clk: diviseur_clk port map(
		clkin => clk_dcm1,
		clk2hz => clk2hz,
		clk16hz => clk16hz,
		clk2khz => clk2khz
	);
	
	Inst_msa_hdl: msa_hdl port map(
		clkin => clk_dcm1,
		rst => rst,
		b0 => b0,
		b1 => b1,
		gs => gs,
		enable_del => enable_del
	);

-- Description de l'encodeur
b0 <= '1' when (debout3 = '0' and debout2 = '1' and debout1 = '0') or
(debout3 = '1' and debout2 = '1' and debout1 = '0') else
'0';
b1 <= '1' when debout3 = '1' and debout2 = '0' and debout1 = '0' else
'0';

-- Description du Get something
gs <= '1' when debout3 = '1' or debout2 = '1' or debout1 = '1' else
'0';

-- Description du registre  dcalage
xshifreg: process(rst,clk16hz)
begin
	if(rst = '1')then
		shreg <= (others => '0');
	elsif(clk16hz'event and clk16hz = '1')then
		if(enable_del = '1')then
			shreg(0)<= clk2hz;
			shreg(7 downto 1) <= shreg(6 downto 0);
		end if;
	end if;
end process;
Q_del <= shreg;


end Behavioral;

