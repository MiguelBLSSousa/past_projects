LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;
USE IEEE.numeric_std.ALL;
ENTITY microstore IS
  PORT (pc : IN integer range 0 to 511;
        microcode : OUT std_logic_vector(15 DOWNTO 0));
END ENTITY microstore;

