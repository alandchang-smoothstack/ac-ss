public class AssignmentOne {
	public static void main(String[] args) {
		String s1 = null;
		String s2 = null;
		
		System.out.println("1)");
		s1 = "****";
		s2 = ".........";
		for (int i=0; i<s1.length(); i++) {
			System.out.println(s1.substring(0, i+1));
		}
		System.out.println(s2);
		System.out.println("\n2)");
		
		s1 = "****";
		s2 = "..........";
		System.out.println(s2);
		for (int i=0; i<s1.length(); i++) {
			System.out.println(s1.substring(0, s1.length()-i));
		}
		
		System.out.println("\n3)");
		s1 = "*******";
		s2 = "...........";
		for (int i=0; i<s1.length(); i+=2) {
			System.out.println(String.format("%" + ((s2.length()+i)/2+1) + "s", s1.substring(0, i+1)));

		}
		System.out.println(s2);
		
		System.out.println("\n4)");
		s1 = "*******";
		s2 = "............";
		System.out.println(s2);
		for (int i=0; i<s1.length(); i+=2) {
			System.out.println(String.format("%" + ((s2.length()+s1.length()-i)/2) + "s", s1.substring(0, s1.length()-i)));
		}
	}
}