																-- Vhdl test bench created from schematic /home/m1/douaille/didact_sc/didact_top.sch - Fri Sep 27 11:50:04 2013
--
-- Notes: 
-- 1) This testbench template has been automatically generated using types
-- std_logic and std_logic_vector for the ports of the unit under test.
-- Xilinx recommends that these types always be used for the top-level
-- I/O of a design in order to guarantee that the testbench will bind
-- correctly to the timing (post-route) simulation model.
-- 2) To use this template as your testbench, change the filename to any
-- name of your choice with the extension .vhd, and use the "Source->Add"
-- menu in Project Navigator to import the testbench. Then
-- edit the user defined section below, adding code to generate the 
-- stimulus for your design.
--
LIBRARY ieee;
USE ieee.std_logic_1164.ALL;
USE ieee.numeric_std.ALL;
LIBRARY UNISIM;
USE UNISIM.Vcomponents.ALL;
ENTITY didact_top_didact_top_sch_tb IS
END didact_top_didact_top_sch_tb;
ARCHITECTURE behavioral OF didact_top_didact_top_sch_tb IS 

   COMPONENT didact_top
   PORT( clkin	:	IN	STD_LOGIC; 
          rst	:	IN	STD_LOGIC; 
          bouton1	:	IN	STD_LOGIC; 
          bouton2	:	IN	STD_LOGIC; 
          bouton3	:	IN	STD_LOGIC; 
          Q_del	:	OUT	STD_LOGIC_VECTOR (7 DOWNTO 0));
   END COMPONENT;

   SIGNAL clkin	:	STD_LOGIC := '0';
   SIGNAL rst	:	STD_LOGIC;
   SIGNAL bouton1	:	STD_LOGIC;
   SIGNAL bouton2	:	STD_LOGIC;
   SIGNAL bouton3	:	STD_LOGIC;
   SIGNAL Q_del	:	STD_LOGIC_VECTOR (7 DOWNTO 0);

BEGIN

   UUT: didact_top PORT MAP(
		clkin => clkin, 
		rst => rst, 
		bouton1 => bouton1, 
		bouton2 => bouton2, 
		bouton3 => bouton3, 
		Q_del => Q_del
   );
	-- *** Test Bench - User Defined Section ***
	-- process pour gnrer l'horloge
	-- une priode d'horloge de 20 ns est utilise
	clkin_gen:process(clkin)
	begin
	clkin <= not clkin after 10 ns;
	end process clkin_gen;
	-- process de d'assignation des signaux de test

   tb : PROCESS
   BEGIN
		rst <='1';
		bouton1 <= '0';
		bouton2 <= '0';
		bouton3 <= '0';
		wait for 100 us;
		rst <= '0';
		bouton1 <= '0';
		bouton2 <= '0';
		bouton3 <= '0';
		wait for 4000 us;
		bouton1 <= '1';
		bouton2 <= '0';
		bouton3 <= '0';
		wait for 4000 us;
		bouton1 <= '0';
		bouton2 <= '0';
		bouton3 <= '0';
		wait for 4000 us;
		bouton1 <= '0';
		bouton2 <= '1';
		bouton3 <= '0';
		wait for 4000 us;
		bouton1 <= '0';
		bouton2 <= '0';
		bouton3 <= '0';
		wait for 4000 us;
		bouton1 <= '0';
		bouton2 <= '0';
		bouton3 <= '1';
		wait for 4000 us;
		bouton1 <= '0';
		bouton2 <= '0';
		bouton3 <= '0';

      WAIT; -- will wait forever
   END PROCESS;
-- *** End Test Bench - User Defined Section ***

END;
