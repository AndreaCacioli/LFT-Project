public class Automa
{
    public static boolean scan(String s)
    {
    	int state = 0;
    	int i = 0;

    	while (state >= 0 && i < s.length())
      {
    	    final char c = s.charAt(i++);

    	    switch (state)
          {
            case 0:
              if(c=='A') state = 1;
              else state = 11;
              break;
            case 1:
              if(c== 'n') state = 2;
              else state = 10;
              break;
            case 2:
              if(c== 'd') state = 3;
              else state = 9;
              break;
            case 3:
              if(c== 'r') state = 4;
              else state = 8;
              break;
            case 4:
              if(c== 'e') state = 5;
              else state = 7;
              break;
            case 5:
              state = 6;
              break;
            case 7:
              if (c=='a') state = 6;
              else state = -1;
              break;
            case 8:
              if(c=='e') state = 7;
              else state = -1;
              break;
            case 9:
              if(c=='r') state = 8;
              else state = -1;
              break;
            case 10:
              if(c=='d') state = 9;
              else state = -1;
              break;
            case 11:
              if(c=='n') state = 10;
              else state = -1;
              break;
    	    }
    	}
    	return state == 6;
    }

    public static void main(String[] args)
    {
	     System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
