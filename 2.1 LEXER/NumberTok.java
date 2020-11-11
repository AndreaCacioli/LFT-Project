public class NumberTok extends Token
{
    public static final int tag = 256;
    public long lexeme;
    NumberTok(long n)
    {
      super(tag);
      lexeme = n;
    }
    public String toString()
    {
      return "<" + tag + "," + String.valueOf(lexeme) + ">";
    }
}
