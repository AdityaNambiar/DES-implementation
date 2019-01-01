import java.util.*;
import java.math.*;

class DES2{
	// PC1 => 56 bit
	static int[] PC1 = {57,  49,   41,  33,   25,   17,   9,
                1,  58,   50,  42,   34,   26,  18,
               10,   2,   59,  51,   43,   35,  27,
               19,  11,    3,  60,   52,   44,  36,
               63,  55,   47,  39,   31,   23,  15,
                7,  62,   54,  46,   38,   30,  22,
               14,   6,   61,  53,   45,   37,  29,
               21,  13,    5,  28,   20,   12,   4};
	// shifts => 16 bit
	static int[] shifts = {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};
	// PC2 => 48 bit
	static int[] PC2 = {14,   17,  11,   24,    1,   5,
                3,   28,  15,    6,   21,  10,
               23,   19,  12,    4,   26,   8,
               16,    7,  27,   20,   13,   2,
               41,   52,  31,   37,   47,  55,
               30,   40,  51,   45,   33,  48,
               44,   49,  39,   56,   34,  53,
               46,   42,  50,   36,   29,  32};

	static <T> void output(T x){ // Java Generic Function -- src: GsfGs.com
		System.out.println(x);
	}

	static StringBuilder divide(StringBuilder full, String request){
		int i;
		int mid = full.length()/2; // 56/2 = 28
		StringBuilder firsthalf = new StringBuilder(mid);
		StringBuilder secondhalf = new StringBuilder(mid);

		if(request.equals("firsthalf")){
			firsthalf.append(full.substring(0,mid)); // range: [0,mid)
			return firsthalf;
		}
		else if(request.equals("secondhalf")){
			secondhalf.append(full.substring(mid,full.length()));
			return secondhalf;
		}
		else{
			return null;
		}
	}

	static StringBuilder rotate(StringBuilder s, int count){
		int[] a = new int[s.length()];
		for (int j = 0; j < s.length(); j++ ){
			a[j] = Integer.parseInt(""+s.charAt(j));
		}

		StringBuilder _s = new StringBuilder(s.length());
		while (count-- != 0) {
			for (int i = 0; i < s.length(); i++){
				a[i%s.length()] = a[(i+1)%s.length()];
				_s.insert(i,a[i]);
			}
		}
		_s = new StringBuilder(_s.substring(0,s.length()));
		return _s;
	}

	static StringBuilder applyPC1(StringBuilder s, String binK){
		//Append characters of binKey[] to keyPlus[]
		//Append characters in the order of the elements of PC1... binKey[ PC1[i] ]
		for(int i = 0; i <= 55;i++){
			int index = PC1[i];
			s.insert(i,binK.charAt(index));
		}
		return s;
	}

	static StringBuilder applyPC2(StringBuilder kP){
		StringBuilder s = new StringBuilder(48);
		for(int i = 1; i <= 48;i++) {
			int index = PC2[i-1];
			s.insert(i-1,kP.toString().charAt(index-1));
		}
		return s;
	}

	public static void main(String args[]) {
			String hexKey= "133457799BBCDFF1";
			String _hexKey = new BigInteger(hexKey,16).toString(2);
			// Binary representation of the original Key => bin.
			// "%64s" just means the String is of 64 bits length and  'replace()' method is used for replacing spaces with zeroes. {representation purpose only}
			String binKey = String.format("%64s",Long.toBinaryString(Long.valueOf(_hexKey,2))).replace(' ','0');
		// ******* OBTAINED Binary equivalent of given Hex Key => binKey *******

			/* ~~ APPLY PC1 */
			StringBuilder keyPlus = new StringBuilder(56);
			keyPlus = applyPC1(keyPlus, binKey);
			// ******* OBTAINED K+ *******

			/* ~~ DIVISIONS and ROTATIONS: */
			// for second half of K+ ~ The C array
			StringBuilder finalFirstHalf = new StringBuilder(28);
			StringBuilder newFirstHalf = new StringBuilder(28);

			// for second half of K+ ~ The D array
			StringBuilder finalSecondHalf = new StringBuilder(28);
			StringBuilder newSecondHalf = new StringBuilder(28);

			// final CD i.e. new K+:
			StringBuilder finalKeyPlus = new StringBuilder(57);

			// sb for Key
			StringBuilder key = new StringBuilder(48);

			/* ~~ GENERATE 16 SUB-KEYS */
			StringBuilder[] keys = new StringBuilder[16];

			for (int k = 0; k <= 15; k++ ){
					if(k == 0){ // Just to make sure that it is the first iteration only that the following code block runs for.
						finalFirstHalf = divide(keyPlus,"firsthalf");
						//output(String.format("Itr: %1$d, String: %2$s",k,finalFirstHalf));

						finalSecondHalf = divide(keyPlus,"secondhalf");
						//output(String.format("Itr: %1$d, String: %2$s",k,finalSecondHalf));
					}
					else{ // A recursive block
						finalFirstHalf = rotate(finalFirstHalf,shifts[k]);
						//output(String.format("Itr: %1$d, String: %2$s",k,finalFirstHalf));

						finalSecondHalf = rotate(finalSecondHalf,shifts[k]);
						//output(String.format("Itr: %1$d, String: %2$s",k,finalSecondHalf));
					}
					finalKeyPlus = finalFirstHalf.append(finalSecondHalf);
					newFirstHalf = new StringBuilder(finalKeyPlus.toString().replace(finalKeyPlus.substring(0,28),finalFirstHalf.toString())
																		 .substring(0,28));
					newSecondHalf = new StringBuilder(finalKeyPlus.toString().replace(finalKeyPlus.substring(28,56),finalSecondHalf.toString())
																			.substring(28,56));
					finalKeyPlus = newFirstHalf.append(newSecondHalf);
					keys[k] = applyPC2(finalKeyPlus);
					//output(String.format("String[%1$d] = %2$s",k,key));
			}
			for(int i = 0; i < 16; i++){
				output(String.format("Key[%1$d]: %2$s",i,keys[i]));
			}
			//******* OBTAINED new K+ and all 16 sub-keys
		}
}
