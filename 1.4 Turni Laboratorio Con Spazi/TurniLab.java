class TurniLab
{
  public static boolean scan(String str)
  {
      int i = 0;
      int state = 0;
      System.out.println("Analizing " + str);
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
                else if(c == ' ') state = 3;
                else if(c >= 'A' && c <= 'K') state = 4;
                else state = -1;
                break;
              case 2:
                if(c == '1' || c== '3' || c == '5' || c== '7' || c=='9') state = 2;
                if(c == '0' || c== '2' || c == '4' || c== '6' || c=='8') state = 1;
                else if(c == ' ') state = 5;
                else if(c >= 'L' && c <= 'Z') state = 6;
                else state = -1;
                break;
              case 3:
                if(c == ' ') state = 3;
                else if(c >= 'A' && c <= 'K') state = 4;
                else state = -1;
                break;
              case 4:
                if(c >= 'a' && c <= 'z') state = 4;
                else if(c==' ') state = 8;
                else state = -1;
                break;
              case 5:
                if(c== ' ') state = 5;
                else if(c>= 'L' && c <= 'Z') state = 6;
                else state = -1;
                break;
              case 6:
                if(c >= 'a' && c <= 'z') state = 6;
                else if(c == ' ') state = 7;
                break;
              case 7:
                if(c>= 'A' && c <= 'Z') state = 6;
                else state = -1;
              case 8:
              if(c>= 'A' && c <= 'Z') state = 4;
              else state = -1;

        }
        i++;
      }
      return state == 4 || state == 6;

  }

  public static void main(String[] args)
  {
      String string = "";                     //NECESSARIO per via degli spazi
      for(int i = 0; i< args.length;i++)
      {
        if(i != 0) string += " ";
        string += args[i];
      }
      System.out.println(scan(string) ? "OK" : "NOPE");
  }
}
