class IdJava
{
  public static boolean scan(String str)
  {
      int i = 0;
      int state = 0;

      while(state != 1 && i<str.length())
      {
        final char c = str.charAt(i);

        switch(state)
        {

              case 0:
                if(c == '_') state = 2;
                else if(c >= '0' && c <= '9') state = 1;
                else state = 3;
                break;
              case 1:
                state = 1;
                break;
              case 2:
                if(c=='_') state = 2;
                else state = 3;
                break;
              case 3:
                state = 3;
                break;

        }
        i++;
      }
      return state == 3;

  }

  public static void main(String[] args)
  {
      System.out.println(scan(args[0]) ? "OK" : "NOPE");
  }
}
