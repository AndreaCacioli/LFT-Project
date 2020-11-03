public class es1_5 
{
    public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;
	int controllo;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
		if (ch >= 'A' && ch <= 'K')
			state = 1;
		    
		else if (ch >= 'L' && ch <= 'Z')
			state = 2;
		else
			state = 5;
		break;

	    case 1:
		if (ch >= 'A' && ch <= 'Z')
		    state = 5;
		else if (ch >= '0' && ch <= '9')
			{controllo = Character.getNumericValue(ch);
				if (controllo%2==0) state = 3;
				else state= 1;}
		else if (ch >= 'a' && ch <= 'z')
			state = 1;
		else
			state = 5;
		break;

	    case 2:
		if (ch >= 'A' && ch <= 'Z')
		    state = 5;
		else if (ch >= '0' && ch <= '9')
			{controllo = Character.getNumericValue(ch);
				if (controllo%2==0) state = 2;
				else state= 4;}
		else if (ch >= 'a' && ch <= 'z')
			state = 2;
		else
			state = 5;
		break;
		
		case 3:
		if (ch >= '0' && ch <= '9')
			{controllo = Character.getNumericValue(ch);
				if ( controllo%2==0) state = 3;
				else state= 1;}
		else 
			state = 5;
		break;
		
		case 4:
		if (ch >= '0' && ch <= '9')
			{controllo = Character.getNumericValue(ch);
				if (controllo%2==0) state = 2;
				else state= 4;}
		else 
			state = 5;
		break;
		
		case 5:
			state = 5;
		break;
		}

	}
	return state == 3 || state == 4;
	}
	

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}