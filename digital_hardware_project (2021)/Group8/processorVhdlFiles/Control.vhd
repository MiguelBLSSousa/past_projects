library IEEE;
use IEEE.std_logic_1164.ALL;
use IEEE.numeric_std.ALL;
entity Control is
  	port (
    		clk : in std_logic;
    		reset : in std_logic; 
    		microcode: in std_logic_vector (15 downto 0);
		opvalue: out std_logic_vector (2 downto 0);
		Sel_A: out std_logic_vector (5 downto 0);
		Sel_B: out std_logic_vector (5 downto 0);
		M_Rd: out std_logic;
		M_Wr: out std_logic;
		pc: out integer range 0 to 511;
		branch_conditions: in std_logic_vector(3 downto 0);
		IR_Bit: in std_logic;
		I_Index: out integer;
		jump_address: integer
 	 );
end entity Control;

architecture structure of Control is

signal temp_pc: integer;
signal temp_microcode: std_logic_vector(15 downto 0);
signal branching: std_logic; 

alias CC_N : std_logic is branch_conditions(3); --negative
alias CC_Z : std_logic is branch_conditions(2); --zero
alias CC_V : std_logic is branch_conditions(1); --overflow
alias CC_C : std_logic is branch_conditions(0); --carry

begin 

process (clk, reset, microcode, branch_conditions, IR_Bit) 

variable f1_read: std_logic_vector(1 downto 0);
variable f2_read: std_logic_vector(3 downto 0);
variable f3_read: std_logic_vector(4 downto 0);
variable f4_read: std_logic_vector(5 downto 0);
variable f5_read: std_logic_vector(4 downto 0);

begin

if reset = '0' then

	f1_read := "00";
	f2_read := "0000";
	f3_read := "00000";
	f4_read := "000000";
	f5_read := "00000";

	Sel_A <= (others => '0');
	Sel_B <= (others => '0');
	
	opvalue <= "000";
	M_Rd <= '0';
	M_Wr <= '0';
	temp_pc <= 0;
	pc <= 0;
	I_Index <= 0;
	temp_microcode <= "0000000000000000";
	branching <= '0';

elsif rising_edge(clk) then

	pc <= temp_pc;				-- program counter used to retrieve microcode from microstore
	I_Index <= 0 after 20 ns;		-- instruction index for register file is reset after one clock cycle;


	if (IR_Bit = '1' AND temp_microcode /= microcode) OR temp_pc = 0 OR branching = '1' then

	opvalue <= "000";			-- operation value for ALU reset
	M_Wr <= '0';				-- memeory write bit for main memory reset
	M_Rd <= '0';				-- memeory read bit for main memory reset
				
	if branching /= '1' then		-- vectors to read different formats used for instruction set
		f1_read := microcode(15 downto 14);
		f2_read := microcode(15 downto 12);
		f3_read := microcode(15 downto 11);
		f4_read := microcode(15 downto 10);
		f5_read := microcode(15 downto 11);
	end if;

	case f1_read is 					--beginning of instruction set, code will check for different formats and allocate 
		when "00" => 					--the data according to the format to Sel_A and Sel_B. State bits will also be sent out
			I_Index <= 2;				--such as memory read, operation values, etc. program counter increased into temporary pc
			M_Rd <= '1';				--and set to pc which retrieves microcode at beginning of code
			Sel_A <= microcode(5 downto 0); 
			Sel_B <= microcode(11 downto 6); 
			temp_pc <= temp_pc + 1;
		when "01" => 
			I_Index <= 3;
			M_Wr <= '1';
			Sel_A <= microcode(5 downto 0);
			Sel_B <= microcode(11 downto 6);
			temp_pc <= temp_pc + 1;	
		when others => 
	
	case f2_read is	
		when "1000" => 
			I_Index <= 1;
			Sel_B <= microcode(11 downto 6);
			Sel_A <= microcode(5 downto 0);
			temp_pc <= temp_pc + 1;	
		when "1001" => 
			I_Index <= 5;
			Sel_B <= microcode(11 downto 6);
			Sel_A <= microcode(5 downto 0);
			opvalue <= "001";
			temp_pc <= temp_pc + 1;
		when "1010" => 
			I_Index <= 5;
			Sel_B <= microcode(11 downto 6);
			Sel_A <= microcode(5 downto 0);
			opvalue <= "010";
			temp_pc <= temp_pc + 1;
		when "1011" => 
			I_Index <= 5;
			Sel_A <= microcode(11 downto 6);
			Sel_B <= microcode(5 downto 0);
			opvalue <= "011";
			temp_pc <= temp_pc + 1;
		when "1100" => 
			I_Index <= 5;
			Sel_B <= microcode(11 downto 6);
			Sel_A <= microcode(5 downto 0);
			opvalue <= "100";
			temp_pc <= temp_pc + 1;
		when "1101" => 
			I_Index <= 5;
			Sel_B <= microcode(11 downto 6);
			Sel_A <= microcode(5 downto 0);
			opvalue <= "101";
			temp_pc <= temp_pc + 1;
		when others => 
	
	case f5_read is 
		when "11101" => 
			I_Index <= 4;
			Sel_B(4 downto 0) <= microcode(10 downto 6);
			Sel_A <= microcode(5 downto 0);
			opvalue <= "011";
			temp_pc <= temp_pc + 1;
		when others  => 

	case f3_read is 	
		when "11110" => 
			I_Index <= 6;
			Sel_A <= microcode(5 downto 0);
			if branching = '0' then
				branching <= '1';
			else branching <= '0';
			temp_pc <= jump_address;
			end if;
		when "11111" => 
			I_Index <= 6;
			case microcode(10 downto 9) is
				when "01" => 
					if CC_N = '1' then
						Sel_A <= microcode(5 downto 0);
						if branching = '0' then
							branching <= '1';
						else branching <= '0';
						end if;
						temp_pc <= jump_address;
					else
						temp_pc <= temp_pc + 1;
					end if;
				when "11" => 
					if CC_V = '1' then 
						Sel_A <= microcode(5 downto 0);
						if branching = '0' then
							branching <= '1';
						else branching <= '0';
						end if;
						temp_pc <= jump_address;					
					else
						temp_pc <= temp_pc + 1;
					end if;
				when "10" => 
					if CC_C = '1' then 
						Sel_A <= microcode(5 downto 0);
						if branching = '0' then
							branching <= '1';
						else branching <= '0';
						end if;
						temp_pc <= jump_address;						
					else
						temp_pc <= temp_pc + 1;
					end if;
				when "00" => 
					if CC_Z = '1' then 
						Sel_A <= microcode(5 downto 0);
						if branching = '0' then
							branching <= '1';
						else branching <= '0';
						end if;
						temp_pc <= jump_address;
					else
						temp_pc <= temp_pc + 1;
					end if;
				when others =>
			end case;
		when others => 
			
	case f4_read is	
		when "111000" => 
			I_Index <= 5;
			Sel_B <= (others => '0');
			Sel_A <= microcode(5 downto 0);
			opvalue <= "110";
			temp_pc <= temp_pc + 1;
			--pc <= temp_pc;
		when "111001" => 
			I_Index <= 5;
			Sel_B <= (others => '0');
			Sel_A <= microcode(5 downto 0);
			opvalue <= "111";
			temp_pc <= temp_pc + 1;
			--pc <= temp_pc;
		when others  =>
	end case; end case; end case; end case; end case;
	
	temp_microcode <= microcode;

	end if;	
end if;

end process;
end;

