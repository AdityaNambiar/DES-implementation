# DES-implementation
DES algorithm implementation upto key generation part

There's a "2" in the name because it is the second attempt and atleast it was completed. 

There's one issue with it and it's that it has trouble accepting any hexadecimal key that 
begins with "A to F", most likely because the equivalent Binary of the Hex key was obtained using 
a explicit 64 bit length restrictive conversion applied on it using String.format().

