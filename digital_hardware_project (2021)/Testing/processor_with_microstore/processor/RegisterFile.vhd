-- Project          : VHDL Hybrid accumulator-2operand machine
--                    Digital Hardware by group 
-- 
-- File             : temp_.vhd
--
-- Related File(s)  : 
--%r0		- %00 always 0 register
--%r59-r%62	- %wr/rd main memory write/read registers
--%r63		- %rd standard destination register from ALU

LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;
USE IEEE.numeric_std.ALL;

ENTITY RegisterFile IS
	PORT (
		Clk : IN std_logic;
		Reset : IN std_logic;
		Bus_A : OUT std_logic_vector(15 DOWNTO 0); --bus to rs1 --Bus_A
		Sel_A : IN std_logic_vector(5 DOWNTO 0);--sel_A
		Bus_B : OUT std_logic_vector(15 DOWNTO 0); --bus to rs2 --Bus_B
		Sel_B : IN std_logic_vector(5 DOWNTO 0);--sel_B
		Bus_C : IN std_logic_vector(15 DOWNTO 0);--alu output --Bus_C
		Bus_D : IN std_logic_vector(15 DOWNTO 0);--read from memory --Bus_D
		I_Index : IN integer;
		IR_Bit: OUT std_logic
      );
END ENTITY RegisterFile;

ARCHITECTURE bhv OF RegisterFile IS
	TYPE register_file_type IS ARRAY (63 DOWNTO 0) OF std_logic_vector(15 DOWNTO 0);--create register file data type
	SIGNAL register_file : register_file_type :=(OTHERS=>(OTHERS=>'0'));--set every adress and every value in adress to 0
	SIGNAL gen_constant: std_logic_vector (15 downto 0) := "0000000000000000"; 
BEGIN

PROCESS(clk, reset, sel_a, sel_b, bus_c, bus_d, i_index) BEGIN

	IF reset = '0' THEN

		--loop to reset all registers to 0
		register_file <= (OTHERS=>(OTHERS=>'0'));
		Bus_A <= "0000000000000000";
		Bus_B <= "0000000000000000";
		IR_Bit <= '1';

	ELSIF falling_edge(clk) THEN
		register_file(0) <= (OTHERS=>'0'); --%r0 constant zero

		case I_Index is
		when 1 =>
			IR_Bit <= '0';
			--move command
			register_file(to_integer(unsigned(Sel_B))) <= register_file(to_integer(unsigned(Sel_A)));
			register_file(63) <= Bus_C;
		when 2 =>
			IR_Bit <= '0';
			--output of memory into register(59-62)
			Bus_A <= register_file(to_integer(unsigned(Sel_A)));
			register_file(to_integer(unsigned(Sel_B))) <= Bus_D;
		when 3 =>
			IR_Bit <= '0';
			--input to memory from register(59-62)
			Bus_A <= register_file(to_integer(unsigned(Sel_A)));
			Bus_B <= register_file(to_integer(unsigned(Sel_B)));
		when 4 =>
			IR_Bit <= '0';
			-- generate constant
			gen_constant(10 downto 0) <= Sel_B(4 downto 0) & Sel_A(5 downto 0);
			Bus_A <= register_file(0);
			Bus_B <= gen_constant;
		when 5 =>
			IR_Bit <= '0';
			--read Bus_C
			register_file(63) <= Bus_C;
			Bus_A <= register_file(to_integer(unsigned(Sel_A)));
			Bus_B <= register_file(to_integer(unsigned(Sel_B)));
		when others =>
			IR_Bit <= '1' after 40 ns;
			register_file(63) <= Bus_C;
		end case;
	END IF;
END PROCESS;

END;
