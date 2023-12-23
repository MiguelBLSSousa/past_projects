-- Project          : VHDL Hybrid accumulator-2operand machine
--                    Digital Hardware by group 
-- 
-- File             : ALU.vhd
--
-- Related File(s)  : 
--000 passive
--001 AND
--010 OR
--011 ADD
--100 SFTL
--101 SFTR
--110 INV
--111 INV


LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;
USE IEEE.numeric_std.ALL;

ENTITY ALU IS
    PORT (
	Clk     : IN std_logic;
	Reset : IN std_logic; -- low active
	Bus_A : IN std_logic_vector(15 DOWNTO 0); --Bus A from registerFile
	Bus_B : IN std_logic_vector(15 DOWNTO 0); --Bus B from registerFile
	opvalue : IN std_logic_vector(2 DOWNTO 0); -- op code for ALU
	Bus_C : OUT std_logic_vector(15 DOWNTO 0); --Bus C to registerFile
	Branch_Conditions : OUT std_logic_vector(3 DOWNTO 0) -- branch conditions bits from ALU
      );
END ENTITY ALU;

ARCHITECTURE bhv OF ALU IS

ALIAS CC_N : std_logic IS Branch_Conditions(3); --negative
ALIAS CC_Z : std_logic IS Branch_Conditions(2); --zero
ALIAS CC_V : std_logic IS Branch_Conditions(1); --overflow
ALIAS CC_C : std_logic IS Branch_Conditions(0); --carry

BEGIN

PROCESS(clk, reset, opvalue)
VARIABLE Bus_C_with_carry : std_logic_vector(16 DOWNTO 0); --bus_c with carry bit
BEGIN

	IF reset = '0' THEN
	
		Bus_C <= "0000000000000000";
		branch_conditions <= "0000";

	ELSIF rising_edge(clk) THEN

		Bus_C <= "0000000000000000";
		Bus_C_with_carry(16):='0';		
		CASE opvalue IS
			--WHEN "000" => --passive
			--	Bus_C_with_carry(15 DOWNTO 0) := "0000000000000000";
			WHEN "001" => --AND
				Bus_C_with_carry(15 DOWNTO 0) := Bus_A AND Bus_B;
			WHEN "010" => --OR
				Bus_C_with_carry(15 DOWNTO 0) := Bus_A OR Bus_B;
			WHEN "011" => --ADD
				Bus_C_with_carry := std_logic_vector(resize(signed(Bus_A), 17) + signed(Bus_B)); --not sure if correct addition
			WHEN "100" => --SFTL
				Bus_C_with_carry(15 DOWNTO 0) := std_logic_vector((shift_left (unsigned(Bus_A),to_integer(unsigned(Bus_B(4 DOWNTO 0))))));
			WHEN "101" => --SFTR
				Bus_C_with_carry(15 DOWNTO 0) := std_logic_vector((shift_right (unsigned(Bus_A),to_integer(unsigned(Bus_B(4 DOWNTO 0))))));
			WHEN "110" => --INV
				Bus_C_with_carry(15 DOWNTO 0) := (NOT Bus_A);
			WHEN "111" =>
				Bus_C_with_carry(15 DOWNTO 0) := std_logic_vector(unsigned(NOT Bus_A) + 1); --unsigned/signed 
			WHEN others =>
			
		END CASE;
		
		CC_N <= Bus_C_with_carry(15);
		IF Bus_C_with_carry(15 DOWNTO 0)=(15 DOWNTO 0=>'0') THEN 
			CC_Z<='1'; 
		ELSE 
			CC_Z<= '0'; 
		END IF;
      		IF (Bus_A(15)=Bus_B(15)) AND (bus_A(15)/=Bus_C_with_carry(15)) THEN 
			CC_V <='1'; 
		ELSE 
			CC_V<='0'; 
		END IF;
     		 CC_C <= Bus_C_with_carry(16);    
		
		Bus_C <= Bus_C_with_carry(15 DOWNTO 0);
	END IF;
END PROCESS;
END;
