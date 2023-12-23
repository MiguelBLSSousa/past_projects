LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;
USE IEEE.numeric_std.ALL;
ENTITY microstore IS
  PORT (pc : IN integer;
        microcode : OUT std_logic_vector(15 DOWNTO 0));
END ENTITY microstore;

ARCHITECTURE behaviour OF microstore IS
  TYPE instr_mem IS ARRAY (0 TO 511) OF std_logic_vector(15 DOWNTO 0);
  CONSTANT instr_store : instr_mem := (OTHERS=>(OTHERS=>'0'));
BEGIN
  microcode <= instr_store(pc);
END ARCHITECTURE behaviour;
