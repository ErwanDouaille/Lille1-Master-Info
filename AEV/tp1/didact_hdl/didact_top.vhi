
-- VHDL Instantiation Created from source file didact_top.vhd -- 11:36:04 10/04/2013
--
-- Notes: 
-- 1) This instantiation template has been automatically generated using types
-- std_logic and std_logic_vector for the ports of the instantiated module
-- 2) To use this template to instantiate this entity, cut-and-paste and then edit

	COMPONENT didact_top
	PORT(
		rst : IN std_logic;
		clkin : IN std_logic;
		bouton1 : IN std_logic;
		bouton2 : IN std_logic;
		bouton3 : IN std_logic;          
		Q_del : OUT std_logic_vector(7 downto 0)
		);
	END COMPONENT;

	Inst_didact_top: didact_top PORT MAP(
		rst => ,
		clkin => ,
		bouton1 => ,
		bouton2 => ,
		bouton3 => ,
		Q_del => 
	);


