public class NumberTok extends Token
{
    public long lexeme;
    NumberTok(long n)
    {
      super(256);
      lexeme = n;
    }
    public String toString()
    {
      return "<256," + String.valueOf(lexeme) + ">";
    }
}
