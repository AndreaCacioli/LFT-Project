import java.io.*;
import java.util.*;

public class Lexer
{

    public static int line = 1;
    private char peek = ' ';

    private void readch(BufferedReader br)
    {
        try
        {
            peek = (char) br.read();
        }
        catch (IOException exc)
        {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br)
    {
      try {


        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r')
        {
            if (peek == '\n') line++;
            readch(br);
        }

        switch (peek)
        {
            case '!':
                peek = ' ';
                return Token.not;
            case '(':
                peek = ' ';
                return Token.lpt;
            case ')':
                peek = ' ';
                return Token.rpt;
            case '{':
                peek = ' ';
                return Token.lpg;
            case '}':
                peek = ' ';
                return Token.rpg;
            case '+':
                peek = ' ';
                return Token.plus;
            case '-':
                peek = ' ';
                return Token.minus;
            case '*':
                peek = ' ';
                return Token.mult;
            case '/':
                peek = ' ';
                return Token.div;
            case ';':
                peek = ' ';
                return Token.semicolon;


            case '&':
                readch(br);
                if (peek == '&')
                {
                    peek = ' ';
                    return Word.and;
                }
                else
                {
                    System.err.println("Erroneous character" + " after & : "  + peek );
                    return null;
                }
            case '|':
                readch(br);
                if (peek == '|')
                {
                    peek = ' ';
                    return Word.or;
                }
                else
                {
                    System.err.println("Erroneous character" + " after & : "  + peek );
                    return null;
                }

            case '<':
                br.mark(20);
                readch(br);
                if(peek == '=')
                {
                  peek = ' ';
                  return Word.le;
                }
                else if(peek == '>')
                {
                  peek = ' ';
                  return Word.ne;
                }
                else
                {
                  br.reset();

                  peek = ' ';
                  return Word.lt;
                }
            case '>':
                br.mark(20);
                readch(br);
                if(peek == '=')
                {
                  peek = ' ';
                  return Word.ge;
                }
                else
                {
                  br.reset();

                  peek = ' ';
                  return Word.gt;
                }
            case '=':
                br.mark(20);
                readch(br);
                if(peek == '=')
                {
                  peek = ' ';
                  return Word.eq;
                }
                else
                {
                  br.reset();

                  peek = ' ';
                  return Token.assign;
                }

            case (char)-1:
                return new Token(Tag.EOF);

            default:


                if (Character.isLetter(peek))
                {
                  Word word = new Word(257, "");

	                while(Character.isLetter(peek) || Character.isDigit(peek))
                  {
                    word.lexeme += peek;

                    readch(br);


                    if((!Character.isLetter(peek) && !Character.isDigit(peek)) || (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r'))
                    {

                      if(word.lexeme.equals("case")) return Word.casetok;
                      if(word.lexeme.equals("when")) return Word.when;
                      if(word.lexeme.equals("then")) return Word.then;
                      if(word.lexeme.equals("else")) return Word.elsetok;
                      if(word.lexeme.equals("while")) return Word.whiletok;
                      if(word.lexeme.equals("do")) return Word.dotok;
                      else return word;
                    }

                  }

                }



                else if (Character.isDigit(peek))
                {
                   if(peek == '0')
                   {
                     readch(br);
                     if(Character.isDigit(peek))
                     {
                       System.err.println("Erroneous character: " + peek );
                       return null;
                     }
                     else
                     {
                       return new NumberTok(0);
                     }
                   }

                   NumberTok n = new NumberTok(Character.getNumericValue(peek));
                   while(Character.isDigit(peek))
                   {
                     readch(br);

                     if(!Character.isDigit(peek) || (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r'))
                     {
                       return n;
                     }
                     else
                     {
                       n.lexeme*=10;
                       n.lexeme+=Character.getNumericValue(peek);
                     }
                   }

                }
                else
                {
                        System.err.println("Erroneous character: " + peek );
                        return null;
                }
         }
      } catch(Exception exception)
      {
        exception.printStackTrace();
       }
         return null;

    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/Users/andreacacioli/Desktop/Universitá/AA2Sems1/LFT_Lab/2.1 LEXER/Input.txt";
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;

            do
            {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);

            }while (tok.tag != Tag.EOF);

            br.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
    }

}
