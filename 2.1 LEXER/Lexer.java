import java.io.*;
import java.util.*;

public class Lexer
{

    public static int line = 1; //TODO: Make sure line is always the right number
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

                readch(br);
                if(peek == '/') // // scenario
                {
                  while(peek != '\n' && peek != (char)-1)
                  {
                    readch(br);
                  }
                  if (peek == '\n')line++;
                  peek = ' ';
                  return lexical_scan(br);
                }
                else if(peek == '*') // /* scenario
                {

                  while(peek != (char)-1)
                  {
                    do
                    {
                      readch(br);
                    }while(peek != '*' && peek != (char)-1);
                    // read: /* something *

                    readch(br);
                    if(peek == '/') //read: /* something */
                    {
                      peek = ' ';
                      return lexical_scan(br);
                    }
                    // else do nothing and while restart
                  }
                  System.err.println("Comment not closed at line " + line);
                  return null;
                }
                else
                {

                  peek = ' ';
                  return Token.div;
                }


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
                  return Word.lt;
                }
            case '>':
                readch(br);
                if(peek == '=')
                {
                  peek = ' ';
                  return Word.ge;
                }
                else
                {
                  return Word.gt;
                }
            case '=':
                readch(br);
                if(peek == '=')
                {
                  peek = ' ';
                  return Word.eq;
                }
                else
                {
                  return Token.assign;
                }

            case (char)-1:
                return new Token(Tag.EOF);

            default:

                //Gestione Degli Identificatori
                if (Character.isLetter(peek) || peek == '_')
                {
                  Word word = new Word(257, "");

	                while(Character.isLetter(peek) || Character.isDigit(peek) || peek == '_')
                  {
                    word.lexeme += peek;
                    readch(br);
					          if(peek == '\n') line++;
                    if((!Character.isLetter(peek) && !Character.isDigit(peek) && peek != '_') || (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r'))
                    {


                      if(word.lexeme.equals("_"))
                      {
                        System.err.println("\'_\' at line " + line + " is not a valid identifier");
                        return null;
                      }
                      if(word.lexeme.equals("cond")) return Word.cond;
                      if(word.lexeme.equals("when")) return Word.when;
                      if(word.lexeme.equals("then")) return Word.then;
                      if(word.lexeme.equals("else")) return Word.elsetok;
                      if(word.lexeme.equals("while")) return Word.whiletok;
                      if(word.lexeme.equals("do")) return Word.dotok;
                      else return word;
                    }

                  }

                }


                //Gestione Dei Numeri
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
                       if(peek == '\n') line++;
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
         return null;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "./Input.txt";
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;

            do
            {
               tok = lex.lexical_scan(br);
               System.out.println("Scan: " + tok);
            }while (tok.tag != Tag.EOF);

			         System.out.println("Line: " + line);

            br.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
    }

}
