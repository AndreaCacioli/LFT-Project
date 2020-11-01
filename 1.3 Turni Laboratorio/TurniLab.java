class TurniLab
{
  public static boolean scan(String str)
  {
      int i = 0;
      int state = 0;

      while(state != -1 && i<str.length())
      {
        final char c = str.charAt(i);

        switch(state)
        {

              case 0:
                if(c == '0' || c== '2' || c == '4' || c== '6' || c=='8') state = 1;
                else if(c == '1' || c== '3' || c == '5' || c== '7' || c=='9') state = 2;
                else state = -1;
                break;
              case 1:
                if(c == '0' || c== '2' || c == '4' || c== '6' || c=='8') state = 1;
                else if(c == '1' || c== '3' || c == '5' || c== '7' || c=='9') state = 2;
                else if(c >= 'A' && c <= 'K') state = 3;
                else if(c >= 'L' && c <= 'Z') state = 4;
                else state = -1;
                break;
              case 2:
                if(c == '1' || c== '3' || c == '5' || c== '7' || c=='9') state = 2;
                if(c == '0' || c== '2' || c == '4' || c== '6' || c=='8') state = 1;
                else if(c >= 'A' && c <= 'K') state = 5;
                else if(c >= 'L' && c <= 'Z') state = 6;
                else state = -1;
                break;
              case 3:
                if(c >= 'a' && c <= 'z') state = 3;
                else state = -1;
                break;
              case 4:
                if(c >= 'a' && c <= 'z') state = 4;
                else state = -1;
                break;
              case 5:
                if(c >= 'a' && c <= 'z') state = 5;
                else state = -1;
                break;
              case 6:
                if(c >= 'a' && c <= 'z') state = 6;
                else state = -1;
                break;
        }
        i++;
      }
      return state == 3 || state == 6;

  }

  public static void main(String[] args)
  {
      System.out.println(scan(args[0]) ? "OK" : "NOPE");
  }
}
